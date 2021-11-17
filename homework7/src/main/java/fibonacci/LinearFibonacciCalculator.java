package fibonacci;

import java.math.BigInteger;

public class LinearFibonacciCalculator implements FibonacciCalculator {
    @Override
    public BigInteger calculate(int n) {
        BigInteger[] values = {BigInteger.ONE, BigInteger.ONE};
        for(int i = 3; i <= n; i++) {
            values = sum(values);
        }
        return values[1];
    }

    private BigInteger[] sum(BigInteger[] values) {
        return new BigInteger[]{values[1], values[0].add(values[1])};
    }
}
