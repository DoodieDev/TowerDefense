package doodieman.towerdefense.sheets;

import doodieman.towerdefense.TowerDefense;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bukkit.Bukkit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SheetsDataManager {

    private Workbook workbook;

    private final String sheetsURL = "https://docs.google.com/spreadsheets/d/1KOcPr-wgWsuWnqi1QG3QvNp6tCQSajreNfNrpaIs9UE/export?type=xlsx";

    public SheetsDataManager() {

    }

    private String getFilePath() {
        return TowerDefense.getInstance().getDataFolder().toString() + "/sheetsdata.xlsx";
    }

    private void initializeWorkbook() throws IOException, InvalidFormatException {
        this.workbook = WorkbookFactory.create(new File(this.getFilePath()));
    }

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


}
