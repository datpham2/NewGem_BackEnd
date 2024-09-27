package project.source.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @Operation(
            method = "GET",
            summary = "Get the home page UI",
            description = "Send a request to get the home page :D")
    @GetMapping("/home")
    public String homePage(){
        return "Home Page";
    }
}
