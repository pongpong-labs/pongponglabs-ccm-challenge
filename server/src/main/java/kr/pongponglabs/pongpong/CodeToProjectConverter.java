package kr.pongponglabs.pongpong;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeToProjectConverter {
    public void run(String projectName, Language language, String code) {
        try {
            if(language.type == Language.Type.Python) {
                Path path = Paths.get("./projects/" + projectName);

                Files.createDirectory(path);

                File codeFile = new File("./projects/" + projectName + "/main.py");
                if (codeFile.createNewFile()) {
                    FileWriter writer = new FileWriter(codeFile);

                    writer.write(code);

                    writer.close();
                }
            }

            if(language.type == Language.Type.Java) {
                Path path = Paths.get("./projects/" + projectName + "/src/main/java");

                Files.createDirectories(path);

                File codeFile = new File("./projects/" + projectName + "/src/main/java/main.java");
                if (codeFile.createNewFile()) {
                    FileWriter writer = new FileWriter(codeFile);

                    writer.write(code);

                    writer.close();
                }
            }

            if(language.type == Language.Type.C || language.type == Language.Type.CPP) {
                String extension = ".c";

                if(language.type == Language.Type.CPP)
                    extension += "pp";

                Path path = Paths.get("./projects/" + projectName);

                Files.createDirectory(path);

                File codeFile = new File("./projects/" + projectName + "/main" + extension);
                if (codeFile.createNewFile()) {
                    FileWriter writer = new FileWriter(codeFile);

                    writer.write(code);

                    writer.close();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
