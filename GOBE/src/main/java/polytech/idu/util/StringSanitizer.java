package polytech.idu.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringSanitizer {

    public static String cleanString(String input) {
        if (input == null) {
            return null;
        }

        // 1. Normalize and remove accents
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(normalized).replaceAll("");

        // 2. Convert to lowercase and trim whitespace
        // Using Locale.ROOT ensures consistent behavior (avoids "Turkish I" issues)
        return noAccents.toLowerCase(Locale.ROOT).trim();
    }
}