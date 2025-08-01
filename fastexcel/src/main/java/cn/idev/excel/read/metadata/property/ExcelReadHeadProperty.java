package cn.idev.excel.read.metadata.property;

import cn.idev.excel.metadata.ConfigurationHolder;
import cn.idev.excel.metadata.property.ExcelHeadProperty;
import java.util.List;

/**
 * Define the header attribute of excel
 *
 */
public class ExcelReadHeadProperty extends ExcelHeadProperty {

    public ExcelReadHeadProperty(ConfigurationHolder configurationHolder, Class headClazz, List<List<String>> head) {
        super(configurationHolder, headClazz, head);
    }
}
