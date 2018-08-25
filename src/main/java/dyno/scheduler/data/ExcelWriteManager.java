/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.DataModel;
import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.datamodels.WorkCenterModel;
import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.ExcelUtil;
import dyno.scheduler.utils.GeneralSettings;
import dyno.scheduler.utils.LogUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Prabash
 */
public class ExcelWriteManager extends DataWriteManager
{

    private String xlsxFilePath;
    private FileInputStream inputStream;

    @Override
    public boolean addData(List<? extends DataModel> dataList, DataModelEnums.DataModelType dataModelType)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateData(List<? extends DataModel> dataList, DataModelEnums.DataModelType dataModelType)
    {
        try
        {
            xlsxFilePath = GeneralSettings.getDefaultExcelFile();
            String excelSheetName = ExcelUtil.getStorageName(dataModelType);
            switch (dataModelType)
            {
                case ShopOrder:
                {
                    return updateShopOrderData((List<ShopOrderModel>) dataList, excelSheetName);
                }
                case ShopOrderOperation:
                {
                    return updateShopOrderOperationData((List<ShopOrderOperationModel>) dataList, excelSheetName);
                }
                case WorkCenter:
                {
                    return updateWorkCenterData((List<WorkCenterModel>) dataList, excelSheetName);
                }
                case WorkCenterAllocation:
                {
                    return updateWorkCenterOpALlocData((List<WorkCenterOpAllocModel>) dataList, excelSheetName);
                }
                default:
                {
                    return false;
                }

            }
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean addShopOrderData(List<ShopOrderModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addShopOrderOperationData(List<ShopOrderOperationModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addWorkCenterData(List<WorkCenterModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addWorkCenterOpALlocData(List<WorkCenterOpAllocModel> workCenterOpAllocs, String storageName)
    {
        try
        {
            // Obtain a workbook from the excel file
            inputStream = new FileInputStream(xlsxFilePath);
            // Create a DataFormatter to format and get each cell's value as String
            try (Workbook workbook = WorkbookFactory.create(inputStream))
            {
                // Create a DataFormatter to format and get each cell's value as String
                DataFormatter dataFormatter = new DataFormatter();
                DateTimeFormatter dateFormat = DateTimeUtil.getDateFormat();
                // Get Sheet at index 0
                Sheet sheet = workbook.getSheet(storageName);
                workCenterOpAllocs.forEach((WorkCenterOpAllocModel workCenterOpAlloc) ->
                {
                    for (Row row : sheet)
                    {
                        if (dataFormatter.formatCellValue(row.getCell(0)).equals(workCenterOpAlloc.getWorkCenterNo())
                                && dateFormat.parseDateTime(dataFormatter.formatCellValue(row.getCell(1))).equals(workCenterOpAlloc.getOperationDate()))
                        {
                            SortedSet<String> keys = new TreeSet<>(workCenterOpAlloc.getTimeBlockAllocation().keySet());

                            keys.forEach((String key) ->
                            {
                                int currentOp = workCenterOpAlloc.getTimeBlockAllocation().get(key);
                                if (currentOp != 0)
                                {
                                    int index = getTimeBlockExcelIndex(key);
                                    // Get the Cell at index 2 from the above row
                                    Cell cell = row.getCell(index);

                                    // Create the cell if it doesn't exist
                                    if (cell == null)
                                    {

                                        cell = row.createCell(index);
                                    }

                                    // Update the cell's value
                                    cell.setCellValue(currentOp);
                                }
                            });
                        }

                    }
                });

                inputStream.close();
                // Write the output to the file
                try (FileOutputStream fileOut = new FileOutputStream(xlsxFilePath))
                {
                    workbook.write(fileOut);
                    // Closing the workbook
                }
            }

        } catch (FileNotFoundException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        } catch (IOException | InvalidFormatException | EncryptedDocumentException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        } finally
        {
            try
            {
                inputStream.close();
            } catch (IOException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean updateShopOrderData(List<ShopOrderModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateShopOrderOperationData(List<ShopOrderOperationModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateWorkCenterData(List<WorkCenterModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateWorkCenterOpALlocData(List<WorkCenterOpAllocModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * returns the column index related to the specific timeBlock
     *
     * @param timeBlock timeBlocValue
     * @return excelIndex
     */
    private static int getTimeBlockExcelIndex(String timeBlock)
    {
        int index = -1;
        int startingIndex = 2;
        switch (timeBlock)
        {
            case "TB1":
                index = startingIndex;
                break;
            case "TB2":
                index = startingIndex + 1;
                break;
            case "TB3":
                index = startingIndex + 2;
                break;
            case "TB4":
                index = startingIndex + 3;
                break;
            case "TB5":
                index = startingIndex + 4;
                break;
            case "TB6":
                index = startingIndex + 5;
                break;
            case "TB7":
                index = startingIndex + 6;
                break;
            case "TB8":
                index = startingIndex + 7;
                break;
            default:
                break;
        }

        return index;
    }
}
