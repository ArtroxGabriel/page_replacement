package org.example.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TraceReader {

  private static final String RESOURCES_PATH = "src/main/resources/";

  public List<Integer> readTrace(String fileName) throws IOException {
    Path filePath = Paths.get(RESOURCES_PATH + fileName);

    fileValidation(filePath);

    List<Integer> pageReferences = Files.readAllLines(filePath)
        .stream()
        .map(String::trim)
        .filter(line -> !line.isEmpty())
        .map(line -> {
          try {
            return Integer.parseInt(line);
          } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid page reference in trace file: " + line);
          }
        })
        .toList();

    if (pageReferences.isEmpty()) {
      throw new IllegalArgumentException("Trace file is empty: " + filePath);
    }

    return pageReferences;
  }

  private void fileValidation(Path filePath) throws IOException {
    if (!Files.exists(filePath)) {
      throw new IOException("Trace file not found: " + filePath);
    }

    if (!Files.isRegularFile(filePath)) {
      throw new IOException("Path is not a file: " + filePath);
    }
  }
}