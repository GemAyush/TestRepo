package resources;

import org.openqa.selenium.By;

public class Locators {
    public static By headerDocument = By.xpath("//a[text() = 'Documents']");
    public static By seeMoreBtn = By.xpath("//a[text() = 'See More']");
    public static By documents(String documents){
        return By.xpath("//a[text() = '"+ documents +"']");
    }
}
