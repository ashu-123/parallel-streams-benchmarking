package org.performance.parallelstream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class M05_Sets {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Set<String> linesSet = new HashSet<>();
        try (Stream<String> lines = Files.lines(Path.of("files/words.txt"))) {
            linesSet.addAll(lines.collect(Collectors.toSet()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> linesList = new ArrayList<>(linesSet);

        ConcurrentHashMap<String, Long> threads = new ConcurrentHashMap<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool(8);

        Callable<Integer> task = () -> {
            System.out.println("Launching thread = " + Thread.currentThread().getName());
            return linesSet.stream()
                    .mapToInt(String::length)
                    .parallel()
                    .peek(i -> threads.merge(Thread.currentThread().getName(), 1L, Long::sum))
                    .sum();
        };

        ForkJoinTask<Integer> submit = forkJoinPool.submit(task);
        submit.get();

        threads.forEach((key, value) -> System.out.println(key + " -> " + value));

        forkJoinPool.shutdown();

        Set<Integer> hashes = linesList.stream()
                .map(line -> hash(line))
                .collect(Collectors.toSet());
        System.out.println("# hashes = " + hashes.size());
        hashes.forEach(System.out::println);
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
