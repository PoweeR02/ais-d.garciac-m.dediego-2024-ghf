package es.codeurjc.ais.nitflex.e2e.selenium;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import es.codeurjc.ais.nitflex.Application;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmUITest {

    @LocalServerPort
    int port;

    protected static Map<String, List<String>> OperativeSystemToBrowser;
    protected WebDriver driver;
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
    public void setup() {
        this.driver = getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void teardown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Test
    @DisplayName("Añadir una nueva película y comprobar que se ha creado")
    public void createFilmTest() throws Exception {

        // GIVEN: Partiendo de que estamos en la página principal de la web
        this.driver.get("http://localhost:" + this.port + "/");

        // WHEN: Creamos un nueva película

        String title = "Spider-Man: No Way Home";
        String synopsis = "Peter Parker es desenmascarado y por tanto no es capaz de separar su vida normal de los enormes riesgos que conlleva ser un súper héroe.";
        String url = "https://www.themoviedb.org/t/p/w220_and_h330_face/osYbtvqjMUhEXgkuFJOsRYVpq6N.jpg";
        String year = "2021";

        // Hacemos click en "New film"
        driver.findElement(By.xpath("//*[text()='New film']")).click();
        // Rellenamos el formulario
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("url")).sendKeys(url);
        driver.findElement(By.name("releaseYear")).sendKeys(year);
        driver.findElement(By.name("synopsis")).sendKeys(synopsis);
        // Enviamos el formulario
        driver.findElement(By.id("Save")).click();

        // THEN: Esperamos que la película creada aparezca en la nueva página resultante

        this.wait.until(ExpectedConditions.textToBe(By.id("film-title"), title));
    }

}