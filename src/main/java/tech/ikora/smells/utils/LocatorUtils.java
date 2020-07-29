package tech.ikora.smells.utils;

import tech.ikora.smells.utils.css.parser.SelectorParser;
import tech.ikora.smells.utils.css.selector.CompoundSelector;
import tech.ikora.smells.utils.css.selector.Selector;

import java.util.regex.Pattern;

public class LocatorUtils {
    private static final Pattern xPathPattern =  Pattern.compile("^xpath:", Pattern.CASE_INSENSITIVE);
    private static final Pattern cssPattern =  Pattern.compile("^css:", Pattern.CASE_INSENSITIVE);

    public static boolean isComplex(final String value, int maxSize){
        if(xPathPattern.matcher(value).find()){
            return getXPathSize(value.replaceAll(xPathPattern.pattern(), "")) > maxSize;
        }
        else if(cssPattern.matcher(value).find()){
            return getCssSize(value.replaceAll(cssPattern.pattern(), "")) > maxSize;
        }

        return false;
    }

    private static int getXPathSize(final String value) {
        return value.replaceAll("^(//|/)", "").split("/").length;
    }

    private static int getCssSize(final String value){
        final Selector selector = SelectorParser.parse(value);
        CompoundSelector compoundSelector = selector.getCompoundSelector();

        int size = 1;
        while (compoundSelector.getPrevious() != null){
            compoundSelector = compoundSelector.getPrevious().getSecond();
            ++size;
        }

        return size;
    }
}
