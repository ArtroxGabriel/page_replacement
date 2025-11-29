package org.example;

import org.example.cli.Pager;
import picocli.CommandLine;


public class Main {

  public static void main(String[] args) {
    int exitCode = new CommandLine(new Pager()).execute(args);
    System.exit(exitCode);
  }

}