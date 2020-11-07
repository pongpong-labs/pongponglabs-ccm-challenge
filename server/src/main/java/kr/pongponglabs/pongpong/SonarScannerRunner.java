package kr.pongponglabs.pongpong;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SonarScannerRunner {
    public String run(String projectName) {
        makePropertyFile(projectName);

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("sonar-scanner");
        processBuilder.directory(new File("./projects/" + projectName));

        StringBuilder res = new StringBuilder();

        try {
            var process = processBuilder.start();

            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    res.append(line).append('\n');
                }

                int exitCode = process.waitFor();
                res.append("\nExited with error code : ").append(exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    private void makePropertyFile(String fileName) {
        try {
            File property = new File("./projects/" + fileName + "/sonar-project.properties");
            if (property.createNewFile()) {
                FileWriter writer = new FileWriter(property);

                String options = "sonar.projectKey=" + fileName + "\n"
                               + "sonar.host.url=http://164.125.219.21:6379" + "\n";

                writer.write(options);
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
