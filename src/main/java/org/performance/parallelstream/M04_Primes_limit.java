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
public class M04_Primes_limit {

    @Param({"10"})
    private int N;

    @Param({"64"})
    private int BIT_LENGTH;

    BigInteger probablePrime() {
        return BigInteger.probablePrime(BIT_LENGTH,
                ThreadLocalRandom.current());
    }

    @Benchmark
    public List<BigInteger> generate_N_primes_limit() {
        return IntStream.range(0, 1000)
                .limit(N)
                .mapToObj(i -> probablePrime())
                .collect(toList());
    }

    @Benchmark
    public List<BigInteger> generate_N_primes() {
        return IntStream.range(0, N)
                .mapToObj(i -> probablePrime())
                .collect(toList());
    }

    @Benchmark
    public List<BigInteger> generate_N_primes_limit_parallel() {
        return IntStream.range(0, 1000)
                .limit(N)
                .parallel()
                .mapToObj(i -> probablePrime())
                .collect(toList());
    }

    @Benchmark
    public List<BigInteger> generate_N_primes_parallel() {
        return IntStream.range(0, N)
                .parallel()
                .mapToObj(i -> probablePrime())
                .collect(toList());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(M04_Primes_limit.class.getName())
                .build();
        new Runner(opt).run();
    }}
