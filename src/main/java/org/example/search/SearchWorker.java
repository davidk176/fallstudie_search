package org.example.search;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class SearchWorker implements Callable<List<String>> {

    private String searchString;
    private List<String> wordList;

    public SearchWorker(String searchString, List<String> wordList) {
        this.searchString = searchString;
        this.wordList = wordList;
    }

    @Override
    public List<String> call() {
        log.info("Starting SearchWorker {}", hashCode());
        List<String> results = new ArrayList<>();
        for (String string : wordList) {
            if (string.toLowerCase().startsWith(searchString.toLowerCase())) {
                results.add(string);
            }
        }
        log.info("Finished SearchWorker {}", hashCode());
        return results;
    }
}
