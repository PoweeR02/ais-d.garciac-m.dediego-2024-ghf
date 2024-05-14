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
        checkHostParameter(host);

        WebDriver driver = setupWebDriver();
        try {
            driver.get(host);
            String welcomeMessage = driver.findElement(By.id("create-film")).getText();
            assertEquals("New film", welcomeMessage);
        } finally {
            driver.quit();
        }
    }

    private void checkHostParameter(String host) {
        if (host == null || host.isEmpty()) {
            fail("Se requiere el parámetro 'host' para ejecutar la prueba. Por favor, asegúrese de proporcionar el host donde está desplegada la aplicación.");
        }
    }

    private WebDriver setupWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }
}
