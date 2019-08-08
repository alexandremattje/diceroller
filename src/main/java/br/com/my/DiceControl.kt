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
    private lateinit var elementsToRoll: Stack<DiceControl.DiceElement>

    private enum class DiceElementType {
        NUMBER, DICE, SIGNAL
    }

    override fun toString(): String {
        var s: String = fullDice
        val it = this.elementsToRoll.iterator()
        while (it.hasNext()) {
            val el = it.next()
            if (el.result.size > 0) {
                s += el.result
            }
        }

        return s
    }

    private class DiceElement constructor(val type: DiceElementType, var value: String) {
        var result: ArrayList<Int> = ArrayList()
        var roll: Boolean = true

        override fun toString(): String {
            return "DiceElement [type=" + this.type + ", value=" + this.value + ", result=" + this.result + ", roll=" + roll + "]"
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
        val it = this.elementsToRoll.iterator()
        var previous: DiceElement? = null
        while (it.hasNext()) {
            val el = it.next()
            if (previous != null) {
                rollPreviousElement(previous, el)
            } else {
                if (el.roll) {
                    rollCurrentElement(el)
                }
            }
            previous = el
        }
    }

    private fun rollCurrentElement(el: DiceElement) {
        when (el.type) {
            NUMBER -> el.result.add(el.value.toInt())
            DICE -> el.result.addAll(rollOne(el, 1))
            SIGNAL -> {
            }
        }
    }

    private fun rollPreviousElement(previous: DiceElement, el: DiceElement) {
        when (previous.type) {
            NUMBER -> {
                when (el.type) {
                    DICE -> el.result.addAll(rollOne(el, previous.value.toInt()))
                    SIGNAL -> {
                        if (previous.roll) {
                            previous.result.add(previous.value.toInt())
                        }
                    }
                    NUMBER -> {
                    }
                }
            }
            DICE -> {
            }
            SIGNAL -> {
                if (el.roll && el.type == NUMBER) {
                    el.result.add(el.value.toInt())
                }
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
        this.elementsToRoll = Stack()
        val it = this.elements.iterator()
        val fullDiceBuilder = StringBuilder()
        var previous: DiceElementType? = null
        var tempNumber: DiceElement? = null
        while (it.hasNext()) {
            val el = it.next()

            if (previous != null) {
                tempNumber = rebuildPreviousElement(previous, el, fullDiceBuilder, tempNumber)
            } else {
                if (el.type == NUMBER) {
                    tempNumber = DiceElement.newNumber("")
                    tempNumber.value = el.value
                } else {
                    this.elementsToRoll.push(el)
                }
                fullDiceBuilder.append(el.value)
            }
            previous = el.type
        }
        if (tempNumber != null) {
            this.elementsToRoll.push(tempNumber)
        }

        this.fullDice = fullDiceBuilder.toString()
    }

    private fun rebuildPreviousElement(previous: DiceElementType, el: DiceElement, fullDiceBuilder: StringBuilder, tempNumber: DiceElement?): DiceElement? {
        var tempNumber1 = tempNumber
        when (previous) {
            DICE -> when (el.type) {
                DICE, NUMBER -> {
                    fullDiceBuilder.append("+")
                    fullDiceBuilder.append(el.value)
                    this.elementsToRoll.push(el)
                }
                SIGNAL -> {
                    fullDiceBuilder.append(el.value)
                    this.elementsToRoll.push(el)
                }
            }
            NUMBER -> when (el.type) {
                DICE, SIGNAL -> {
                    if (tempNumber1 != null) {
                        // fullDiceBuilder.append(tempNumber1.value)
                        tempNumber1.roll = false
                        this.elementsToRoll.push(tempNumber1)
                        tempNumber1 = null
                    }
                    fullDiceBuilder.append(el.value)
                    this.elementsToRoll.push(el)
                }
                NUMBER -> {
                    if (tempNumber1 == null) {
                        tempNumber1 = DiceElement.newNumber("")
                    }
                    tempNumber1.value += el.value
                    fullDiceBuilder.append(el.value)
                }
            }
            SIGNAL -> when (el.type) {
                DICE -> {
                    fullDiceBuilder.append(el.value)
                    this.elementsToRoll.push(el)
                }
                NUMBER -> {
                    if (tempNumber1 == null) {
                        tempNumber1 = DiceElement.newNumber("")
                        tempNumber1.value += el.value
                    }
                    fullDiceBuilder.append(el.value)
                }
                SIGNAL -> {
                }
            }
        }
        return tempNumber1
    }
}

