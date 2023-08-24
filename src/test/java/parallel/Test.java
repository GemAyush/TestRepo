package parallel;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;


public class Test {

    @org.testng.annotations.Test
    public void testMethod(){
        SoftAssert softAssert = new SoftAssert();

        System.out.println("Ayush");
//        Assert.assertEquals(2, 5, "Not Matching Failing!");

        softAssert.assertEquals(2, 5, "Not Matching Failing!");
        softAssert.assertEquals("Ayush", "Saxena", "Not Equal Failing!");
        softAssert.assertEquals(false, false, "Good it's Working!");
        try{
            softAssert.assertAll();
        }
        catch (Exception e){
//            e.printStackTrace();
//            System.out.println("Assertion Error");
        }

        System.out.println("Saxena");
    }
}
