package org.kie.junit;

public class RuleNameMatcher {

    protected boolean shouldIgnoreCase = false;
    protected COMPARE_TYPE compareType = COMPARE_TYPE.EXACT_MATCH;
    protected String ruleName;

    boolean isShouldIgnoreCase() {
        return shouldIgnoreCase;
    }

    COMPARE_TYPE getCompareType() {
        return compareType;
    }

    String getRuleName() {
        return ruleName;
    }

    private void RuleNameMatcher(){

    }

    public static RuleNameMatcher newIgnoreCaseMatcher(){
        RuleNameMatcher matcher = new RuleNameMatcher();
        matcher.shouldIgnoreCase = true;
        return matcher;
    }

    public static RuleNameMatcher newMatcher(){
        return new RuleNameMatcher();
    }

    public RuleNameMatcher startsWith(String ruleName){
        compareType = COMPARE_TYPE.STARTS_WITH;
        this.ruleName = ruleName;
        return this;
    }

    public RuleNameMatcher endsWith(String ruleName){
        compareType = COMPARE_TYPE.ENDS_WITH;
        this.ruleName = ruleName;
        return this;
    }

    public RuleNameMatcher ignoreCase(boolean shouldIgnoreCase){
        this.shouldIgnoreCase = shouldIgnoreCase;
        return this;
    }

    public RuleNameMatcher exactMatch(String ruleName){
        compareType = COMPARE_TYPE.EXACT_MATCH;
        this.ruleName = ruleName;
        return this;
    }

    public RuleNameMatcher contains(String ruleName){
        compareType = COMPARE_TYPE.CONTAINS;
        this.ruleName = ruleName;
        return this;
    }

    protected enum COMPARE_TYPE {EXACT_MATCH, STARTS_WITH, ENDS_WITH, CONTAINS};
}
