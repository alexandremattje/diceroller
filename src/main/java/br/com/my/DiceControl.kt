package br.com.my

import br.com.my.DiceControl.DiceElementType.*
import java.security.SecureRandom
import java.util.Stack
import kotlin.collections.ArrayList

/**
 * DiceControl
 */
class DiceControl {

    var fullDice: String = ""
    private val elements = Stack<DiceControl.DiceElement>()

    private enum class DiceElementType {
        NUMBER, DICE, SIGNAL
    }

    override fun toString(): String {
        var s: String = ""
        val it = this.elements.iterator()
        while (it.hasNext()) {
            val el = it.next()
            if (el.result.size > 0) {
                s += el
            }
        }

        return s
    }

    private class DiceElement constructor(val type: DiceElementType, var value: String) {
        var result: ArrayList<Int> = ArrayList()

        override fun toString(): String {
            return "DiceElement [type=" + this.type + ", value=" + this.value + ", result=" + this.result + "]"
        }

        fun clear() {
            result.clear()
        }

        companion object {

            fun newDice(dice: String): DiceElement {
                return DiceElement(DICE, dice)
            }

            fun newSignal(signal: String): DiceElement {
                return DiceElement(SIGNAL, signal)
            }

            fun newNumber(number: String): DiceElement {
                return DiceElement(NUMBER, number)
            }
        }

    }

    /**
     *
     */
    fun addDice(dice: String) {
        this.elements.push(DiceElement.newDice(dice))
        this.rebuild()
    }

    /**
     *
     */
    fun addSignal(signal: String) {
        this.elements.push(DiceElement.newSignal(signal))
        this.rebuild()
    }

    /**
     *
     */
    fun addNumber(number: String) {
        this.elements.push(DiceElement.newNumber(number))
        this.rebuild()
    }

    /**
     *
     */
    fun removeLast() {
        this.elements.pop()
        this.rebuild()
    }

    private fun clearResults() {
        val it = this.elements.iterator()
        while (it.hasNext()) {
            val el = it.next()
            el.clear()

        }
    }

    /**
     *
     */
    fun roll() {
        clearResults()
        val it = this.elements.iterator()
        var previous: DiceElement? = null
        var tempNumber: DiceElement = DiceElement.newNumber("0")
        tempNumber.result.add(0)
        while (it.hasNext()) {
            val el = it.next()
            if (previous != null) {
                rollPreviousElement(previous, el, tempNumber)
            } else {
                tempNumber = rollCurrentElement(el, tempNumber)
            }
            previous = el
        }
    }

    private fun rollCurrentElement(el: DiceElement, tempNumber: DiceElement): DiceElement {
        var tempNumber1 = tempNumber
        when (el.type) {
            NUMBER -> tempNumber1 = el
            DICE -> el.result.addAll(rollOne(el, 1))
            SIGNAL -> {
            }
        }
        return tempNumber1
    }

    private fun rollPreviousElement(previous: DiceElement, el: DiceElement, tempNumber: DiceElement) {
        when (previous.type) {
            NUMBER -> {
                when (el.type) {
                    DICE -> el.result.addAll(rollOne(el, tempNumber.value.toInt()))
                    SIGNAL -> previous.result.add(previous.value.toInt())
                    NUMBER -> tempNumber.value += tempNumber.value
                }
            }
            DICE, SIGNAL -> {
            }
        }
    }

    private fun rollOne(el: DiceElement, qt: Int): Collection<Int> {
        val rolls: ArrayList<Int> = ArrayList()
        var number = el.value.substring(1).toInt()
        for (i in 1..qt) {
            rolls.add(SecureRandom.getInstanceStrong().nextInt(number) + 1)
        }
        return rolls
    }

    private fun rebuild() {
        val it = this.elements.iterator()
        val fullDiceBuilder = StringBuilder()
        var previous: DiceElementType? = null
        while (it.hasNext()) {
            val el = it.next()

            if (previous != null) {
                rebuildPreviousElement(previous, el, fullDiceBuilder)
            } else {
                fullDiceBuilder.append(el.value)
            }
            previous = el.type
        }
        this.fullDice = fullDiceBuilder.toString()
    }

    private fun rebuildPreviousElement(previous: DiceElementType, el: DiceElement, fullDiceBuilder: StringBuilder) {
        when (previous) {
            DICE -> when (el.type) {
                DICE, NUMBER -> {
                    fullDiceBuilder.append("+")
                    fullDiceBuilder.append(el.value)
                }
                SIGNAL -> fullDiceBuilder.append(el.value)
            }
            NUMBER -> when (el.type) {
                DICE, SIGNAL -> fullDiceBuilder.append(el.value)
                NUMBER -> fullDiceBuilder.append(el.value)
            }
            SIGNAL -> when (el.type) {
                DICE, NUMBER -> fullDiceBuilder.append(el.value)
                SIGNAL -> {
                }
            }
        }
    }

}
