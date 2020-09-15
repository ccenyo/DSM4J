import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static void writeToFile(File file, List<String> line) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            for(String link : line) {
                bw.write(link);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected static File makeFile(TemporaryFolder folder, List<String> lines, String fileName) throws IOException {
        File file = folder.newFile(fileName);
        if(file.exists()) {
            writeToFile(file, lines);
        }
        return file;
    }

    protected static File makeFile(TemporaryFolder folder, String content, String fileName) throws IOException {
        File file = folder.newFile(fileName);
        if(file.exists()) {
            writeToFile(file, Collections.singletonList(content));
        }
        return file;
    }
}
