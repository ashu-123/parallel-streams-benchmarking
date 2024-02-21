package org.performance.parallelstream;

import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main_01 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Set<String> threadNames = ConcurrentHashMap.newKeySet();
        ConcurrentHashMap<String, Long> threads = new ConcurrentHashMap<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool(8);

        Callable<Integer> task = () -> IntStream.range(0, 100000)
                .map(i -> i * 3)
                .parallel()
                .peek(i -> threads.merge(Thread.currentThread().getName(), 1L, Long::sum))
                .sum();

//        int sum = forkJoinPool.submit(task).get();

        int sum = IntStream.range(0, 100000)
                .map(i -> i * 3)
                .parallel()
                .peek(i -> threads.merge(Thread.currentThread().getName(), 1L, Long::sum))
                .sum();
        System.out.println("sum = " + sum);
        // threadNames.forEach(System.out::println);
        threads.forEach((key, value) -> System.out.println(key + " -> " + value));

        forkJoinPool.shutdown();
    }

}
