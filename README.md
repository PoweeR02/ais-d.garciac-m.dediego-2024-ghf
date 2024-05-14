# Practica 3: Definición de Workflow

Nombre de los alumnos: David García Cava y Mireya De Diego Gordo

[Repositorio de GitHub](https://github.com/PoweeR02/ais-d.garciac-m.dediego-2024-ghf)

[Aplicación Azure](http://ais-anuncios.westeurope.azurecontainer.io:8080/)


### __Workflow 1__

*Por cada commit en una rama que no sea main, se ejecutarán las pruebas unitarias y de integración.*

```java
name: CI Workflow 1
on:
  push:
    branches-ignore:
      -main
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
          
      - name: Run Unitary Test
        run: mvn test -P unitary

      - name: Run Integration Test
        run: mvn test -P integration
```
**Captura de que el Workflow 1 funciona correctamente**

![Workflow1](Capturas\workflow1ok.jpeg "Workflow1")

![Workflow1](Capturas\workflow1contenido.jpeg "Workflow1")

### __Workflow 2__
*Cada vez que se termine una feature y antes de integrarse en la rama
 main, se ejecutarán todas las pruebas: unitarias, de integración y de sistema*
 ```java
name: CI Workflow 2

on:
  pull_request:
    branches:
      - main

jobs:
  test:
    runs-on: ubuntu-latest 
    steps:        
      - name: Checkout code         
        uses: actions/checkout@v4
            
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Run Unitary Tests
        run: mvn test -P unitary
        
      - name: Run Integration Tests
        run: mvn test -P integration
        
      - name: Run Selenium Tests
        run: mvn test -P selenium
```
**Captura de que el Workflow 2 funciona correctamente**

![Workflow2](Capturas\workflow2ok.jpeg "Workflow2")

![Workflow2](Capturas\workflow2contenido.jpeg "Workflow2")

### __Workflow 3__
*Al integrar con la rama de producción (main): Se publicará una versión de la aplicación como una imagen Docker en Docker Hub, cuya versión sea el hash del commit, Se desplegará esta versión en Azure. Se ejecutará un smoke test sobre la aplicación desplegada*
```java
name: CI Workflow 3

on:
  push:

permissions:
  id-token: write
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Generate Docker image
        run: mvn spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=${{ secrets.DOCKERHUB_USERNAME }}/anuncios:${{ github.sha }}
```

**Implementación del SmokeTest**
```java
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
```
**Captura de que el Workflow 3 funciona correctamente**

![Workflow3](Capturas\workflow3ok.jpeg "Workflow3")

![Workflow3](Capturas\workflow3contenido.jpeg "Workflow3")

**Captura de la Aplicación desplegada en Azure con la URL**

![Navegador Azure](Capturas\Azure.png "Azure")


### __Workflow 4__
*Cada noche, en la rama de producción (main): Se ejecutará como mínimo una de las pruebas de sistema sobre distintos navegadores y sistemas operativos: Chrome/Firefox (Linux/Windows/MacOS), Edge (Windows) y Safari (MacOS)*


```java
name: CI Workflow 4

on:
  schedule:
    - cron: "0 0 * * *" 

jobs:
  system-tests:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        browser: [ chrome, firefox, edge, safari ]
        os: [ ubuntu-latest, windows-latest, macos-latest ]
        exclude:
          - browser: edge
            os: ubuntu-latest
            
          - browser: safari
            os: ubuntu-latest

          - browser: safari
            os: windows-latest

          - browser: edge
            os: macos-latest
          

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Run system tests
        run: mvn -Dbrowser=${{ matrix.browser }} -Dos=${{ matrix.os }} test -P selenium
```

**Captura de que el workflow 4 funciona correctamente**

![Workflow4](Capturas\workflow4ok.jpeg "Workflow4")

![Workflow4](Capturas\workflow4contenido.jpeg "Workflow4")