package cn.idev.excel.analysis.v07.handlers.sax;

import cn.idev.excel.analysis.v07.handlers.CellFormulaTagHandler;
import cn.idev.excel.analysis.v07.handlers.CellInlineStringValueTagHandler;
import cn.idev.excel.analysis.v07.handlers.CellTagHandler;
import cn.idev.excel.analysis.v07.handlers.CellValueTagHandler;
import cn.idev.excel.analysis.v07.handlers.CountTagHandler;
import cn.idev.excel.analysis.v07.handlers.HyperlinkTagHandler;
import cn.idev.excel.analysis.v07.handlers.MergeCellTagHandler;
import cn.idev.excel.analysis.v07.handlers.RowTagHandler;
import cn.idev.excel.analysis.v07.handlers.XlsxTagHandler;
import cn.idev.excel.constant.ExcelXmlConstants;
import cn.idev.excel.context.xlsx.XlsxReadContext;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
@Slf4j
public class XlsxRowHandler extends DefaultHandler {
    private final XlsxReadContext xlsxReadContext;
    private static final Map<String, XlsxTagHandler> XLSX_CELL_HANDLER_MAP = new HashMap<>(64);

    static {
        CellFormulaTagHandler cellFormulaTagHandler = new CellFormulaTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.CELL_FORMULA_TAG, cellFormulaTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_CELL_FORMULA_TAG, cellFormulaTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_CELL_FORMULA_TAG, cellFormulaTagHandler);
        CellInlineStringValueTagHandler cellInlineStringValueTagHandler = new CellInlineStringValueTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.CELL_INLINE_STRING_VALUE_TAG, cellInlineStringValueTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_CELL_INLINE_STRING_VALUE_TAG, cellInlineStringValueTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_CELL_INLINE_STRING_VALUE_TAG, cellInlineStringValueTagHandler);
        CellTagHandler cellTagHandler = new CellTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.CELL_TAG, cellTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_CELL_TAG, cellTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_CELL_TAG, cellTagHandler);
        CellValueTagHandler cellValueTagHandler = new CellValueTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.CELL_VALUE_TAG, cellValueTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_CELL_VALUE_TAG, cellValueTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_CELL_VALUE_TAG, cellValueTagHandler);
        CountTagHandler countTagHandler = new CountTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.DIMENSION_TAG, countTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_DIMENSION_TAG, countTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_DIMENSION_TAG, countTagHandler);
        HyperlinkTagHandler hyperlinkTagHandler = new HyperlinkTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.HYPERLINK_TAG, hyperlinkTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_HYPERLINK_TAG, hyperlinkTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_HYPERLINK_TAG, hyperlinkTagHandler);
        MergeCellTagHandler mergeCellTagHandler = new MergeCellTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.MERGE_CELL_TAG, mergeCellTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_MERGE_CELL_TAG, mergeCellTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_MERGE_CELL_TAG, mergeCellTagHandler);
        RowTagHandler rowTagHandler = new RowTagHandler();
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.ROW_TAG, rowTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.X_ROW_TAG, rowTagHandler);
        XLSX_CELL_HANDLER_MAP.put(ExcelXmlConstants.NS2_ROW_TAG, rowTagHandler);
    }

    public XlsxRowHandler(XlsxReadContext xlsxReadContext) {
        this.xlsxReadContext = xlsxReadContext;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        XlsxTagHandler handler = XLSX_CELL_HANDLER_MAP.get(name);
        if (handler == null || !handler.support(xlsxReadContext)) {
            return;
        }
        xlsxReadContext.xlsxReadSheetHolder().getTagDeque().push(name);
        handler.startElement(xlsxReadContext, name, attributes);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String currentTag = xlsxReadContext.xlsxReadSheetHolder().getTagDeque().peek();
        if (currentTag == null) {
            return;
        }
        XlsxTagHandler handler = XLSX_CELL_HANDLER_MAP.get(currentTag);
        if (handler == null || !handler.support(xlsxReadContext)) {
            return;
        }
        handler.characters(xlsxReadContext, ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        XlsxTagHandler handler = XLSX_CELL_HANDLER_MAP.get(name);
        if (handler == null || !handler.support(xlsxReadContext)) {
            return;
        }
        handler.endElement(xlsxReadContext, name);
        xlsxReadContext.xlsxReadSheetHolder().getTagDeque().pop();
    }
}
