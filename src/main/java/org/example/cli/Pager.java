package org.example.cli;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "pager", mixinStandardHelpOptions = true, version = "pager 0.1", description = "example usage of command line")
public class Pager implements Callable<Integer> {

  @Parameters(index = "0", description = "The file whose checksum to calculate.")
  private File file;

  @Option(names = {"-a",
      "--algorithm"}, description = "MD5, SHA-1, SHA-256, ...", defaultValue = "SHA-256")
  private String algorithm;

  @Override
  public Integer call() throws Exception {
    byte[] fileContents = Files.readAllBytes(file.toPath());
    byte[] digest = MessageDigest.getInstance(algorithm).digest(fileContents);
    System.out.printf("%0" + (digest.length * 2) + "x%n", new BigInteger(1, digest));
    return 0;
  }
}
