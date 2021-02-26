package lu.uni.serval.ikora.smells.utils;

import lu.uni.serval.ikora.core.analytics.KeywordStatistics;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.HashMap;
import java.util.Map;

public class StatementCounter {
    private static final Map<TestCase, Integer> cache = new HashMap<>();

    public static int getCount(TestCase testCase){
        if(!cache.containsKey(testCase)){
            int count = KeywordStatistics.getStatementCount(testCase);
            cache.put(testCase, count);
        }

        return cache.get(testCase);
    }
}
