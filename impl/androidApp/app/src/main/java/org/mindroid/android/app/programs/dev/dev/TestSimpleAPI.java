package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.SimpleAPI;

public class TestSimpleAPI extends SimpleAPI {

	public TestSimpleAPI(){
		super("Display Test", -1);
	}

	@Override
		public void run() {
			displayColors();
			delay(1000);
			clearDisplay();
			displayDistance();
			delay(1000);
			clearDisplay();

			waitForMessage();
			playSingleBeep();
			waitForMessage("GO");
			playDoubleBeep();

			driveToWall();
			delay(1000);

			turnAround();
			delay(1000);

			driveToWallAndTurn();

	}
}