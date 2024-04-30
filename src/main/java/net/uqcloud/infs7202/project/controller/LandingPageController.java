package net.uqcloud.infs7202.project.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPageController {
    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public String home() {
        return "landing-page";
    }
}
