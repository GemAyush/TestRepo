package parallel;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.Locators;
import utils.DocumentVerify;

import java.time.Duration;
import java.util.Set;

public class ParallelTest {

    public WebDriver driver;

    @Before
    public void initializeDriver(){
        driver = DriverClass.setup();
    }
    @Given("You're on {string} and get its Title")
    public void ChromeTest(String url)
    {
        //Initialize the chrome driver
        driver.get(url);
        driver.manage().window().maximize();
        System.out.println(driver.getTitle());
    }

    @When("Click On Document")
    public void clickOnDocument(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(Locators.headerDocument));
        driver.findElement(Locators.headerDocument).click();
    }
    @Then("Open every {string} to see if loading correctly")
    public void openEveryDocumentToSeeIfLoadingCorrectly(String Documents) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
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

            if(windowHandles.size() > 1) {
                driver.switchTo().window(handle[1]);
                DocumentVerify.pdfDocumentVerify(driver, handle[1], document);
                driver.close();
            }
            else{
                DocumentVerify.excelDocumentVerify(document);
            }
            driver.switchTo().window(parentWindow);
        }
    }

    @After
    public void close_browser(){
            driver.quit();
    }
}
