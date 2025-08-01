package cn.idev.excel.context.xls;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.metadata.holder.xls.XlsReadSheetHolder;
import cn.idev.excel.read.metadata.holder.xls.XlsReadWorkbookHolder;

/**
 * A context is the main anchorage point of a ls xls reader.
 *
 *
 **/
public interface XlsReadContext extends AnalysisContext {
    /**
     * All information about the workbook you are currently working on.
     *
     * @return Current workbook holder
     */
    XlsReadWorkbookHolder xlsReadWorkbookHolder();

    /**
     * All information about the sheet you are currently working on.
     *
     * @return Current sheet holder
     */
    XlsReadSheetHolder xlsReadSheetHolder();
}
