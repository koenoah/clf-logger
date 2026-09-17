package io.github.koenoah.cli;

public class App 
{
    public static void main( String[] args )
    {
        if (args.length < 1) {
            System.err.println("Error: missing argument");
            System.out.println("Usage: java APP <file-path>");
            System.exit(1);
        }

        String filePath = args[0];

        LogEntry[] logs = LogAnalyzer.fileToData(filePath);
    }
}
