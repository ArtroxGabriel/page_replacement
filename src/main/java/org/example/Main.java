package org.example;

import org.example.cli.Pager;
import picocli.CommandLine;

public class Main {

  // cmd example: ./gradlew run --args="--algo FIFO --frames 3 --trace src/main/resources/silberschatz2001.trace --verbose"
  public static void main(String[] args) {
    if (args.length == 0) {
      args = new String[]{
          "--algo", "FIFO",
          "--frames", "3",
          "--trace", "src/main/resources/silberschatz2001.trace",
          "--verbose"
      };
    }

    Pager pager = new Pager();
    int exitCode = new CommandLine(pager).execute(args);

    System.exit(exitCode);
  }
}