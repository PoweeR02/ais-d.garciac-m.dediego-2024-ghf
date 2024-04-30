package es.codeurjc.ais.nitflex.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GitInfo {

    private Properties properties;

    Logger logger = LoggerFactory.getLogger(GitAttributes.class);

    public GitInfo() {

        this.properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));
            logger.info("Commit hash: " + properties.getProperty("git.commit.id"));
        } catch (Exception e) {
            logger.error("Error loading git properties: " + e.getMessage());
        }

    }

    public String getGitHash() {
        return properties.getProperty("git.commit.id", "Unknown");
    }
    
}
