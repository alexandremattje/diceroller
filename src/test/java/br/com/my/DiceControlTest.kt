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

        Assert.assertEquals("5d2+2d3-1+d6", dc.fullDice )
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
        dc.addDice("d10")

        Assert.assertEquals("5d2+2d3-1d10", dc.fullDice)
    }

    internal fun testRolld2() {
        val dc = DiceControl()

        dc.addDice("d2")
        System.out.println("" + dc.roll() + ":" + dc.toString())
    }

    internal fun testRoll3d2() {
        val dc = DiceControl()

        dc.addNumber("3")
        dc.addDice("d2")
        System.out.println("" + dc.roll() + ":" + dc.toString())
    }

    internal fun testRoll3d2Plus1() {
        val dc = DiceControl()

        dc.addNumber("3")
        dc.addDice("d2")
        dc.addSignal("+")
        dc.addNumber("1")
        System.out.println("" + dc.roll() + ":" + dc.toString())
    }

    internal fun testRoll23d6Plus45() {
        val dc = DiceControl()

        dc.addNumber("2")
        dc.addNumber("3")
        dc.addDice("d6")
        dc.addSignal("+")
        dc.addNumber("4")
        dc.addNumber("5")
        System.out.println("" + dc.roll() + ":" + dc.toString())
    }

    internal fun testRollAll() {
        val dc = DiceControl()

        dc.addDice("d2")
        dc.addDice("d3")
        dc.addDice("d4")
        dc.addDice("d6")
        dc.addDice("d8")
        dc.addDice("d10")
        dc.addDice("d12")
        dc.addDice("d20")
        dc.addDice("d100")
        System.out.println("" + dc.roll() + ":" + dc.toString())
    }

    internal fun testRoll23d6Plus45Plus4d23Plus234() {
        val dc = DiceControl()

        dc.addNumber("2")
        dc.addNumber("3")
        dc.addDice("d6")
        dc.addSignal("+")
        dc.addNumber("4")
        dc.addNumber("5")
        dc.addSignal("+")
        dc.addNumber("4")
        dc.addDice("d23")
        dc.addSignal("+")
        dc.addNumber("2")
        dc.addNumber("3")
        dc.addNumber("4")
        System.out.println("" + dc.roll() + ":" + dc.toString())
    }

}
