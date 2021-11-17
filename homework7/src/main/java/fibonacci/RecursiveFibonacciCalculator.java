package fibonacci;

import java.math.BigInteger;

public class RecursiveFibonacciCalculator implements FibonacciCalculator {
    @Override
    public BigInteger calculate(int n) {
        if (n < 3) {
            return BigInteger.ONE;
        } else {
            return calculate(n - 1).add(calculate(n - 2));
        }
    }
}
