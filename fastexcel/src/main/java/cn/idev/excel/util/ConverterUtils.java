package cn.idev.excel.util;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.ConverterKeyBuild;
import cn.idev.excel.converters.ConverterKeyBuild.ConverterKey;
import cn.idev.excel.converters.NullableObjectConverter;
import cn.idev.excel.converters.ReadConverterContext;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.exception.ExcelDataConvertException;
import cn.idev.excel.metadata.data.CellData;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.read.metadata.holder.ReadSheetHolder;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Converting objects
 *
 *
 **/
public class ConverterUtils {
    public static Class<?> defaultClassGeneric = String.class;

    private ConverterUtils() {}

    /**
     * Convert it into a String map
     *
     * @param cellDataMap
     * @param context
     * @return
     */
    public static Map<Integer, String> convertToStringMap(
            Map<Integer, ReadCellData<?>> cellDataMap, AnalysisContext context) {
        Map<Integer, String> stringMap = MapUtils.newHashMapWithExpectedSize(cellDataMap.size());
        ReadSheetHolder readSheetHolder = context.readSheetHolder();
        int index = 0;
        for (Map.Entry<Integer, ReadCellData<?>> entry : cellDataMap.entrySet()) {
            Integer key = entry.getKey();
            ReadCellData<?> cellData = entry.getValue();
            while (index < key) {
                stringMap.put(index, null);
                index++;
            }
            index++;
            if (cellData.getType() == CellDataTypeEnum.EMPTY) {
                stringMap.put(key, null);
                continue;
            }
            Converter<?> converter =
                    readSheetHolder.converterMap().get(ConverterKeyBuild.buildKey(String.class, cellData.getType()));
            if (converter == null) {
                throw new ExcelDataConvertException(
                        context.readRowHolder().getRowIndex(),
                        key,
                        cellData,
                        null,
                        "Converter not found, convert " + cellData.getType() + " to String");
            }
            try {
                stringMap.put(key, (String)
                        (converter.convertToJavaData(new ReadConverterContext<>(cellData, null, context))));
            } catch (Exception e) {
                throw new ExcelDataConvertException(
                        context.readRowHolder().getRowIndex(),
                        key,
                        cellData,
                        null,
                        "Convert data " + cellData + " to String error ",
                        e);
            }
        }
        return stringMap;
    }

    /**
     * Convert it into a Java object
     *
     * @param cellData
     * @param field
     * @param contentProperty
     * @param converterMap
     * @param context
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public static Object convertToJavaObject(
            ReadCellData<?> cellData,
            Field field,
            ExcelContentProperty contentProperty,
            Map<ConverterKey, Converter<?>> converterMap,
            AnalysisContext context,
            Integer rowIndex,
            Integer columnIndex) {
        return convertToJavaObject(
                cellData, field, null, null, contentProperty, converterMap, context, rowIndex, columnIndex);
    }

    /**
     * Convert it into a Java object
     *
     * @param cellData
     * @param field
     * @param clazz
     * @param contentProperty
     * @param converterMap
     * @param context
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public static Object convertToJavaObject(
            ReadCellData<?> cellData,
            Field field,
            Class<?> clazz,
            Class<?> classGeneric,
            ExcelContentProperty contentProperty,
            Map<ConverterKey, Converter<?>> converterMap,
            AnalysisContext context,
            Integer rowIndex,
            Integer columnIndex) {
        if (clazz == null) {
            if (field == null) {
                clazz = String.class;
            } else {
                clazz = field.getType();
            }
        }
        if (clazz == CellData.class || clazz == ReadCellData.class) {
            ReadCellData<Object> cellDataReturn = cellData.clone();
            cellDataReturn.setData(doConvertToJavaObject(
                    cellData,
                    getClassGeneric(field, classGeneric),
                    contentProperty,
                    converterMap,
                    context,
                    rowIndex,
                    columnIndex));
            return cellDataReturn;
        }
        return doConvertToJavaObject(cellData, clazz, contentProperty, converterMap, context, rowIndex, columnIndex);
    }

    private static Class<?> getClassGeneric(Field field, Class<?> classGeneric) {
        if (classGeneric != null) {
            return classGeneric;
        }
        if (field == null) {
            return defaultClassGeneric;
        }
        Type type = field.getGenericType();
        if (!(type instanceof ParameterizedType)) {
            return defaultClassGeneric;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length == 0) {
            return defaultClassGeneric;
        }
        Type actualType = types[0];
        if (!(actualType instanceof Class<?>)) {
            return defaultClassGeneric;
        }
        return (Class<?>) actualType;
    }

    /**
     * @param cellData
     * @param clazz
     * @param contentProperty
     * @param converterMap
     * @param context
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    private static Object doConvertToJavaObject(
            ReadCellData<?> cellData,
            Class<?> clazz,
            ExcelContentProperty contentProperty,
            Map<ConverterKey, Converter<?>> converterMap,
            AnalysisContext context,
            Integer rowIndex,
            Integer columnIndex) {
        Converter<?> converter = null;
        if (contentProperty != null) {
            converter = contentProperty.getConverter();
        }

        boolean canNotConverterEmpty =
                cellData.getType() == CellDataTypeEnum.EMPTY && !(converter instanceof NullableObjectConverter);
        if (canNotConverterEmpty) {
            return null;
        }

        if (converter == null) {
            converter = converterMap.get(ConverterKeyBuild.buildKey(clazz, cellData.getType()));
        }
        if (converter == null) {
            throw new ExcelDataConvertException(
                    rowIndex,
                    columnIndex,
                    cellData,
                    contentProperty,
                    "Error at row " + rowIndex + ", column " + columnIndex + ": Converter not found, convert "
                            + cellData.getType() + " to " + clazz.getName());
        }

        try {
            return converter.convertToJavaData(new ReadConverterContext<>(cellData, contentProperty, context));
        } catch (Exception e) {
            throw new ExcelDataConvertException(
                    rowIndex,
                    columnIndex,
                    cellData,
                    contentProperty,
                    "Error at row " + rowIndex + ", column " + columnIndex + ": Convert data " + cellData + " to "
                            + clazz + " error ",
                    e);
        }
    }
}
