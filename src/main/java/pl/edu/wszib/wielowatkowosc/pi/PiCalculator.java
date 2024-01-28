package pl.edu.wszib.wielowatkowosc.pi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.*;

public class PiCalculator {

    public static void main(String[] args) {
        final int allPoints = 1000000000; // max 2147483647
        int threadCount = 4;
        ExecutorService executor = newFixedThreadPool(threadCount);

        List<Future<Integer>> futureResults = new ArrayList<>();

        int pointsPerThread = allPoints / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final Callable<Integer> callable = () -> {
                int pointsInCircle = 0;
                Random random = new Random();
                for (int j = 0; j < pointsPerThread; j++) {
                    double x = random.nextDouble();
                    double y = random.nextDouble();
                    double distanceFromCenter = Math.sqrt(x * x + y * y);
                    if (distanceFromCenter < 1) {
                        pointsInCircle++;
                    }
                }
                return pointsInCircle;
            };
            futureResults.add(executor.submit(callable));
        }

        executor.shutdown();

        long totalPointsInCircle = 0;
        for (Future<Integer> result : futureResults) {
            try {
                // Dodawanie wyniku do sumy punktów wewnątrz okręgu
                totalPointsInCircle += result.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        double pi = 4.0 * totalPointsInCircle / allPoints;
        System.out.println("Przybliżona wartość liczby PI wynosi: " + pi);
    }
}