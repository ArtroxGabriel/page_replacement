package org.example;

import org.example.cli.Pager;
import picocli.CommandLine;

// cmd example: ./gradlew run --args="--algo FIFO --frames 3 --trace silberschatz2001.trace --verbose"

public class Main {
  public static void main(String[] args) {
    if (args.length == 0) {
      args = new String[]{
          "--algo", "FIFO",
          "--frames", "3",
          "--trace", "silberschatz2001.trace",
          "--verbose"
      };
    }

    Pager pager = new Pager();
    int exitCode = new CommandLine(pager).execute(args);

    System.exit(exitCode);
  }
}