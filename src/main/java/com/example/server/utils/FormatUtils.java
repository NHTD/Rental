package com.example.server.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.stream.Stream;

@UtilityClass
public class FormatUtils {
    public static String format(final String message, final Object... args) {
        if (!StringUtils.hasText(message)) {
            return message;
        }
        String replaced = message;
        boolean stop = false;
        final Iterator<Object> it = Stream.of(args).iterator();
        while (!stop && it.hasNext()) {
            final String next = formatObject(it.next());
            final String after = replaced.replaceFirst("\\{\\}", next);
            stop = after.equals(replaced);
            replaced = after;
        }
        return replaced;
    }

    private static String formatObject(final Object object) {
        return object == null ? "null" : object.toString();
    }
}
