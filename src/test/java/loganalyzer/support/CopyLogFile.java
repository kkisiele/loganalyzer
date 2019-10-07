package loganalyzer.support;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class CopyLogFile {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(CopyLogFile.class.getResourceAsStream("/logfile.txt")));
        List<String> lines = new LinkedList<>();
        String line;
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("big_logfile.txt"));
        for(int i = 0; i < 2_000_000; i++)
           save(writer, lines);
        writer.close();
    }

    private static void save(BufferedWriter writer, List<String> lines) {
        lines.forEach(line -> {
            try {
                writer.write(line);
                writer.write("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
