package doodieman.towerdefense.sheetsdata;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.sheetsdata.dataobjects.SheetMobType;
import doodieman.towerdefense.sheetsdata.dataobjects.SheetRound;
import lombok.Getter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SheetsDataManager {

    @Getter
    private Workbook workbook;
    @Getter
    private Sheet mobSheet;
    @Getter
    private Sheet roundsSheet;

    final String sheetsURL = "https://docs.google.com/spreadsheets/d/1KOcPr-wgWsuWnqi1QG3QvNp6tCQSajreNfNrpaIs9UE/export?type=xlsx";

    @Getter
    private final List<SheetMobType> sheetMobsList;
    @Getter
    private final List<SheetRound> sheetRoundList;
    @Getter
    private final SheetsDataUtil util;

    public SheetsDataManager() {
        this.util = new SheetsDataUtil(this);

        this.sheetMobsList = new ArrayList<>();
        this.sheetRoundList = new ArrayList<>();

    }

    //Loads all SheetRound instances
    public void loadRounds() {

        //Go through all round rows until there is an empty
        int round = 1;
        while (this.roundsSheet.getRow(round - 1).getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {

            List<Object> roundData = new ArrayList<>();

            //Go through all the round cells until there is an empty
            int cellIndex = 0;
            while (this.roundsSheet.getRow(round - 1).getCell(cellIndex) != null) {
                Cell cell = this.roundsSheet.getRow(round - 1).getCell(cellIndex);
                roundData.add(this.getCellValue(cell));
                cellIndex++;
            }

            SheetRound sheetRound;
            try {
                sheetRound = new SheetRound(round, roundData);
            } catch (Exception exception) {
                TowerDefense.getInstance().announceForAdmins("§cError loading round "+round+". ("+exception.getMessage()+")");
                return;
            }

            this.sheetRoundList.add(sheetRound);
            round++;
        }
    }

    //Loads all the SheetMob instances
    public void loadMobs() {
        int row = 1;
        int length = 11;

        while (this.mobSheet.getRow(row).getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {

            List<Object> mobValues = this.getCellValues(mobSheet,row,0,row,length);
            SheetMobType mob;
            try {
                mob = new SheetMobType(mobValues);
            } catch (Exception exception) {
                TowerDefense.getInstance().announceForAdmins("§cError loading mob '"+mobValues.get(0)+"'. ("+exception.getMessage()+")");
                row++;
                continue;
            }

            this.sheetMobsList.add(mob);
            row++;
        }
    }

    //Get all the cells between two points
    public List<Object> getCellValues(Sheet sheet, int startRow, int startColumn, int endRow, int endColumn) {
        List<Object> objects = new ArrayList<>();
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                Cell cell = sheet.getRow(row).getCell(column);
                objects.add(this.getCellValue(cell));
            }
        }
        return objects;
    }

    //Get the cell value
    public Object getCellValue(Cell cell) {
        try {
            switch (cell.getCachedFormulaResultType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    return cell.getBooleanCellValue();
                case Cell.CELL_TYPE_ERROR:
                    return cell.getErrorCellValue();
                case Cell.CELL_TYPE_FORMULA:
                    return cell.getCellFormula();
                case Cell.CELL_TYPE_NUMERIC:
                    return cell.getNumericCellValue();
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                default:
                    return null;
            }
        } catch (IllegalStateException exception) {
            return null;
        }
    }

    //Initialize the environment
    private void initializeWorkbook() throws IOException, InvalidFormatException {
        this.workbook = WorkbookFactory.create(new File(this.getFilePath()));
        this.mobSheet = this.workbook.getSheet("rawdata_mobs");
        this.roundsSheet = this.workbook.getSheet("rawdata_rounds");
    }

    //Downloads the new .xlsx document online
    public void download() throws IOException, InvalidFormatException {
        URL url = new URL(sheetsURL);
        try (InputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(getFilePath())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
        this.initializeWorkbook();
    }

    private String getFilePath() {
        return TowerDefense.getInstance().getDataFolder().toString() + "/sheetsdata.xlsx";
    }

}
