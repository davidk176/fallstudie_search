package org.example.search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SearchWorker implements Callable<List<String>> {

    private String searchString;
    private List<String> wordList;

    public SearchWorker(String searchString, List<String> wordList) {
        this.searchString = searchString;
        this.wordList = wordList;
    }

    @Override
    public List<String> call() {
        List<String> results = new ArrayList<>();
        for (String word : wordList) {
            if (word.startsWith(searchString)) {
                results.add(word);
            }
        }
        return results;
    }
}
