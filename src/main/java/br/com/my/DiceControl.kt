package br.com.my

import br.com.my.DiceControl.DiceElementType.*
import java.util.Stack
import kotlin.collections.ArrayList
import kotlin.random.Random

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
        var rolled: Boolean = false

        override fun toString(): String {
            return "DiceElement [type=" + this.type + ", value=" + this.value + ", result=" + this.result + ", roll=" + roll + "]"
        }

        fun clear() {
            result.clear()
        }

        fun roll() {
            roll(1)
        }

        fun roll(qt: Int) {
            when (this.type) {
                NUMBER -> this.result.add(this.value.toInt())
                DICE -> result.addAll(rollOne(qt))
                SIGNAL -> {
                }
            }
            this.rolled = true
        }

        private fun rollOne(qt: Int): Collection<Int> {
            val rolls: ArrayList<Int> = ArrayList()
            val number = this.value.substring(1).toInt()
            for (i in 1..qt) {
                rolls.add(random.nextInt(1, number + 1))
            }
            return rolls
        }

        companion object {

           val random = Random

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
        if (!elements.isEmpty()) {
            this.elements.pop()
            this.rebuild()
        }
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
    fun roll(): Int {
        if (elements.empty()) {
            return -1
        }
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
        if (!previous!!.rolled) {
            rollCurrentElement(previous!!)
        }

        return rolled()
    }

    private fun rolled(): Int {
        var roll = 0
        this.elementsToRoll.forEach { it -> it.result.forEach { t -> roll += t } }
        return roll
    }

    private fun rollCurrentElement(el: DiceElement) {
        when (el.type) {
            NUMBER -> el.roll()
            DICE -> el.roll(1)
            SIGNAL -> {
            }
        }
    }

    private fun rollPreviousElement(previous: DiceElement, el: DiceElement) {
        when (previous.type) {
            NUMBER -> {
                when (el.type) {
                    DICE -> {
                        el.roll(previous.value.toInt())
                        el.rolled = true
                    }
                    SIGNAL -> {
                        if (previous.roll) {
                            previous.result.add(previous.value.toInt())
                            previous.rolled = true
                        }
                    }
                    NUMBER -> {
                    }
                }
            }
            DICE -> {
                when (el.type) {
                    DICE -> {
                        el.roll(1)
                        el.rolled = true
                    }
                    SIGNAL -> {
                    }
                    NUMBER -> {
                    }
                }
            }
            SIGNAL -> {
            }
        }
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
                        tempNumber1.roll = el.type == SIGNAL
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

    /**
     * 
     */
    fun clear() {
        this.elements.clear()
        this.elementsToRoll.clear()
        this.rebuild()
    }
}

