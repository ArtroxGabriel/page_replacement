package org.example;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();

      // create the Option
        Options options = new Options();
        options.addOption("a", "all", false, "do not hide entries starting with .");
        options.addOption("A", "almost-all", false, "do not list implied . and ..");
        options.addOption("b", "escape", false, "print octal escapes for non-graphic "
                + "characters");
        options.addOption(Option.builder("SIZE").longOpt("block-size")
                .desc("use SIZE-byte blocks")
                .hasArg()
                .get());
        options.addOption("B", "ignore-backups", false, "do not list implied entries "
                + "ending with ~");
        options.addOption("c", false, "with -lt: sort by, and show, ctime (time of last "
                + "modification of file status information) with "
                + "-l:show ctime and sort by name otherwise: sort "
                + "by ctime");
        options.addOption("C", false, "list entries by columns");

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);


            var formatter = new HelpFormatter();
            formatter.printHelp("ls", options);

            // validate that block-size has been set
            if (line.hasOption("block-size")) {
                // print the value of block-size
                System.out.println(line.getOptionValue("block-size"));
            }


        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}