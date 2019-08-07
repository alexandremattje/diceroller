package br.com.my;

import java.util.Iterator;
import java.util.Stack;

public class DiceControl {

    private enum DiceElementType {
        NUMBER, DICE, SIGNAL
    }

    private static class DiceElement {

        private DiceElementType type;
        private String value;

        protected DiceElement(DiceElementType type, String value) {
            this.type = type;
            this.value = value;
        }

        protected static DiceElement newDice(String dice) {
            return new DiceElement(DiceElementType.DICE, dice);
        }

        public static DiceElement newSignal(String signal) {
            return new DiceElement(DiceElementType.SIGNAL, signal);
        }

        public static DiceElement newNumber(String number) {
            return new DiceElement(DiceElementType.NUMBER, number);
        }

        @Override
        public String toString() {
            return "DiceElement [type=" + this.type + ", value=" + this.value + "]";
        }

    }

    public String fullDice;
    private Stack<DiceElement> elements = new Stack<DiceControl.DiceElement>();

    public void addDice(String dice) {
        this.elements.push(DiceElement.newDice(dice));
        this.rebuild();
    }

    public void addSignal(String signal) {
        this.elements.push(DiceElement.newSignal(signal));
        this.rebuild();
    }

    public void addNumber(String number) {
        this.elements.push(DiceElement.newNumber(number));
        this.rebuild();
    }

    public void removeLast() {
        this.elements.pop();
        this.rebuild();
    }

    private void rebuild() {
        Iterator<DiceElement> it = this.elements.iterator();
        StringBuilder fullDiceBuilder = new StringBuilder();
        DiceElementType previous = null;
        while (it.hasNext()) {
            DiceElement el = it.next();

            if (previous != null) {
                switch (previous) {
                    case DICE:
                        switch (el.type) {
                            case DICE:
                            case NUMBER:
                                fullDiceBuilder.append("+");
                                fullDiceBuilder.append(el.value);
                                break;
                            case SIGNAL:
                                fullDiceBuilder.append(el.value);
                                break;
                            default:
                                break;
                        }
                        break;
                    case NUMBER:
                        switch (el.type) {
                            case DICE:
                            case SIGNAL:
                                fullDiceBuilder.append(el.value);
                                break;
                            case NUMBER:
                                fullDiceBuilder.append(el.value);
                                break;
                            default:
                                break;
                        }
                        break;
                    case SIGNAL:
                        switch (el.type) {
                            case DICE:
                            case NUMBER:
                                fullDiceBuilder.append(el.value);
                                break;
                            case SIGNAL:
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            } else {
                fullDiceBuilder.append(el.value);
            }
            previous = el.type;
        }
        this.fullDice = fullDiceBuilder.toString();


    }

}
