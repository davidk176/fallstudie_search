package org.example.controller;

import org.example.model.SearchInput;
import org.example.search.ParallelSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @PostMapping("/search")
    public RedirectView searchList(@ModelAttribute("searchInput") SearchInput searchInput, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        var result = parallelSearchService.search(searchInput);
        final RedirectView redirectView = new RedirectView("/list/search", true);
        redirectAttributes
                .addFlashAttribute("searchedInput", searchInput)
                .addFlashAttribute("result", result);
        return redirectView;
    }
}
