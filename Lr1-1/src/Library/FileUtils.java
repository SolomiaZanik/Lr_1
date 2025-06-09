package Library;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void writeToFile(String filename, double[] data) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < data.length; i++) {
                writer.write(String.format("%d %.10f\n", i, data[i]));
            }
        } catch (IOException e) {
            System.out.println("Помилка запису у файл: " + e.getMessage());
        }
    }
}