package br.com.my

import java.util.Stack

class DiceControl {

    var fullDice: String = ""
    private val elements = Stack<DiceControl.DiceElement>()

    private enum class DiceElementType {
        NUMBER, DICE, SIGNAL
    }

    private class DiceElement constructor(val type: DiceElementType, val value: String) {

        override fun toString(): String {
            return "DiceElement [type=" + this.type + ", value=" + this.value + "]"
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
