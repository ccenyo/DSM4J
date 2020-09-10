import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.List;

public class BaseTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    protected File makeFile(List<String> lines, String fileName) throws IOException {
        File file = folder.newFile(fileName);
        if(file.exists()) {
            writeToFile(file, lines);
        }
        return file;
    }

    public static void writeToFile(File fout, List<String> line) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
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
}
