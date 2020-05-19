package tech.ikora.smells.checks;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.analytics.visitor.CountCallByTypeVisitor;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;

public class LoggingInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        int statements = countStatements(testCase.getSetup()) + countStatements(testCase.getTearDown());
        int logging = countLogging(testCase.getSetup()) + countLogging(testCase.getTearDown());

        double metric = (double)logging / (double)statements;

        return new SmellMetric(SmellMetric.Type.LOGGING_IN_FIXTURE_CODE, metric);
    }

    private int countStatements(KeywordCall fixture){
        if(fixture == null){
            return 0;
        }

        return KeywordStatistics.getStatementCount(fixture);
    }

    private int countLogging(KeywordCall fixture){
        if(fixture == null){
            return 0;
        }

        CountCallByTypeVisitor visitor = new CountCallByTypeVisitor(Keyword.Type.LOG);
        return visitor.getCount();
    }
}
