package parallel;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.hu.Ha;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import resources.Locators;
import utils.DocumentVerify;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static utils.Actions.isClickable;

public class ParallelTest {
    public WebDriver driver;
    public List<String> fundTitle = new ArrayList<>();

    List<String> urlStatus = new ArrayList<>();
    public HashMap<String, List<String>> fundDocumentName = new HashMap<>();
    public static HashMap<List<String>, HashMap<String, List<String>>> funds = new HashMap<>();
    SoftAssert softAssert = new SoftAssert();
    @Before
    public void driverSetUp(){
        driver = DriverClass.setup();
    }
    @Given("You're on {string} and get its Title")
    public void ChromeTest(String url)
    {
        driver.get(url);
        driver.manage().window().maximize();
        fundTitle.add(0, driver.getTitle());
        fundTitle.add(1, url);
    }
    @When("Click On Document")
    public void clickOnDocument(){
        try{
            if(isClickable(driver, Locators.headerDocument)){
                driver.findElement(Locators.headerDocument).click();
            }
        }
        catch (ElementNotInteractableException e){
            softAssert.assertTrue(false, "Element Not Clickable!");
        }
    }
    @Then("Open every {string} to see if loading correctly")
    public void openEveryDocumentToSeeIfLoadingCorrectly(String Documents) throws InterruptedException {
        try{
            if(isClickable(driver, Locators.seeMoreBtn)){
                driver.findElement(Locators.seeMoreBtn).click();
            }
        }
        catch (ElementNotInteractableException e){
            softAssert.assertTrue(false, "Element Not Clickable!");
        }
        Thread.sleep(5000);
        String[] documents = Documents.split(",");

        for(String document : documents) {
            List<String> urlStatus = new ArrayList<>();
            try {
                if (isClickable(driver, Locators.documents(document))) {
                    driver.findElement(Locators.documents(document)).click();
                }
            }
            catch (ElementNotInteractableException e){
                fundDocumentName.put(document, null);
                softAssert.assertTrue(false, "Element Not Clickable!");
            }
            urlStatus.add(0, driver.findElement(Locators.documents(document)).getAttribute("href"));
            System.out.println(driver.findElement(Locators.documents(document)).getAttribute("href"));
            urlStatus.add(1, "null");
            fundDocumentName.put(document, urlStatus);
            Thread.sleep(5000);
            Set<String> windowHandles = driver.getWindowHandles();
            String [] handle = windowHandles.toArray(new String[windowHandles.size()]);
            try {
                if(windowHandles.size() > 1) {
                    driver.switchTo().window(handle[1]);
                    DocumentVerify.pdfDocumentVerify(driver, document, fundDocumentName);
                    driver.close();
                }
                else{
                    DocumentVerify.excelDocumentVerify(document, fundDocumentName);
                }
            } catch (AssertionError e) {

            } finally {
                driver.switchTo().window(handle[0]);
            }
        }
        softAssert.assertAll();
    }
    @And("Store the Documents")
    public void storeTheDocuments() {
        funds.put(fundTitle, fundDocumentName);
    }

    @After
    public void close_browser(){
        driver.quit();
    }
    @AfterAll
    public static void before_or_after_all(){
        int idx = 0;
        String details = "";
        String cssStyles = "<style type=\"text/css\">\n" +
                ".tg  {border-collapse:collapse;border-color:#aabcfe;border-spacing:0;width: 100%;}\n" +
                ".tg td{background-color:#e8edff;border-color:#aabcfe;border-style:solid;border-width:1px;color:#000;\n" +
                "  font-family:Arial, sans-serif;font-size:14px;overflow:hidden;padding:10px 5px;word-break:normal;}\n" +
                ".tg th{background-color:#b9c9fe;border-color:#aabcfe;border-style:solid;border-width:1px;color:#000;\n" +
                "  font-family:Arial, sans-serif;font-size:14px;font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}\n" +
                ".tg .tg-c3ow{border-color:inherit;text-align:center;vertical-align:top}\n" +
                ".tg .tg-0pky{border-color:inherit;text-align:left;vertical-align:top}\n" +
                ".tg .bg-green {background-color: rgb(15, 255, 80);} /* New class for green background */\n" +
                ".tg .bg-red {background-color: rgb(255, 49, 49);} /* New class for red background */\n" +
                "@media screen and (max-width: 767px) {.tg {width: auto !important;}.tg col {width: auto !important;}.tg-wrap {overflow-x: auto;-webkit-overflow-scrolling: touch;}}</style>";
        String header = cssStyles + "<table class=\"tg\">\n" +
                "<thead>\n" +
                "  <tr>\n" +
                "    <th class=\"tg-0pky\" >SNo.</th>\n" +
                "    <th class=\"tg-c3ow\">Fund Name</th>\n" +
                "    <th class=\"tg-0pky\">Document Name</th>\n" +
                "  </tr>\n" +
                "</thead>\n";
        String tbody = header + "<tbody>\n";
        for(HashMap.Entry<List<String>, HashMap<String, List<String>>> entry : funds.entrySet()){
            String funcValue = "";
            boolean flag = false;
            String headline = "  <tr>\n" +
                            "    <td class=\"tg-0pky\" rowspan=\""+ entry.getValue().size() +"\">"+idx+"</td>\n" +
                            "    <td class=\"tg-c3ow\" rowspan=\""+ entry.getValue().size() +"\"><a href=\""+entry.getKey().get(1)+"\">"+entry.getKey().get(0)+"</a></td>\n";

            funcValue = funcValue + headline;
            for(Map.Entry<String, List<String>> document : entry.getValue().entrySet()) {
                boolean status = Boolean.parseBoolean(document.getValue().get(1));
                String statusValue = "";
                if(status){
                    statusValue = " bg-green";
                }
                else if(!status){
                    statusValue = " bg-red";
                }
                else{
                    statusValue = " bg-yellow";
                }

                if(flag == false){
                    funcValue = funcValue + "  <td class=\"tg-0pky"+ statusValue +"\"><a href=\""+document.getValue().get(0)+"\">" + document.getKey() + "</a></td>\n" + "</tr>\n";
                    flag = true;
                }
                else {
                    funcValue = funcValue + "<tr>\n" +
                            "    <td class=\"tg-0pky" + statusValue + "\"><a href=\""+document.getValue().get(0)+"\">" + document.getKey() + "</a></td>\n" +
                            "  </tr>";
                }
            }
            idx++;
            details = details + funcValue;
        }
        String tbodyClose = tbody + details + "</tbody>\n" +
                "</table>";
        String filePath = "Report.html";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(tbodyClose);
            writer.close();
            System.out.println("HTML content has been written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
