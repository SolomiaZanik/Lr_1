package Library;

public class RungeKuttaSolver {

    private double x0;
    private double y0;
    private double xEnd;
    private int n;

    public enum MethodType {
        SINGLE_THREAD,
        MULTI_THREAD,
        BOTH
    }

    public static double f(double x, double y) {
        double sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += Math.sin(x * y + i) * Math.cos(i);
        }
        return sum / 1000.0;
    }

    public static void rungeKuttaStep(double x0, double y0, double h, int n, double[] result, int offset) {
        double x = x0;
        double y = y0;
        for (int i = 0; i < n; i++) {
            double k1 = h * f(x, y);
            double k2 = h * f(x + h / 2, y + k1 / 2);
            double k3 = h * f(x + h / 2, y + k2 / 2);
            double k4 = h * f(x + h, y + k3);
            y += (k1 + 2 * k2 + 2 * k3 + k4) / 6;
            x += h;
            result[offset + i] = y;
        }
    }

    public static void runSingleThread(double x0, double y0, double xEnd, int n) {
        System.out.println("Запуск однопотокового методу...");
        long startTime = System.nanoTime();

        double h = (xEnd - x0) / n;
        double[] result = new double[n];
        rungeKuttaStep(x0, y0, h, n, result, 0);

        long endTime = System.nanoTime();
        double elapsedMs = (endTime - startTime) / 1e6;
        System.out.printf("Однопотоковий метод завершено. Час виконання: %.5f с\n", elapsedMs / 1000);

        System.out.println("Результати однопотокового методу (перші 10 значень):");
        for (int i = 0; i < Math.min(10, result.length); i++) {
            System.out.printf("y[%d] = %.5f\n", i, result[i]);
        }

        FileUtils.writeToFile("single_thread_results.txt", result);
    }

    public static void runMultiThread(double x0, double y0, double xEnd, int n) {
        System.out.println("Запуск багатопотокового методу...");
        long startTime = System.nanoTime();

        double h = (xEnd - x0) / n;
        int threadCount = Runtime.getRuntime().availableProcessors();
        double[] result = new double[n];

        Thread[] threads = new Thread[threadCount];
        int chunkSize = n / threadCount;
        int remainder = n % threadCount;

        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            int steps = (idx == threadCount - 1) ? chunkSize + remainder : chunkSize;
            int offset = idx * chunkSize;
            double localX0 = x0 + offset * h;
            threads[i] = new Thread(() -> {
                double[] localResult = new double[steps];
                rungeKuttaStep(localX0, y0, h, steps, localResult, 0);
                System.arraycopy(localResult, 0, result, offset, steps);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
        double elapsedMs = (endTime - startTime) / 1e6;
        System.out.printf("Багатопотоковий метод завершено. Час виконання: %.5f с\n", elapsedMs / 1000);

        System.out.println("Результати багатопотокового методу (перші 10 значень):");
        for (int i = 0; i < Math.min(10, result.length); i++) {
            System.out.printf("y[%d] = %.5f\n", i, result[i]);
        }

        FileUtils.writeToFile("multi_thread_results.txt", result);
    }

    public void solve(MethodType methodType) {
        switch (methodType) {
            case SINGLE_THREAD -> runSingleThread(x0, y0, xEnd, n);
            case MULTI_THREAD -> runMultiThread(x0, y0, xEnd, n);
            case BOTH -> {
                runSingleThread(x0, y0, xEnd, n);
                runMultiThread(x0, y0, xEnd, n);
            }
            default -> throw new IllegalArgumentException("Невідомий метод розв’язання.");
        }
    }

    public RungeKuttaSolver(double x0, double y0, double xEnd, int n) {
        this.x0 = x0;
        this.y0 = y0;
        this.xEnd = xEnd;
        this.n = n;
    }
}