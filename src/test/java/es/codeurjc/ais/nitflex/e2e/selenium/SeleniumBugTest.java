package es.codeurjc.ais.nitflex.e2e.selenium;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import es.codeurjc.ais.nitflex.Application;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SeleniumBugTest {

    @LocalServerPort
    int port;

    public static Map<String, List<String>> OperativeSystemToBrowser;
    private WebDriver driver;
    private String baseUrl;
    protected WebDriverWait wait;

    private WebDriver getDriver() {
        WebDriver driver = null;
        String browser = System.getProperty("browser");
        String os = System.getProperty("os");
        if (OperativeSystemToBrowser.get(os).contains(browser)) {
            switch (browser) {
                case "safari":
                    driver = new SafariDriver();
                    break;
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    EdgeOptions options = new EdgeOptions();
                    options.addArguments("--headless");
                    driver = new EdgeDriver(options);
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    driver = new FirefoxDriver(firefoxOptions);
            }
        }

        return driver;
    }

    @BeforeAll
    public static void initialiceMap() {
        OperativeSystemToBrowser = new HashMap<>();
        OperativeSystemToBrowser.put("ubuntu-latest", new ArrayList<>(Arrays.asList("chrome", "firefox")));
        OperativeSystemToBrowser.put("windows-latest", new ArrayList<>(Arrays.asList("edge", "chrome", "firefox")));
        OperativeSystemToBrowser.put("macos-latest", new ArrayList<>(Arrays.asList("safari", "chrome", "firefox")));
    }

    @BeforeEach
    public void setupTest() {
        driver = getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void bugDetectionTest() throws InterruptedException {
        // Given
        String filmName = "In time";
        String filmSynopsis = "El hallazgo de una formula contra el envejecimiento trae consigo no solo superpoblacion, sino tambien la transformacion del tiempo en moneda de cambio que permite sufragar tanto lujos como necesidades. En este contexto, Will, un obrero que consigue casualmente una inmensa cantidad de tiempo, debe huir de unos policias corruptos, con una rehen adinerada.";
        driver.get(baseUrl);
        driver.findElement(By.id("create-film")).click();
        WebElement titleInput = driver.findElement(By.name("title"));
        titleInput.sendKeys(filmName);
        WebElement yearInput = driver.findElement(By.name("releaseYear"));
        yearInput.sendKeys("2011");
        WebElement urlInput = driver.findElement(By.name("url"));
        urlInput.sendKeys("https://pics.filmaffinity.com/In_Time-139117348-large.jpg");
        WebElement synopsisInput = driver.findElement(By.name("synopsis"));
        synopsisInput.sendKeys(filmSynopsis);
        driver.findElement(By.id("Save")).submit();

        // When
        driver.findElement(By.id("edit-film")).click();
        driver.findElement(By.id("Cancel")).click();

        // Then
        assertThat(driver.findElement(By.id("film-title")).getText()).isEqualTo(filmName);
        assertThat(driver.findElement(By.id("film-synopsis")).getText()).isEqualTo(filmSynopsis);
    }
}
