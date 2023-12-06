package com.blueprintto3d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/img")
    public String imgPage() {
        return "img";
    }

    @GetMapping("/gltf")
    public String gltfPage() {
        return "gltf";
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }
}
