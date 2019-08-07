package br.com.my;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class DiceControlTest
        extends TestCase {

    public static Test suite() {
        return new TestSuite(DiceControlTest.class);
    }

    public void test1() {
        DiceControl dc = new DiceControl();

        dc.addDice("d2");
        dc.addNumber("2");
        dc.addDice("d3");
        dc.addSignal("-");
        dc.addNumber("1");

        assertEquals(dc.fullDice, "d2+2d3-1");
    }

    public void test2() {
        DiceControl dc = new DiceControl();

        dc.addNumber("5");
        dc.addDice("d2");
        dc.addNumber("2");
        dc.addDice("d3");
        dc.addSignal("-");
        dc.addNumber("1");
        dc.addSignal("+");
        dc.addDice("d6");

        assertEquals(dc.fullDice, "5d2+2d3-1+d6");
    }

    public void test3() {
        DiceControl dc = new DiceControl();

        dc.addNumber("5");
        dc.addDice("d2");
        dc.addNumber("2");
        dc.addDice("d3");
        dc.addSignal("-");
        dc.addNumber("1");
        dc.addSignal("+");
        dc.addDice("d6");
        dc.removeLast();
        dc.addNumber("1");

        assertEquals(dc.fullDice, "5d2+2d3-1+1");
    }

    public void test4() {
        DiceControl dc = new DiceControl();

        dc.addNumber("5");
        dc.addDice("d2");
        dc.addNumber("2");
        dc.addDice("d3");
        dc.addSignal("-");
        dc.addNumber("1");
        dc.addSignal("+");
        dc.addDice("d6");
        dc.removeLast();
        dc.removeLast();
        dc.addNumber("d10");

        assertEquals("5d2+2d3-1 d10", dc.fullDice);
    }

}
