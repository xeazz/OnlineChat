package Settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadSettings {
    private int port;
    private String address;


    public LoadSettings() {
        try (BufferedReader br = new BufferedReader(new FileReader("setting.txt"))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith("PORT: ")) {
                    port = Integer.parseInt(line.replaceAll("PORT: ", ""));
                }
                if (line.startsWith("ADDRESS: ")) {
                    address = line.replaceAll("ADDRESS: ", "");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
