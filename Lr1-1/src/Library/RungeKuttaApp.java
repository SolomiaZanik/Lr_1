package Library;

import java.util.Scanner;

public class RungeKuttaApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть початкове значення x0: ");
        double x0 = scanner.nextDouble();

        System.out.print("Введіть початкове значення y0: ");
        double y0 = scanner.nextDouble();

        System.out.print("Введіть кінцеве значення xEnd: ");
        double xEnd = scanner.nextDouble();

        System.out.print("Введіть кількість кроків n: ");
        int n = scanner.nextInt();

        System.out.println("\nМеню:");
        System.out.println("1. Однопотоковий метод");
        System.out.println("2. Багатопотоковий метод");
        System.out.println("3. Обидва методи");
        System.out.print("Ваш вибір: ");
        int choice = scanner.nextInt();

        RungeKuttaSolver.MethodType methodType;
        switch (choice) {
            case 1 -> methodType = RungeKuttaSolver.MethodType.SINGLE_THREAD;
            case 2 -> methodType = RungeKuttaSolver.MethodType.MULTI_THREAD;
            case 3 -> methodType = RungeKuttaSolver.MethodType.BOTH;
            default -> {
                System.out.println("Невірний вибір.");
                return;
            }
        }

        RungeKuttaSolver solver = new RungeKuttaSolver(x0, y0, xEnd, n);
        solver.solve(methodType);

        scanner.close();
    }
}