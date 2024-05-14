package es.codeurjc.ais.nitflex.smoke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

class SmokeTest {

    @Test
    void testWelcomeMessage() {
        String host = System.getProperty("host");
        if (host == null || host.isEmpty()) {
            fail("La propiedad del sistema 'host' no está configurada. Asegúrate de pasar la URL de la aplicación correctamente.");
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String welcomeMessage = getWelcomeMessageFromApp(host);
        assertEquals("Welcome to my application", welcomeMessage, "El mensaje de bienvenida no coincide");
    }

    private String getWelcomeMessageFromApp(String host) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(host);

        try {
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                fail("La solicitud a la aplicación falló con el código de estado: " + statusCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            fail("Error al realizar la solicitud a la aplicación: " + e.getMessage());
            return null;
        }
    }
}
