package parallel;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.hu.Ha;
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

public class ParallelTest {
    public WebDriver driver;
    public  String fundTitle;
    public HashMap<String, Boolean> fundDocumentName = new HashMap<>();
    public static HashMap<String, HashMap<String, Boolean>> funds = new HashMap<>();
    @Before
    public void driverSetUp(){
        driver = DriverClass.setup();
    }
    @Given("You're on {string} and get its Title")
    public void ChromeTest(String url)
    {
        driver.get(url);
        driver.manage().window().maximize();
        fundTitle = driver.getTitle();
    }
    @When("Click On Document")
    public void clickOnDocument(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(Locators.headerDocument));
        driver.findElement(Locators.headerDocument).click();
    }
    @Then("Open every {string} to see if loading correctly")
    public void openEveryDocumentToSeeIfLoadingCorrectly(String Documents) throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(Locators.seeMoreBtn));
        Thread.sleep(5000);
        driver.findElement(Locators.seeMoreBtn).click();

        Thread.sleep(5000);
        String[] documents = Documents.split(",");

        for(String document : documents) {
            WebElement webElement = driver.findElement(Locators.documents(document));
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
            webElement.click();
            String parentWindow = driver.getWindowHandle();
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
                // Log the assertion failure
                // Perform any necessary error handling
                // You can choose whether to continue or stop execution
            } finally {
                driver.switchTo().window(parentWindow);
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
        int idx = 1;
        String details = "";
        String cssStyles = "<style type=\"text/css\">\n" +
                ".tg  {border-collapse:collapse;border-color:#aabcfe;border-spacing:0;width: 100%;}\n" +
                ".tg td{background-color:#e8edff;border-color:#aabcfe;border-style:solid;border-width:1px;color:#669;\n" +
                "  font-family:Arial, sans-serif;font-size:14px;overflow:hidden;padding:10px 5px;word-break:normal;}\n" +
                ".tg th{background-color:#b9c9fe;border-color:#aabcfe;border-style:solid;border-width:1px;color:#039;\n" +
                "  font-family:Arial, sans-serif;font-size:14px;font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}\n" +
                ".tg .tg-c3ow{border-color:inherit;text-align:center;vertical-align:top}\n" +
                ".tg .tg-0pky{border-color:inherit;text-align:left;vertical-align:top}\n" +
                ".tg .bg-green {background-color: green;} /* New class for green background */\n" +
                ".tg .bg-red {background-color: red;} /* New class for red background */\n" +
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
        for(HashMap.Entry<String, HashMap<String, Boolean>> entry : funds.entrySet()){
            String funcValue = "";
            String headline = "  <tr>\n" +
                            "    <td class=\"tg-0pky\">"+idx+"</td>\n" +
                            "    <td class=\"tg-c3ow\">"+entry.getKey()+"</td>\n";

            funcValue = funcValue + headline;
            for(Map.Entry<String, Boolean> document : entry.getValue().entrySet()) {
                String status = document.getValue() ? " bg-green" : " bg-red";
                funcValue = funcValue + "  <td class=\"tg-0pky"+ status +"\">" + document.getKey() + "</td>\n";
            }
            funcValue = funcValue + "</tr>\n";
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
