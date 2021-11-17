package fibonacci;

import java.math.BigInteger;

public class MatrixFibonacciCalculator implements FibonacciCalculator {
    @Override
    public BigInteger calculate(int n) {
        if (n < 3) {
            return BigInteger.ONE;
        } else {
            Matrix matrix = Matrix.BASE.power(n - 2);
            return matrix.a11.add(matrix.a21);
        }
    }

    private static class Matrix {

        final static Matrix ONE = new Matrix(BigInteger.ONE, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE);
        final static Matrix BASE = new Matrix(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO);

        public final BigInteger a11;
        public final BigInteger a12;
        public final BigInteger a21;
        public final BigInteger a22;

        public Matrix(BigInteger a11, BigInteger a12,
                      BigInteger a21, BigInteger a22) {
            this.a11 = a11;
            this.a12 = a12;
            this.a21 = a21;
            this.a22 = a22;
        }

        public Matrix multiply(Matrix other) {
            return new Matrix(
                    a11.multiply(other.a11).add(a12.multiply(other.a21)),
                    a11.multiply(other.a12).add(a12.multiply(other.a22)),
                    a21.multiply(other.a11).add(a22.multiply(other.a21)),
                    a21.multiply(other.a12).add(a22.multiply(other.a22))
            );
        }

        public Matrix power(int b) {
            if (b == 0) return ONE;
            if (b == 1) return this;
            Matrix c = this.power(b / 2);
            c = c.multiply(c);
            if (b % 2 == 1) {
                c = c.multiply(this);
            }
            return c;
        }
    }
}
