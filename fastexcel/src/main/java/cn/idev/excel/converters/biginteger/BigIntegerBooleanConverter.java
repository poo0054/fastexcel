package cn.idev.excel.converters.biginteger;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import java.math.BigInteger;

/**
 * BigInteger and boolean converter
 *
 *
 */
public class BigIntegerBooleanConverter implements Converter<BigInteger> {

    @Override
    public Class<BigInteger> supportJavaTypeKey() {
        return BigInteger.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.BOOLEAN;
    }

    @Override
    public BigInteger convertToJavaData(
            ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (cellData.getBooleanValue()) {
            return BigInteger.ONE;
        }
        return BigInteger.ZERO;
    }

    @Override
    public WriteCellData<?> convertToExcelData(
            BigInteger value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (BigInteger.ONE.equals(value)) {
            return new WriteCellData<>(Boolean.TRUE);
        }
        return new WriteCellData<>(Boolean.FALSE);
    }
}
