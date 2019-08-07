package br.com.my

import java.security.SecureRandom
import java.util.Stack
import java.util.concurrent.ThreadLocalRandom

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
                return DiceElement(DiceElementType.DICE, dice)
            }

            fun newSignal(signal: String): DiceElement {
                return DiceElement(DiceElementType.SIGNAL, signal)
            }

            fun newNumber(number: String): DiceElement {
                return DiceElement(DiceElementType.NUMBER, number)
            }
        }

    }

    fun addDice(dice: String) {
        this.elements.push(DiceElement.newDice(dice))
        this.rebuild()
    }

    fun addSignal(signal: String) {
        this.elements.push(DiceElement.newSignal(signal))
        this.rebuild()
    }

    fun addNumber(number: String) {
        this.elements.push(DiceElement.newNumber(number))
        this.rebuild()
    }

    fun removeLast() {
        this.elements.pop()
        this.rebuild()
    }

    fun clearResults(){
        val it = this.elements.iterator()
        while (it.hasNext()) {
            val el = it.next()
            el.clear()

        }
    }

    fun roll() {
        clearResults()
        val it = this.elements.iterator()
        var previous: DiceElement? = null
        var tempNumber: DiceElement = DiceElement.newNumber("0")
        tempNumber.result.add(0)
        while (it.hasNext()) {
            val el = it.next()
            if (previous != null) {
                when (previous.type) {
                    DiceElementType.NUMBER -> {
                        when (el.type) {
                            DiceElementType.DICE -> el.result.addAll(rollOne(el, tempNumber.value.toInt()))
                            DiceElementType.SIGNAL -> previous.result.add(previous.value.toInt())
                            DiceElementType.NUMBER -> tempNumber.value += tempNumber.value
                        }
                    }
                }
            } else {
                when (el.type) {
                    DiceElementType.NUMBER -> tempNumber = el
                    DiceElementType.DICE -> el.result.addAll(rollOne(el, 1))
                    else -> {
                    }
                }
            }
            previous = el
        }
    }

    private fun rollOne(el: DiceElement, qt: Int): Collection<Int> {
        val rolls : ArrayList<Int> = ArrayList()
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
                when (previous) {
                    DiceControl.DiceElementType.DICE -> when (el.type) {
                        DiceControl.DiceElementType.DICE, DiceControl.DiceElementType.NUMBER -> {
                            fullDiceBuilder.append("+")
                            fullDiceBuilder.append(el.value)
                        }
                        DiceControl.DiceElementType.SIGNAL -> fullDiceBuilder.append(el.value)
                    }
                    DiceControl.DiceElementType.NUMBER -> when (el.type) {
                        DiceControl.DiceElementType.DICE, DiceControl.DiceElementType.SIGNAL -> fullDiceBuilder.append(el.value)
                        DiceControl.DiceElementType.NUMBER -> fullDiceBuilder.append(el.value)
                    }
                    DiceControl.DiceElementType.SIGNAL -> when (el.type) {
                        DiceControl.DiceElementType.DICE, DiceControl.DiceElementType.NUMBER -> fullDiceBuilder.append(el.value)
                        DiceControl.DiceElementType.SIGNAL -> {
                        }
                    }
                }
            } else {
                fullDiceBuilder.append(el.value)
            }
            previous = el.type
        }
        this.fullDice = fullDiceBuilder.toString()
    }

}
