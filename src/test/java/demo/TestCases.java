package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.logging.Level;

import demo.utils.ExcelDP;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDP { // Lets us read the data
        ChromeDriver driver;
        WebDriverWait wait;

        @Test(priority = 1, enabled = true)
        public void testCase01() {
                System.out.println("Testcase01 started");
                Boolean URL = driver.getCurrentUrl().contains("youtube");
                Assert.assertTrue(URL, "Youtube link is not correctly opened");
                WebElement about = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='About']")));
                Wrappers.clickOnBtn(driver, about);
                String text = driver.findElement(By.xpath("//section[@class='ytabout__content']/p")).getText();
                System.out.println(text);
                System.out.println("Clicked on the about button");
                System.out.println("Testcase01 ended");
        }

        @Test(priority = 2, enabled = true)
        public void testCase02() {
                System.out.println("Testcase02 started");
              
                WebElement Films = wait.until(ExpectedConditions
                                .elementToBeClickable(By.xpath("//yt-formatted-string[text()='Movies']")));
                Wrappers.clickOnBtn(driver, Films);

                WebElement RightArrow = wait.until(
                                ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Next']")));
                Wrappers.clickuntilbuttonisinvisible(driver, RightArrow);
                String Comedy = driver
                                .findElement(By.xpath(
                                                "//ytd-grid-movie-renderer[contains(@class,'style-scope')][16]/a/span"))
                                .getText();
                System.out.println("Extracted Movie Text: '" + Comedy + "'"); // Debugging Output

                SoftAssert softAssert = new SoftAssert();
                softAssert.assertEquals(Comedy, "Comedy");

                String certificate = driver
                                .findElement(By.xpath(
                                                "//ytd-grid-movie-renderer[contains(@class,'style-scope')][16]//ytd-badge-supported-renderer/div[2]/p"))
                                .getText();
                softAssert.assertEquals(certificate, "A");
                System.out.println("Extracted Movie Text: '" + certificate + "'"); // Debugging Output
                System.out.println("Testcase02 ended");
        }

        @Test(priority = 3, enabled = true)
        public void testCase03() throws InterruptedException {
                System.out.println("Testcase03 started");
                WebElement Music = wait.until(ExpectedConditions
                                .elementToBeClickable(By.xpath("//yt-formatted-string[text()='Music']")));
                Wrappers.clickOnBtn(driver, Music);
                Thread.sleep(3000);
                WebElement showmore = driver.findElement(
                                By.xpath("(//span[contains(text(),'Biggest Hits')]/ancestor::div[@id='dismissible']//button)[1]"));
                Wrappers.clickOnBtn(driver, showmore);


                String lastplaylist=driver.findElement(By.xpath("//span[text()='Bollywood Dance Hitlist']")).getText();
                System.out.println(lastplaylist);
                WebElement songs=driver.findElement(By.xpath("//span[text()='Bollywood Dance Hitlist']/../../../../../../a/yt-collection-thumbnail-view-model/yt-collections-stack/div/div[3]/yt-thumbnail-view-model/yt-thumbnail-overlay-badge-view-model/yt-thumbnail-badge-view-model/badge-shape/div[2]"));

                String text= songs.getText().replaceAll("[^0-9]", "");
                int number=Integer.parseInt(text);
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(number<=50,"Number is not less or equal to 50");
                System.out.println("Testcase03 ended");
        }


        @Test(priority = 4, enabled = true)
    public void testCase04() {
        System.out.println("Testcase04 started");
        By newsTab = By.xpath("//a[contains(@title, 'News')]");
        Wrappers.scrollToViewport(driver, newsTab);
        Wrappers.clickAW(driver, newsTab);

        // Locate the "Latest News Posts" section
        By latestNewsPosts = By.xpath("//span[contains(text(), 'Latest news post')]");
        Wrappers.scrollToViewport(driver, latestNewsPosts);

        // Retrieve and print the body and the number of likes for each of the first 3 news posts
        int numberOfNewsPosts = 3;
        By firstNNewsLocator = By.xpath("(//span[contains(text(), 'Latest news post')]/ancestor::ytd-rich-section-renderer//ytd-post-renderer)[position() <= "+ numberOfNewsPosts +"]");
        Wrappers.getBodyAndViewCount(driver, firstNNewsLocator);
        System.out.println("Testcase04 ended");
    }


    @Test(priority = 5, enabled = true, description = "Verify video views count", dataProvider = "searchTerms", dataProviderClass = ExcelDP.class)
    public void TestCase05(String searchTerms) {
        System.out.println("Testcase05 started");
        // Search for the item
        By searchBox = By.xpath("//div[@id='center']//div/form/input");
        Wrappers.sendKeysAW(driver, searchBox, searchTerms);

        // Scroll through the search results until the total views for the videos reach 10 crore
        long totalCount = 10_00_00_000;
        Wrappers.scrollTillVideoCountReaches(driver, totalCount);
        System.out.println("Testcase05 ended");
    }


        @BeforeMethod
    public void driverGet() {
        // Navigate to https://www.youtube.com
        driver.get("https://www.youtube.com");
    }

        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);

                // Fix WebSocket issue
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);
                wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                driver.manage().window().maximize();
        }

        @AfterTest
        public void endTest() {
                if (driver != null) {
                        driver.quit();
                }
        }
}
