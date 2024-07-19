package org.example.controller;

import org.example.model.SearchInput;
import org.example.search.ParallelSearchService;
import org.example.test.ParallelSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/list")
public class SearchController {

    @Autowired
    ParallelSearchService parallelSearchService;

    @GetMapping("/search")
    public String addSearchView(Model model) {
        model.addAttribute("searchInput", new SearchInput());
        return "search-list";
    }

    @GetMapping("/live")
    public String addLiveSearchView(Model model) {
        model.addAttribute("searchInput", new SearchInput());
        return "search-list-live";
    }

    @PostMapping("/search")
    public RedirectView searchList(@ModelAttribute("searchInput") SearchInput searchInput, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        var result = parallelSearchService.search(searchInput);
        final RedirectView redirectView = new RedirectView("/list/search", true);
        redirectAttributes
                .addFlashAttribute("searchListSuccess", true)
                .addFlashAttribute("searchedInput", new SearchInput())
                .addFlashAttribute("result", result);
        return redirectView;
    }

    @PostMapping("/live")
    public RedirectView searchListLive(@ModelAttribute("searchInput") SearchInput searchInput, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        var result = parallelSearchService.searchLive(searchInput);
        final RedirectView redirectView = new RedirectView("/search/live", true);
        redirectAttributes
                .addFlashAttribute("searchListSuccess", true)
                .addFlashAttribute("searchedInput", new SearchInput())
                .addFlashAttribute("result", result);
        return redirectView;
    }

    @PostMapping("/live/liveajax")
    public List<String> runLiveSearchCall(@RequestBody String searchString) {
        return List.of("foo", "bar");
    }

    @PostMapping("/liveajax")
    public List<String> runLiveSearchCall2(@RequestBody String searchString) {
        return List.of("foo");
    }

}
