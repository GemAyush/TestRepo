package parallel;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;


public class Test {

    @org.testng.annotations.Test
    public void testMethod(){
        SoftAssert softAssert = new SoftAssert();

//
        List<String> lst = new ArrayList<>();
        lst.add(0, "ayush");
        lst.add(1, "saxena");
        lst.set(0, "Alok");

        System.out.println(lst);
    }
}
