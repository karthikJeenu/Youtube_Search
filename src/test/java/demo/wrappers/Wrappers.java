package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Wrappers {

    public static void clickOnBtn(WebDriver driver, WebElement button) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    public static void clickuntilbuttonisinvisible(WebDriver driver, WebElement button) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            while (button.isDisplayed()) {
                button.click();
                wait.until(ExpectedConditions.visibilityOf(button));
            }
        } catch (Exception e) {
            System.out.println("Button is no longer visible.");
        }
    }
     public static void scrollToViewport(WebDriver driver, By locator) {
        try {

            // Set up a WebDriverWait to wait for the element to become visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // JavaScript executor to scroll the viewport to the element
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView();", element);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void clickAW(WebDriver driver, By locator) {
        try {
         //   YouTubeUtils.logStatus("clickAW", "Clicking");

            // Setting up WebDriverWait with a timeout of 20 seconds
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Waiting for the element to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // Locating the web element
            WebElement element = driver.findElement(locator);

            // Clicking the element
            element.click();
        } catch (Exception e) {
          // YouTubeUtils.logStatus("clickAW", "Exception while clicking\n" + e.getMessage());
          System.out.println(e.getMessage());
        }
    }

      public static List<WebElement> getElements(WebDriver driver, By firstNNewsLocator) {
        List<WebElement> elements = new ArrayList<>();
        try {
          //  logStatus("getElements", "Getting elements");

            // Find all elements matching the locator
            elements = driver.findElements(firstNNewsLocator);
        } catch (Exception e) {
           // logStatus("getElements", "Exception\n\t\t\t" + e.getMessage());
           System.out.println(e.getMessage());
        }
        return elements;
    }

    public static void getBodyAndViewCount(WebDriver driver, By firstNNewsLocator) {
        int totalLikes = 0;
        try {
           // logStatus("getBodyAndViewCount", "Getting body and view count");

            // Wait for the first news post to become visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstNNewsLocator));

            // Retrieve all news posts matching the provided locator
            List<WebElement> newsPosts = Wrappers.getElements(driver, firstNNewsLocator);

            // Iterate through each news post
            for (WebElement elementBody : newsPosts) {
                int likes = 0;
                try {
                    // Retrieve the number of likes for the current news post
                    WebElement numberOfLikes = elementBody.findElement(By.xpath(".//span[contains(@id, 'vote-count-middle')]"));
                    likes = Integer.parseInt(numberOfLikes.getText());
                } catch (Exception e) {
                    // If likes count is not available or cannot be parsed, set likes to 0
                    likes = 0;
                }

                // Retrieve the body content of the current news post
                WebElement bodyElement = elementBody.findElement(By.xpath(".//div[contains(@id, 'body')]"));
                totalLikes += likes;

                System.out.println("Body:"+bodyElement.getText());
                System.out.println("Likes:"+likes);
               
            }
        } catch (Exception e) {
    
          System.out.println(e.getMessage());
        }
        System.out.println("Total likes: " + totalLikes);
    }

      public static void sendKeysAW(WebDriver driver, By locator, String text) {
        try {
           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Waiting for the element to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // Locating the web element
            WebElement element = driver.findElement(locator);

            // Clearing any existing text in the element
            element.clear();

            // Sending the specified text to the element
            element.sendKeys(text);

            // Sending ENTER key press after sending the text
            element.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void scrollTillVideoCountReaches(WebDriver driver, long totalCount) {
        try {
            
            int index = 1;

            // Continue scrolling until the total count reaches 0 or below
            while (totalCount >= 0) {
                // Wait for the video view element to become visible
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                WebElement videoViews = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//ytd-video-renderer//span[contains(text(), 'views')])["+ index +"]")));

                // Scroll the viewport to the video view element
                Wrappers.scrollToViewport(driver, By.xpath("(//ytd-video-renderer//span[contains(text(), 'views')])["+ index +"]"));

                // Subtract the views count of the current video from the total count
                totalCount -= parseViewsCount(videoViews.getText());

                // Increment the index for the next video
                index++;
                System.out.println(videoViews.getText());
                System.out.println(parseViewsCount(videoViews.getText()));
                System.out.println( (totalCount <= 0) ? "Count reached" : totalCount);
      
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static long parseViewsCount(String viewsText) {
        // Remove the " views" suffix from the views text
        viewsText = viewsText.replace(" views", "");
        long viewsCount;
        if (viewsText.endsWith("K")) {
            // Convert the abbreviated count with "K" suffix into a long integer
            viewsCount = (long) (Double.parseDouble(viewsText.substring(0, viewsText.length() - 1)) * 1_000);
        } else if (viewsText.endsWith("M")) {
            // Convert the abbreviated count with "M" suffix into a long integer
            viewsCount = (long) (Double.parseDouble(viewsText.substring(0, viewsText.length() - 1)) * 10_00_000);
        } else {
            // Convert the count without any suffix into a long integer
            viewsCount = Long.parseLong(viewsText);
        }
        return viewsCount;
    }

}
