package org.example.controller;

import org.example.model.Book;
import org.example.model.SearchInput;
import org.example.test.ParallelSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SearchController {



    @GetMapping("/list")
    public String addSearchView(Model model) {
        model.addAttribute("searchInput", new SearchInput());
        return "search-list";
    }

    @PostMapping("/list")
    public RedirectView searchList(@ModelAttribute("book") SearchInput searchInput, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        var is = ParallelSearch.class.getClassLoader().getResourceAsStream("words.txt");
        var wordList = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        var result = ParallelSearch.parallelSearch(wordList, searchInput.getSearchString(), 4);
        final RedirectView redirectView = new RedirectView("/search/list", true);
        redirectAttributes.addFlashAttribute("searchListSuccess", true);
        redirectAttributes.addFlashAttribute("searchedInput", new SearchInput());
        redirectAttributes.addFlashAttribute("result", result);
        return redirectView;
    }
}
