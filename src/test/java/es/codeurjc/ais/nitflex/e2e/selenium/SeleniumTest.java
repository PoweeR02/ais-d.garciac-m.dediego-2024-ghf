package es.codeurjc.ais.nitflex.e2e.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class SeleniumTest {

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
    void newMovieCreationTest() throws InterruptedException {
        // Given
        String filmName = "Oppenheimer";
        String filmSynopsis = "Durante la Segunda Guerra Mundial, el teniente general Leslie Groves designo al fisico J. Robert Oppenheimer para un grupo de trabajo que esta desarrollando el Proyecto Manhattan, cuyo objetivo consiste en fabricar la primera bomba atomica.";
        driver.get(baseUrl);

        // When
        driver.findElement(By.id("create-film")).click();
        WebElement titleInput = driver.findElement(By.name("title"));
        titleInput.sendKeys(filmName);
        WebElement yearInput = driver.findElement(By.name("releaseYear"));
        yearInput.sendKeys("2023");
        WebElement urlInput = driver.findElement(By.name("url"));
        urlInput.sendKeys("https://pics.filmaffinity.com/Oppenheimer-828933592-large.jpg");
        WebElement synopsisInput = driver.findElement(By.name("synopsis"));
        synopsisInput.sendKeys(filmSynopsis);
        driver.findElement(By.id("Save")).submit();

        // Then
        assertThat(driver.findElement(By.id("film-title")).getText()).isEqualTo(filmName);
        assertThat(driver.findElement(By.id("film-synopsis")).getText()).isEqualTo(filmSynopsis);
    }

    @Test
    void movieDeletionConfirmationTest() throws InterruptedException {
        // Given
        String filmName = "La sociedad de la nieve";
        driver.get(baseUrl);
        driver.findElement(By.id("create-film")).click();
        WebElement titleInput = driver.findElement(By.name("title"));
        titleInput.sendKeys(filmName);
        WebElement yearInput = driver.findElement(By.name("releaseYear"));
        yearInput.sendKeys("2023");
        WebElement urlInput = driver.findElement(By.name("url"));
        urlInput.sendKeys("https://pics.filmaffinity.com/La_sociedad_de_la_nieve-826551508-large.jpg");
        WebElement synopsisInput = driver.findElement(By.name("synopsis"));
        synopsisInput.sendKeys(
                "Un avion uruguayo en el que viajan los jugadores del equipo de rugby del Old Christians Club de Montevideo se estrella en la cordillera de los Andes y los supervivientes deben sobreponerse a las condiciones extremas para mantenerse vivos.");
        driver.findElement(By.id("Save")).submit();

        // When
        driver.findElement(By.id("remove-film")).click();
        String messageCorrectRemove = driver.findElement(By.id("positiveMessage")).getText();
        driver.findElement(By.id("all-films")).click();

        // Then
        assertThat(messageCorrectRemove).isEqualTo("Film '" + filmName + "' deleted");
        // Asumimos que NO puede haber dos peliculas con el mismo titulo
        assertTrue(driver.findElements(By.linkText(filmName)).isEmpty());
    }
}