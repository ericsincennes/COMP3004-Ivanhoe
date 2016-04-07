package comp3004.ivanhoe.testcases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
   ActionCardTest.class,
   BoardStateTest.class,
   CardTest.class,
   DeckTest.class,
   HandTest.class,
   PlayerTests.class,
   PointsBoardTest.class,
   RulesEngineTest.class
})
public class JunitTestSuite {   
} 
