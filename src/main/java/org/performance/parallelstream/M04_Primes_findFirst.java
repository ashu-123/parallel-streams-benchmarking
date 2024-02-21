package org.performance.parallelstream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class M04_Primes_findFirst {

    @Param({"10"})
    private int N;

    @Param({"64"})
    private int BIT_LENGTH;

    BigInteger probablePrime() {
        return BigInteger.probablePrime(BIT_LENGTH,
                ThreadLocalRandom.current());
    }

    @Benchmark
    public Object generate_primes_findFirst() {
        return IntStream.range(0, 1000)
                .mapToObj(i -> probablePrime())
                .filter(prime -> prime.toString().startsWith("1"))
                .findFirst();
    }

    @Benchmark
    public Object generate_primes_findAny() {
        return IntStream.range(0, 1000)
                .mapToObj(i -> probablePrime())
                .filter(prime -> prime.toString().startsWith("1"))
                .findAny();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(M04_Primes_findFirst.class.getName())
                .build();
        new Runner(opt).run();
    }}
