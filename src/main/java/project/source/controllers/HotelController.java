package project.source.controllers;
/**
 * @author An Nguyen
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hotel")
public class HotelController {
    @GetMapping("/allHotel")
    public String getAllHotel(){
        return "All hotel";
    }
}
