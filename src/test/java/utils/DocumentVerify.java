package utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import resources.Locators;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static utils.Actions.isElementVisible;

public class DocumentVerify {

    static boolean flag;

    public static void pdfDocumentVerify(WebDriver driver, String documentTitle, HashMap<String, List<String>> fundDocumentName) throws InterruptedException{
        flag = false;
        List<String> urlStatus = new ArrayList<>();
        try{
            if(isElementVisible(driver, Locators.documents(documentTitle))){
                fundDocumentName.get(documentTitle).set(1, "true");
                flag = true;
            }
        }
        catch(Exception e){
//            e.printStackTrace();
        }
        finally {
            if(flag == false){
                String url = driver.getCurrentUrl();
                String UpdatedDocumentName = documentTitle.replace(" ", "%20");
                if(url.contains(UpdatedDocumentName)){
                    fundDocumentName.get(documentTitle).set(1, "true");
                }
            }
        }
        Assert.assertTrue(fundDocumentName.containsKey(documentTitle), "PDF document verification failed for: " + documentTitle);
        if(!fundDocumentName.containsKey(documentTitle)){
            fundDocumentName.get(documentTitle).set(1, "false");
        }
    }
    public static void excelDocumentVerify(String documentTitle, HashMap<String, List<String>> fundDocumentName){
        List<String> urlStatus = new ArrayList<>();
        File dir = new File(System.getProperty("user.home") + "/Downloads");
        File[] dir_contents = dir.listFiles();
        Arrays.sort(dir_contents, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        if (dir_contents[0].getName().contains(".csv") || dir_contents[0].getName().contains(".xls")) {
            fundDocumentName.get(documentTitle).set(1, "true");
            dir_contents[0].delete();
        }
        Assert.assertTrue(fundDocumentName.containsKey(documentTitle), "Excel document verification failed for: " + documentTitle);
        if(!fundDocumentName.containsKey(documentTitle)){
            fundDocumentName.get(documentTitle).set(1, "false");
        }
    }
}
