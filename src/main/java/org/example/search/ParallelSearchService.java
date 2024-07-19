package org.example.search;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.model.SearchInput;
import org.example.test.ParallelSearch;
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

    public List<String> search(SearchInput searchInput) throws ExecutionException, InterruptedException {
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

        var hits = new ArrayList<String>();
        for (Future<List<String>> future : futures) {
            hits.addAll(future.get());
        }
        return hits;
    }

    public List<String> searchLive(SearchInput searchInput) throws ExecutionException, InterruptedException {
        return List.of("foo", "bar");
    }

    private List<String> readWordList(String filename) {
        var is = ParallelSearch.class.getClassLoader().getResourceAsStream(filename);
        var wordList = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        Collections.shuffle(wordList);
        return wordList;
    }


}
