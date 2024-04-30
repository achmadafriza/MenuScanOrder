package net.uqcloud.infs7202.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order/{uuid}")
public class OrderController {
    /* TODO: update page */
    @GetMapping
    public String orderForm(@PathVariable("uuid") String uuid) {
        return "user-order";
    }

    /* TODO: update page */
    @PostMapping
    public String order(@PathVariable("uuid") String uuid) {
        return "user-order";
    }
}
