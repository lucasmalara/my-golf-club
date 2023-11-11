package com.mygolfclub.utils.reader;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileReader {

    public static String readToString(String filePath) {
        StringBuilder builder = new StringBuilder();
        readAllLines(filePath).forEach(builder::append);
        return builder.toString();
    }

    public static List<String> readAllLines(String filePath) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            return reader.lines().toList();
        } catch (IOException ignored) {
            return List.of("");
        }
    }
}
