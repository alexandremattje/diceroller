package br.com.my

import junit.framework.Assert
import junit.framework.Test
import junit.framework.TestCase
import junit.framework.TestSuite

/**
 * Unit test for simple App.
 */
class DiceControlTest : TestCase() {

    internal fun test1() {
        val dc = DiceControl()

        dc.addDice("d2")
        dc.addNumber("2")
        dc.addDice("d3")
        dc.addSignal("-")
        dc.addNumber("1")

        Assert.assertEquals(dc.fullDice, "d2+2d3-1")
    }

    internal fun test2() {
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

    internal fun test3() {
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

    internal fun test4() {
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

    internal fun testRolld2() {
        val dc = DiceControl()

        dc.addDice("d2")
        dc.roll()
        System.out.println(dc.toString())
    }

    internal fun testRoll3d2() {
        val dc = DiceControl()

        dc.addNumber("3")
        dc.addDice("d2")
        dc.roll()
        System.out.println(dc.toString())
    }

    internal fun testRoll3d2Plus1() {
        val dc = DiceControl()

        dc.addNumber("3")
        dc.addDice("d2")
        dc.addSignal("+")
        dc.addNumber("1")
        dc.roll()
        System.out.println(dc.toString())
    }

    internal fun testRoll22d6Plus33() {
        val dc = DiceControl()

        dc.addNumber("2")
        dc.addNumber("2")
        dc.addDice("d6")
        dc.addSignal("+")
        dc.addNumber("3")
        dc.addNumber("3")
        dc.roll()
        System.out.println(dc.toString())
    }

}
