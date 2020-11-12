package kr.pongponglabs.pongpong;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CppCheckRunner {
    public String run(String projectName, Language language) {
        String extension = ".c";

        if(language.type == Language.Type.CPP)
            extension += "pp";

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("cppcheck", "--output-file=report.txt", "--enable=all", "main" + extension);

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

        Path path = Paths.get("./projects/" + projectName + "/report.txt");
        Charset cs = StandardCharsets.UTF_8;

        List<String> stringList = new ArrayList<>();

        try {
            stringList = Files.readAllLines(path,cs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder report = new StringBuilder();

        for(String line : stringList)
            report.append(line).append('\n');

        return report.toString();
    }
}
