package org.performance.parallelstream;

import java.util.stream.IntStream;

public class M04_Associative {

    public static void main(String[] args) {

        int sum = IntStream.range(0, 10).sum();
        System.out.println("sum = " + sum);

        int sumParallel = IntStream.range(0, 10).parallel().sum();
        System.out.println("parallel sum = " + sumParallel);

        int sumOfSquares = IntStream.range(0, 10).reduce(0, (i1, i2) -> i1*i1 + i2*i2);
        System.out.println("sum of squares = " + sumOfSquares);

        int sumOfSquaresParallel = IntStream.range(0, 10).map(i -> i*i).parallel().reduce(0, (i1, i2) -> i1*i1 + i2*i2);
        System.out.println("parallel sum of squares = " + sumOfSquaresParallel);
    }
}
