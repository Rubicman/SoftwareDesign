import fibonacci.LinearFibonacciCalculator;
import fibonacci.MatrixFibonacciCalculator;
import fibonacci.RecursiveFibonacciCalculator;

public class Main {
    public static void main(String[] args) {
        System.out.println("--------------30th Fibonacci number--------------");
        System.out.println();
        new RecursiveFibonacciCalculator().calculate(30);
        new LinearFibonacciCalculator().calculate(30);
        new MatrixFibonacciCalculator().calculate(30);
        ProfileAspect.showStatistic();
        ProfileAspect.clearStatistic();
        System.out.println("------------300,000th Fibonacci number------------");
        System.out.println();
        new LinearFibonacciCalculator().calculate(300_000);
        new MatrixFibonacciCalculator().calculate(300_000);
        ProfileAspect.showStatistic();
    }
}
