package es.codeurjc.ais.nitflex.e2e.selenium;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import es.codeurjc.ais.nitflex.Application;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SeleniumBugTest {

    @LocalServerPort
    int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        baseUrl = "http://localhost:" + port + "/";
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
