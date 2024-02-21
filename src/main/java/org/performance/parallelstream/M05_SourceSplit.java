package org.performance.parallelstream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class M05_SourceSplit {

    @Param("10000000")
    int N;

    Random random = new Random(314L);

    Set<String> lineSet;
    List<String> lineList;
    Set<Integer> intSet;
    List<Integer> intList;

    @Setup
    public void readLines() {
        try (Stream<String> lines = Files.lines(Path.of("files/words.txt"))) {
            this.lineSet = lines.collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lineList = new ArrayList<>(this.lineSet);
    }

    @Setup
    public void intsList() {
        intList = IntStream.range(0, N)
                .map(i -> random.nextInt())
                .boxed()
                .collect(Collectors.toList());
    }

    @Setup
    public void intsSet() {
        intSet = IntStream.range(0, N)
                .map(i -> random.nextInt())
                .boxed()
                .collect(Collectors.toSet());
    }

    @Benchmark
    public Object process_string_set() {
        return lineSet.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .sum();
    }

    @Benchmark
    public Object process_string_list() {
        return lineList.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .sum();
    }

    @Benchmark
    public Object process_string_list_parallel() {
        return lineList.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .parallel()
                .sum();
    }

    @Benchmark
    public Object process_string_set_parallel() {
        return lineSet.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .parallel()
                .sum();
    }

    @Benchmark
    public Object process_int_set() {
        return intSet.stream()
                .mapToInt(i -> i * 3)
                .sum();
    }

    @Benchmark
    public Object process_int_list() {
        return intList.stream()
                .mapToInt(i -> i * 3)
                .sum();
    }

    @Benchmark
    public Object process_int_set_parallel() {
        return intSet.stream()
                .mapToInt(i -> i * 3)
                .parallel()
                .sum();
    }

    @Benchmark
    public Object process_int_list_parallel() {
        return intList.stream()
                .mapToInt(i -> i * 3)
                .parallel()
                .sum();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(M05_SourceSplit.class.getName())
                .build();
        new Runner(opt).run();
    }
}
