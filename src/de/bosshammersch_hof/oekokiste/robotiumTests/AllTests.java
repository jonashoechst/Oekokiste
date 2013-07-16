package de.bosshammersch_hof.oekokiste.robotiumTests;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        suite.addTest(TestSuite.createTest(TestMainActivity.class, "testLoginActivity"));
        suite.addTest(TestSuite.createTest(TestMainActivity.class, "testFindRecipesByArticleActivity_NOITEMS"));
        suite.addTest(TestSuite.createTest(TestMainActivity.class, "testFindRecipesByArticleActivity_WITHITEMS"));
        suite.addTest(TestSuite.createTest(TestMainActivity.class, "testScaleRecipeDetailActivity"));
        suite.addTest(TestSuite.createTest(TestMainActivity.class, ""));
        
        suite.addTest(TestSuite.createTest(TestLoginActivity.class, "testGo2OrderActivity"));

        
        return suite;
    }
}
