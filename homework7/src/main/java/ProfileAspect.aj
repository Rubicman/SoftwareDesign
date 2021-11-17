import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public aspect ProfileAspect {

    private static final Map<String, Statistic> statistics = new HashMap<>();

    pointcut allMethods(): call(* fibonacci..*(..));
    pointcut interestingMethods(): call(* fibonacci.RecursiveFibonacciCalculator.calculate(..))
            || call(* fibonacci.LinearFibonacciCalculator.sum(..))
            || call(* fibonacci.MatrixFibonacciCalculator.Matrix.power(..));


    Object around(): interestingMethods()  {
        String method = thisJoinPoint.getSignature().toLongString();

        Instant start = Instant.now();
        Object result = proceed();
        Instant finish = Instant.now();

        statistics.put(
                method,
                statistics.getOrDefault(
                        method,
                        new Statistic()
                ).addCall(Duration.between(start, finish))
        );

        return result;
    }

    public static void clearStatistic() {
        statistics.clear();
    }

    public static void showStatistic() {
        for (var entry : statistics.entrySet()) {
            String[] components = entry.getKey().split(" ");
            System.out.println(components[components.length - 1]);
            System.out.println("\tcall count: " + entry.getValue().callCount);
            System.out.println("\tsum running time: " + entry.getValue().totalRunningTime);
            System.out.println("\taverage running time: " + entry.getValue().totalRunningTime.dividedBy(entry.getValue().callCount));
            System.out.println("\tmax running time: " + entry.getValue().maxRunningTime);
            System.out.println();
        }
    }

    public static class Statistic {
        private int callCount = 0;
        private Duration totalRunningTime = Duration.ZERO;
        private Duration maxRunningTime = Duration.ZERO;

        public Statistic() {
        }

        public Statistic addCall(Duration runningTime) {
            callCount++;
            totalRunningTime = totalRunningTime.plus(runningTime);
            if (maxRunningTime.compareTo(runningTime) < 0) {
                maxRunningTime = runningTime;
            }
            return this;
        }
    }
}
