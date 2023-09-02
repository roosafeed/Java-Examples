import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPerformanceDemo {
    public static void main(String[] args) {
        // Number of threads to create
        int numThreads1 = 4;
        int numThreads2 = 1000;
        int[] numbers = generateNumbers(10000); // Generate an array of numbers

        long startTime = System.currentTimeMillis();
        int result1 = computeSumUsingThreads(numbers, numThreads1);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.printf("Thread count: %d \nResult: %d \nExecution time: %d ms\n\n", numThreads1, result1, executionTime);

        long startTime2 = System.currentTimeMillis();
        int result2 = computeSumUsingThreads(numbers, numThreads2);
        long endTime2 = System.currentTimeMillis();
        long executionTime2 = endTime2 - startTime2;
        System.out.printf("Thread count: %d \nResult: %d \nExecution time: %d ms\n\n", numThreads2, result2, executionTime2);
    }

    static int[] generateNumbers(int size) {
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i;
        }
        return numbers;
    }

    static int computeSumUsingThreads(int[] numbers, int numThreads) {
        int chunkSize = numbers.length / numThreads;
        AtomicInteger result = new AtomicInteger();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numThreads - 1) ? numbers.length : (i + 1) * chunkSize;

            Thread thread = new Thread(() -> {
                int localSum = 0;
                for (int j = startIndex; j < endIndex; j++) {
                    localSum += numbers[j];
                }
                synchronized (ThreadPerformanceDemo.class) {
                    result.addAndGet(localSum); // Synchronize access to the result
                }
            });

            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result.get();
    }
}

