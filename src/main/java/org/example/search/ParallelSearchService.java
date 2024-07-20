package org.example.search;

import org.example.model.SearchInput;
import org.example.model.SearchResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class ParallelSearchService {

    private final int numberOfThreads;

    private final ExecutorService executor;

    private final List<String> wordList;

    public ParallelSearchService() {
        this.numberOfThreads = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
        this.wordList = readWordList("words.txt");
    }

    public SearchResult search(SearchInput searchInput) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        int chunkSize = (int) Math.ceil((double) wordList.size() / numberOfThreads); //round up chunk size
        List<Future<List<String>>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = startIndex + chunkSize;
            //detect end of list
            if (endIndex > wordList.size()) {
                endIndex = wordList.size();
            }
            var sublist = wordList.subList(startIndex, endIndex);
            var worker = new SearchWorker(searchInput.getSearchString(), sublist);
            futures.add(executor.submit(worker));
        }

        var result = new ArrayList<String>();
        for (Future<List<String>> future : futures) {
            result.addAll(future.get());
        }
        return SearchResult.builder()
                .result(result)
                .successful(true)
                .took(System.currentTimeMillis() - start)
                .hits(result.size())
                .build();
    }

    private List<String> readWordList(String filename) {
        var is = this.getClass().getClassLoader().getResourceAsStream(filename);
        var wordList = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        Collections.shuffle(wordList);
        return wordList;
    }
}
