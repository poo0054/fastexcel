package cn.idev.excel.test.core.fill.style;

import cn.idev.excel.annotation.write.style.ContentFontStyle;
import cn.idev.excel.annotation.write.style.ContentStyle;
import cn.idev.excel.enums.BooleanEnum;
import cn.idev.excel.enums.poi.FillPatternTypeEnum;
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
public class FillStyleAnnotatedData {
    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 13)
    @ContentFontStyle(bold = BooleanEnum.TRUE, color = 19)
    private String name;

    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 10)
    @ContentFontStyle(bold = BooleanEnum.TRUE, color = 16)
    private Double number;

    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 17)
    @ContentFontStyle(bold = BooleanEnum.TRUE, color = 58)
    private Date date;

    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 12)
    @ContentFontStyle(bold = BooleanEnum.TRUE, color = 18)
    private String empty;
}
