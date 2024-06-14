package com.maeumteo.maeumteobackend.main;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

//    @GetMapping("/")
//    public List<> menu() {
//        return mainService.menu();
//    }
}
