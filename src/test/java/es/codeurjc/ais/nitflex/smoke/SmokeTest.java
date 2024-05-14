package es.codeurjc.ais.nitflex.smoke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class SmokeTest {
    @Test
    public void verifyApplicationDeployment() {
        String host = System.getProperty("host");
        if (host == null || host.isEmpty()) {
            fail("El parametro 'host' no ha sido especificado. Por favor, proporcione el host donde esta desplegada la aplicacion");
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);
        driver.get("http://ais-anuncios.westeurope.azurecontainer.io:8080/");
        String welcomeMessage = driver.findElement(By.id("create-film")).getText();
        driver.quit();
        assertEquals("New film", welcomeMessage);
    }
}
