package br.com.my;

public class DiceControlMain {

	public static void main(String[] args) {

		DiceControl dc = new DiceControl();

		// "d2+d3-1"
		dc.addDice("d2");
		dc.addNumber("2");
		dc.addDice("d3");
		dc.addSignal("-");
		dc.addNumber("1");

	}

}
