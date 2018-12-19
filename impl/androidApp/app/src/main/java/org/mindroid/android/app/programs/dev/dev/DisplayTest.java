package org.mindroid.android.app.programs.dev.dev;

import org.mindroid.api.ImperativeWorkshopAPI;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.impl.brick.Button;
import org.mindroid.impl.brick.Textsize;

public class DisplayTest extends ImperativeWorkshopAPI {

	public DisplayTest(){
		super("Display Test", -1);
	}

	@Override
		public void run() {
			clearDisplay();
			drawString("Middle Column");
			delay(1000);
			drawString("1st Column", 1);
			drawString("2nd Column", 2);
			drawString("3rd Column", 3);

			for (int i=4; i<9; i++) {
				drawString("Zeile " + i, i);
			}
			delay(1000);
			drawString("Blabla", 99);


	}
}