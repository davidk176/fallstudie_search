package org.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/live")
public class LiveSearchController {

    @PostMapping("/search")
    public List<String> runLiveSearchCall2(@RequestBody String searchString) {
        return List.of("foo");
    }
}
