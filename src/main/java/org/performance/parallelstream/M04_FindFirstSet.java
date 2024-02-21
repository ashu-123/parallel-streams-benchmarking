package org.performance.parallelstream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class M04_FindFirstSet {

    private Random random = new Random();

    @Param({"1000000"})
    private int N100;
    private int limit;
    private Set<Integer> intsN100;
    private Set<Integer> intsN150;

    @Setup
    public void setup() {
        intsN100 = IntStream.range(0, N100)
                .mapToObj(index -> random.nextInt(100_0000_000))
                .collect(Collectors.toSet());
        intsN100.add(200_0000_000);

        Set<Integer> intsN50 = IntStream.range(0, N100/2)
                .mapToObj(index -> random.nextInt(100_0000_000))
                .collect(Collectors.toSet());

        intsN150 = new HashSet<>();
        intsN150.addAll(intsN100);
        intsN150.add(200_0000_000);
        intsN150.addAll(intsN50);
        limit = intsN150.size() + 1;
    }

    @Benchmark
    public double find_first_100_no_parallel() {
        return intsN100.stream().filter(i -> i > 100_0000_000).findFirst().get();
    }

    @Benchmark
    public double find_any_100_no_parallel() {
        return intsN100.stream().filter(i -> i > 100_0000_000).findAny().get();
    }

    @Benchmark
    public double find_first_100_parallel() {
        return intsN100.stream().filter(i -> i > 100_0000_000).parallel().findFirst().get();
    }

    @Benchmark
    public double find_any_100_parallel() {
        return intsN100.stream().filter(i -> i > 100_0000_000).parallel().findAny().get();
    }

    @Benchmark
    public double find_first_150_no_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).findFirst().get();
    }

    @Benchmark
    public double find_any_150_no_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).findAny().get();
    }

    @Benchmark
    public double find_first_150_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).parallel().findFirst().get();
    }

    @Benchmark
    public double find_any_150_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).parallel().findAny().get();
    }

    @Benchmark
    public double find_first_150_limit_no_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).limit(limit).findFirst().get();
    }

    @Benchmark
    public double find_any_150_limit_no_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).limit(limit).findAny().get();
    }

    @Benchmark
    public double find_first_150_limit_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).limit(limit).parallel().findFirst().get();
    }

    @Benchmark
    public double find_any_150_limit_parallel() {
        return intsN150.stream().filter(i -> i > 100_0000_000).limit(limit).parallel().findAny().get();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(M04_FindFirstSet.class.getName())
                .build();
        new Runner(opt).run();
    }
}
