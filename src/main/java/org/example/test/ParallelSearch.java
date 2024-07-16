package org.example.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ParallelSearch {

    public static class SearchWorker implements Callable<List<String>> {
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

    public static List<String> parallelSearch(List<String> wordList, String searchString, int numThreads) throws InterruptedException, ExecutionException {
        int chunkSize = wordList.size() / numThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<List<String>>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numThreads - 1) ? wordList.size() : (i + 1) * chunkSize;
            List<String> sublist = wordList.subList(startIndex, endIndex);
            SearchWorker worker = new SearchWorker(searchString, sublist);
            futures.add(executor.submit(worker));
        }

        List<String> results = new ArrayList<>();
        for (Future<List<String>> future : futures) {
            results.addAll(future.get());
        }

        executor.shutdown();
        return results;
    }

    public static void liveSearch(List<String> wordList, int numThreads) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);
        String currentString = "";

        while (true) {
            System.out.println("Current search string: " + currentString);
            System.out.print("Enter next character or 'exit' to stop: ");
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            }

            currentString += input;
            List<String> results = parallelSearch(wordList, currentString, numThreads);
            System.out.println("Results for '" + currentString + "': " + results);
        }

        scanner.close();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

//        List<String> wordList = List.of("haus", "apple", "banana", "apricot", "cherry", "blueberry", "avocado", "apfel", "orange", "hund", "katze", "maus", "eule", "tiger", "elefant", "koala", "ente", "maus", "hund", "wasser", "apfelsaft", "sprudel");
        var is = ParallelSearch.class.getClassLoader().getResourceAsStream("wordlist-german.txt");
        var wordList = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
//        for (int i = 0; i < 9; i++) {
//            wordList.addAll(wordList);
//        }
        Collections.shuffle(wordList);
//        BufferedWriter br = new BufferedWriter(new FileWriter("new-file.txt"));
//        wordList.forEach(it -> {
//            try {
//                br.write(it + System.lineSeparator());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        br.close();
        long start = System.currentTimeMillis();
        int numThreads = Runtime.getRuntime().availableProcessors();
//        numThreads = 1;

        System.out.println("Initial search with 'maus':");
        List<String> results = parallelSearch(wordList, "maus", numThreads);
        System.out.println(results);

//        System.out.println("\nLive Search:");
//        liveSearch(wordList, numThreads);
        System.out.println(System.currentTimeMillis() - start);
    }
}
