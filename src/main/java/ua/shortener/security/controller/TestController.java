package ua.shortener.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/all-links")
    public String allLinks(){
        return "/hello";
    }


}
