package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static Integer StringToInteger(String str) throws NumberFormatException {
        return Integer.valueOf(str);
    }

    public static Integer StringToInteger(String str, Integer defaultValue) {
        Integer result = defaultValue;
        try {
            result = Integer.valueOf(str);
        } finally {
            return result;
        }
    }

    public static Boolean StringToBoolean(String str) {
        return Boolean.valueOf(str);
    }

    public static Double StringToDouble(String str) throws NumberFormatException {
        return Double.valueOf(str);
    }

    public static Double StringToDouble(String str, Double defaultValue) {
        Double result = defaultValue;
        try {
            result = Double.valueOf(str);
        } finally {
            return result;
        }
    }

    public static List<String> SplitToSentences(String string) {
        List<String> stringList = new ArrayList<>();
        String str = string.trim();

        String THREE_DOT = "#3DOT#";

        Pattern compile = Pattern.compile("[.?!\u061f\u2e2e\u2200c]{3,}", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            String s = group.substring(0, 1);
            if (s.equals(".")) {
                str = str.replace(group, THREE_DOT);
            } else {
                str = str.replace(group, s + s + s);
            }
        }

        String[] split = str.split("<br>");

        for (String s : split) {
            s = s.trim();
            boolean hasEnd = false;
            String[] ends = {"?", "؟", "!", "."};
            for (String end : ends) {
                if (s.endsWith(end)) {
                    hasEnd = true;
                }
            }
            if (!hasEnd) {
                s = s + ".";
            }

            Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
            Matcher matcher2 = re.matcher(s);
            while (matcher2.find()) {
                String group = matcher2.group();
                stringList.add(group);
            }
        }

        List<String> out = new ArrayList<>();
        for (String s : stringList) {
            s = cleanText(s);
            out.add(s.replaceAll(THREE_DOT, "..."));
        }

        return out;
    }

    public static String cleanText(String text) {


        return text;
    }

    public static Integer getInteger(String s, Integer defaultValue) {
        if (isNumeric(s)) {
            return Integer.valueOf(s);
        }
        return defaultValue;
    }

    public static Integer getInteger(String s) {
        return getInteger(s, null);
    }
}
