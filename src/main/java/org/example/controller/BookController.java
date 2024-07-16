package org.example.controller;

import org.example.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {


    @GetMapping("/viewBooks")
    public String viewBooks(Model model) {
        var books = List.of(new Book("a", "b", "c"));
        model.addAttribute("books", books);
        return "view-books";
    }

    @GetMapping("/addBook")
    public String addBookView(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/addBook")
    public RedirectView addBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        final RedirectView redirectView = new RedirectView("/book/addBook", true);
        redirectAttributes.addFlashAttribute("savedBook", new Book("a", "b", "c"));
        redirectAttributes.addFlashAttribute("addBookSuccess", true);
        return redirectView;
    }
}
