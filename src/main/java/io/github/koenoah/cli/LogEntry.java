package io.github.koenoah.cli;

import java.time.Instant;

public record LogEntry(Instant timestamp, String ip, String method, int statusCode, long responseSize) {}
