package pokerapplication;

import java.io.*;

public class Counter {
//	/pokerapplication/pokerapplication/src/pokerapplication/counter.txt
    private static final String FILE_PATH = System.getProperty("user.dir") + "\\pokerapplication\\src\\pokerapplication\\counter.txt";
    private static int count;
    
    static {
        count = readCountFromFile();
    }
    
    private static int readCountFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Default value if the file doesn't exist or an error occurs
    }
    
    private static void writeCountToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(Integer.toString(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void increment() {
        count++;
        writeCountToFile();
    }
    
    public static void decrement() {
        count--;
        writeCountToFile();
    }
    
    public static void reset() {
        count = 0;
        writeCountToFile();
    }
    
    public static int getCount() {
        return count;
    }
}

