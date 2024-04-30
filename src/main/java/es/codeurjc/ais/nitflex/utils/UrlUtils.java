package es.codeurjc.ais.nitflex.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UrlUtils {

    public void checkValidImageURL(String candidateURL){
        
        // CHECK THAT URL HAS VALID FORMAT
        URL url;
        try {
            url = new URL(candidateURL);
        } catch ( MalformedURLException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url format is not valid");
        }

        // CHECK THAT THE URL IS AN IMAGE
        if(!candidateURL.matches(".*\\.(jpg|jpeg|gif|png)")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url is not an image resource");  
        }

        // CHECK THAT RESOURCE EXIST
        int responseCode;
        try {
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            responseCode = huc.getResponseCode();
        } catch ( IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem at checking URL: "+candidateURL);  
        }

        if(responseCode != HttpURLConnection.HTTP_OK) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url resource does not exists");
    }

}
