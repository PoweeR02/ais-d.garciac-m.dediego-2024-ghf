package es.codeurjc.ais.nitflex.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GitAttributes {

    @Autowired
    private GitInfo gitInfo;

    @ModelAttribute("gitHash")
    public String gitHash() {
        return gitInfo.getGitHash();
    }

}
