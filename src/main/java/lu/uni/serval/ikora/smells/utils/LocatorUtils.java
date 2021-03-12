package lu.uni.serval.ikora.smells.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class LocatorUtils {
    private static final Pattern xPathPattern =  Pattern.compile("^xpath:", Pattern.CASE_INSENSITIVE);
    private static final Pattern cssPattern =  Pattern.compile("^css:", Pattern.CASE_INSENSITIVE);
    private static final Pattern jqueryPattern =  Pattern.compile("^jquery:", Pattern.CASE_INSENSITIVE);

    private LocatorUtils() {}

    public static boolean isComplex(final String value, int maxSize){
        if(xPathPattern.matcher(value).find()){
            return getXPathSize(value.replaceAll(xPathPattern.pattern(), "")) > maxSize;
        }
        else if(cssPattern.matcher(value).find()){
            return getCssSize(value.replaceAll(cssPattern.pattern(), "")) > maxSize;
        }
        else if(jqueryPattern.matcher(value).find()){
            return getJquerySize(value.replaceAll(jqueryPattern.pattern(), "")) > maxSize;
        }

        // all other strategies do not generate complex locators
        return false;
    }

    private static int getXPathSize(final String value) {
        return value.replaceAll("^(//|/)", "").split("/").length;
    }

    private static int getCssSize(final String value){
        return value.split(">").length;
    }

    private static int getJquerySize(final String value){
        final String[] split = value.trim()
                .replaceAll("(?:([\"'`])[^\1]*?\1)\\s+|\\r?\\n", "")
                .split("[.,\\s>#]");

        return (int)Arrays.stream(split).filter(s -> !s.isEmpty()).count();
    }
}
