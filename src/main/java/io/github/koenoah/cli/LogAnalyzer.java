package io.github.koenoah.cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogAnalyzer {
    private static final Pattern PATTERN = Pattern.compile(
            "\\S+(?=\\s+-\\s+-)|" + // IP addresses before " - - "
            "\\[(.*?)\\]|" +        // Timestamp
            "(?<=\")[A-Z]{3,7}|" +    // HTTP Method
            "(?<!\\S)\\d{1,3}(?!\\S)"); // Status Code
    private static final DateTimeFormatter CLF_FORMATTER = DateTimeFormatter
        .ofPattern("dd/MMM/yyy:HH:mm:ss Z", Locale.ENGLISH);

    private static LogEntry extractCLF(String string) {
        Matcher matcher = PATTERN.matcher(string);

        String ip = null;
        Instant timestamp = null;
        String httpmethod = null;
        int statusCode = 0;
        long responseSize = (long) string.length();
        
        int counter = 0;
        while (matcher.find() && counter < 4) {
            String group = matcher.group();

            switch(counter) {
                case 0:
                    ip = group;
                    break;
                case 1:
                    String cleaned = group.substring(1, group.length() - 1);
                    timestamp = Instant.from(CLF_FORMATTER.parse(cleaned));
                    break;
                case 2:
                    httpmethod = group.trim().toUpperCase();
                    break;
                case 3:
                    statusCode = Integer.parseInt(group);
                    break;
                default:
            }
            counter++;
        }
        LogEntry log = new LogEntry(timestamp, ip, httpmethod, statusCode, responseSize);

        return log;
    }

    public static LogEntry[] fileToData (String filePath) {
        LogEntry[] mainArray = {};

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogEntry log = extractCLF(line);
                System.out.println(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mainArray;
    }
}
