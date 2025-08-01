package cn.idev.excel.test.core.converter;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.metadata.data.ReadCellData;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
@EqualsAndHashCode
public class ConverterReadData {
    @ExcelProperty("日期")
    private Date date;

    @ExcelProperty("本地日期")
    private LocalDate localDate;

    @ExcelProperty("本地日期时间")
    private LocalDateTime localDateTime;

    @ExcelProperty("布尔")
    private Boolean booleanData;

    @ExcelProperty("大数")
    private BigDecimal bigDecimal;

    @ExcelProperty("大整数")
    private BigInteger bigInteger;

    @ExcelProperty("长整型")
    private long longData;

    @ExcelProperty("整型")
    private Integer integerData;

    @ExcelProperty("短整型")
    private Short shortData;

    @ExcelProperty("字节型")
    private Byte byteData;

    @ExcelProperty("双精度浮点型")
    private double doubleData;

    @ExcelProperty("浮点型")
    private Float floatData;

    @ExcelProperty("字符串")
    private String string;

    @ExcelProperty("自定义")
    private ReadCellData<?> cellData;
}
