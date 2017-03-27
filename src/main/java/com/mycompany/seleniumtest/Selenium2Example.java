/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.seleniumtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Selenium2Example {
    
    public static void main(String[] args) throws InterruptedException {
        
        System.setProperty("webdriver.gecko.driver", "C:\\selenium\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe");

        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        WebDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("localhost:3000");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");
//        (new WebDriverWait(driver, 5).until(isTrue))
        // Find the text input element by its name
//        WebElement element = driver.findElement(By.tagName("tbody tr"));

        WebElement element = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return driver.findElement(By.tagName("tbody"));
            }
        });
        // Enter something to search for
        System.out.println(element.findElements(By.tagName("tr")).size());

        // Now submit the form. WebDriver will find the form for us from the element
        //element.submit();
        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
//        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//            public Boolean apply(WebDriver d) {
//                return d.getTitle().toLowerCase().startsWith("cheese!");
//            }
//        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());

        //Close the browser
        driver.quit();
    }
}
