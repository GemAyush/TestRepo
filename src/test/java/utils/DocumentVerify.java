package utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.openqa.selenium.WebDriver;
import resources.Locators;

import java.io.File;
import java.util.Arrays;

public class DocumentVerify {

    static boolean flag;
    public static void pdfDocumentVerify(WebDriver driver, String childHandle, String documentTitle) throws InterruptedException{
        flag = false;
        Thread.sleep(5000);
        try{
            if(driver.findElement(Locators.documents(documentTitle)).isDisplayed()){
//            Assert.assertEquals(true, driver.findElement(Locators.documents(documentTitle)).isDisplayed());
                System.out.println("1 :" + documentTitle);
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
                    System.out.println("2 :" + documentTitle);
                }
            }
        }
    }
    public static void excelDocumentVerify(String documentTitle){
        File dir = new File(System.getProperty("user.home") + "/Downloads");
        File[] dir_contents = dir.listFiles();
        Arrays.sort(dir_contents, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        if (dir_contents[0].getName().contains(".csv") || dir_contents[0].getName().contains(".xls")) {
            System.out.println("3 :" + documentTitle);
        }
    }
}
