package es.codeurjc.ais.nitflex.smoke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String welcomeMessage = getWelcomeMessageFromApp(host);
        assertEquals("Welcome to my application", welcomeMessage);
    }

    private String getWelcomeMessageFromApp(String host) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(host);

        try {
            HttpResponse response = httpClient.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}