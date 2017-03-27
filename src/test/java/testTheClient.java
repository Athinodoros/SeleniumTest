/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Athinodoros
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testTheClient {

    private static final int WAIT_MAX = 6;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {

        System.setProperty("webdriver.gecko.driver", "C:\\selenium\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    @Test
    public void domLoaded5RowsinTbody() {
        WebElement tableBody = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });
        Assert.assertThat(tableBody.findElements(By.tagName("tr")).size(), is(5));
    }

    @Test
    public void lookUp2002() throws Exception {
        WebElement element = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return driver.findElement(By.xpath("//*[@id=\"filter\"]"));
        });
        element.clear();
        element.sendKeys("2002");
        WebElement table = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(2));
    }

    @Test
    public void removeFilterTest() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys(Keys.BACK_SPACE);
        WebElement tableBody = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });
        List<WebElement> rows = tableBody.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(5));
    }

    @Test
    public void sortByYear() {
        WebElement sort = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return driver.findElement(By.xpath("//*[@id=\"h_year\"]"));
        });
        sort.click();
        WebElement table = driver.findElement(By.tagName("tbody"));
        List<WebElement> sortedRows = table.findElements(By.tagName("tr"));
        Assert.assertThat(sortedRows.get(0).findElements(By.tagName("td")).get(0).getText(), is("938"));
        Assert.assertThat(sortedRows.get(4).findElements(By.tagName("td")).get(0).getText(), is("940"));
    }

    @Test
    public void verifyAlterations() throws Exception {

        WebElement editButton = null;
        List<WebElement> rows = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        }).findElements(By.tagName("tr"));;
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                editButton = rows.get(i);
                break;
            }
        }
        editButton = editButton.findElements(By.tagName("td")).get(7).findElements(By.tagName("a")).get(0);
        //click the edit button
        editButton.click();
        //clear description field and type coolscars 
        WebElement element = driver.findElement(By.id("description"));
        element.clear();
        element.sendKeys("cool cars");
        driver.findElement(By.xpath("//*[@id=\"save\"]")).click();
        List<WebElement> updatedRows = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        }).findElements(By.tagName("tr"));;
        String newDescr = null;
        for (int i = 0; i < updatedRows.size(); i++) {
            if (updatedRows.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                newDescr = updatedRows.get(i).findElements(By.tagName("td")).get(5).getText();
                break;
            }
        }
        assertThat(newDescr, is("cool cars"));
    }

    @Test
    public void testFieldErrorPrompt() {
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("save")).click();

        String errorMessage = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<String>) (WebDriver d) -> {
            return d.findElement(By.xpath("//*[@id=\"submiterr\"]")).getText();
        });
        Assert.assertThat(errorMessage, is("All fields are required"));
    }

    @Test
    public void zCreateNewCarEntry() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<String>) (WebDriver d) -> {
            return d.findElement(By.xpath("//*[@id=\"new\"]")).getText();
        });
        //populate fields
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("year")).sendKeys("2008");
        driver.findElement(By.id("registered")).sendKeys("2002-5-5");
        driver.findElement(By.id("make")).sendKeys("Kia");
        driver.findElement(By.id("model")).sendKeys("Rio");
        driver.findElement(By.id("description")).sendKeys("As new");
        driver.findElement(By.id("price")).sendKeys("31000");
        //save
        driver.findElement(By.id("save")).click();
        List<WebElement> updatedRows = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));}).findElements(By.tagName("tr"));
        assertThat(updatedRows.size(), is(6));
        assertThat(updatedRows.get(5).findElements(By.tagName("td")).get(1).getText(), is("2008"));
        assertThat(updatedRows.get(5).findElements(By.tagName("td")).get(4).getText(), is("Rio"));

    }
    
}
