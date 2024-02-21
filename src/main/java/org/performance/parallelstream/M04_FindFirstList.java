package org.performance.parallelstream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class M04_FindFirstList {

    public static final int MAX_RANDOM = 100_0000_000;
    private Random random = new Random();

    @Param({"1000000"})
    private int N100;
    private int limit;
    private List<Integer> intsN100;
    private List<Integer> intsN150;

    @Setup
    public void setup() {
        intsN100 = IntStream.range(0, N100)
                .mapToObj(index -> random.nextInt(MAX_RANDOM))
                .collect(Collectors.toList());
        intsN100.add(200_0000_000);

        List<Integer> intsN50 = IntStream.range(0, N100 /2)
                .mapToObj(index -> random.nextInt(MAX_RANDOM))
                .collect(Collectors.toList());

        intsN150 = new ArrayList<>();
        intsN150.addAll(intsN100);
        intsN150.add(200_0000_000);
        intsN150.addAll(intsN50);
        limit = intsN150.size() + 1;
    }

    @Benchmark
    public double find_first_100_no_parallel() {
        return intsN100.stream().filter(i -> i > MAX_RANDOM).findFirst().get();
    }

    @Benchmark
    public double find_any_100_no_parallel() {
        return intsN100.stream().filter(i -> i > MAX_RANDOM).findAny().get();
    }

    @Benchmark
    public double find_first_100_parallel() {
        return intsN100.stream().filter(i -> i > MAX_RANDOM).parallel().findFirst().get();
    }

    @Benchmark
    public double find_any_100_parallel() {
        return intsN100.stream().filter(i -> i > MAX_RANDOM).parallel().findAny().get();
    }

    @Benchmark
    public double find_first_150_no_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).findFirst().get();
    }

    @Benchmark
    public double find_any_150_no_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).findAny().get();
    }

    @Benchmark
    public double find_first_150_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).parallel().findFirst().get();
    }

    @Benchmark
    public double find_any_150_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).parallel().findAny().get();
    }

    @Benchmark
    public double find_first_150_limit_no_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).limit(limit).findFirst().get();
    }

    @Benchmark
    public double find_any_150_limit_no_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).limit(limit).findAny().get();
    }

    @Benchmark
    public double find_first_150_limit_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).limit(limit).parallel().findFirst().get();
    }

    @Benchmark
    public double find_any_150_limit_parallel() {
        return intsN150.stream().filter(i -> i > MAX_RANDOM).limit(limit).parallel().findAny().get();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(M04_FindFirstList.class.getName())
                .build();
        new Runner(opt).run();
    }
}
