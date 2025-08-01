package cn.idev.excel.read.metadata.holder.xls;

import cn.idev.excel.read.metadata.ReadWorkbook;
import cn.idev.excel.read.metadata.holder.ReadWorkbookHolder;
import cn.idev.excel.support.ExcelTypeEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Workbook holder
 *
 *
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class XlsReadWorkbookHolder extends ReadWorkbookHolder {
    /**
     * File System
     */
    private POIFSFileSystem poifsFileSystem;
    /**
     * Format tracking HSSFListener
     */
    private FormatTrackingHSSFListener formatTrackingHSSFListener;
    /**
     * HSSFWorkbook
     */
    private HSSFWorkbook hssfWorkbook;
    /**
     * Bound sheet record list.
     */
    private List<BoundSheetRecord> boundSheetRecordList;
    /**
     * Need read sheet.
     */
    private Boolean needReadSheet;
    /**
     * Sheet Index
     */
    private Integer readSheetIndex;
    /**
     * Ignore record.
     */
    private Boolean ignoreRecord;

    /**
     * Has the current sheet already stopped
     */
    private Boolean currentSheetStopped;

    public XlsReadWorkbookHolder(ReadWorkbook readWorkbook) {
        super(readWorkbook);
        this.boundSheetRecordList = new ArrayList<BoundSheetRecord>();
        this.needReadSheet = Boolean.TRUE;
        setExcelType(ExcelTypeEnum.XLS);
        if (getGlobalConfiguration().getUse1904windowing() == null) {
            getGlobalConfiguration().setUse1904windowing(Boolean.FALSE);
        }
        ignoreRecord = Boolean.FALSE;
        currentSheetStopped = Boolean.TRUE;
    }
}
