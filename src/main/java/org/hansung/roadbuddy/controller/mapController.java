package org.hansung.roadbuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class mapController {
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}
