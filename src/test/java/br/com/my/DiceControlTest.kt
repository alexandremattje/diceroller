package br.com.my

import junit.framework.Assert
import junit.framework.Test
import junit.framework.TestCase
import junit.framework.TestSuite

/**
 * Unit test for simple App.
 */
class DiceControlTest : TestCase() {

    fun test1() {
        val dc = DiceControl()

        dc.addDice("d2")
        dc.addNumber("2")
        dc.addDice("d3")
        dc.addSignal("-")
        dc.addNumber("1")

        Assert.assertEquals(dc.fullDice, "d2+2d3-1")
    }

    fun test2() {
        val dc = DiceControl()

        dc.addNumber("5")
        dc.addDice("d2")
        dc.addNumber("2")
        dc.addDice("d3")
        dc.addSignal("-")
        dc.addNumber("1")
        dc.addSignal("+")
        dc.addDice("d6")

        Assert.assertEquals(dc.fullDice, "5d2+2d3-1+d6")
    }

    fun test3() {
        val dc = DiceControl()

        dc.addNumber("5")
        dc.addDice("d2")
        dc.addNumber("2")
        dc.addDice("d3")
        dc.addSignal("-")
        dc.addNumber("1")
        dc.addSignal("+")
        dc.addDice("d6")
        dc.removeLast()
        dc.addNumber("1")

        Assert.assertEquals(dc.fullDice, "5d2+2d3-1+1")
    }

    fun test4() {
        val dc = DiceControl()

        dc.addNumber("5")
        dc.addDice("d2")
        dc.addNumber("2")
        dc.addDice("d3")
        dc.addSignal("-")
        dc.addNumber("1")
        dc.addSignal("+")
        dc.addDice("d6")
        dc.removeLast()
        dc.removeLast()
        dc.addNumber("d10")

        Assert.assertEquals("5d2+2d3-1d10", dc.fullDice)
    }

    companion object {

        fun suite(): Test {
            return TestSuite(DiceControlTest::class.java)
        }
    }

}
