package net.catchpole.B9.tools;

import net.catchpole.B9.devices.controlpad.ClassicController;
import net.catchpole.B9.devices.controlpad.ClassicControllerListener;

public class ClassicControllerTest {
    public ClassicControllerTest() {
        ClassicController classicController = new ClassicController();
        classicController.setClassicControllerListener(new ClassicControllerListener() {
            public void buttonDown(int button) {
                System.out.println("Button down " + button);
            }

            public void buttonUp(int button) {
                System.out.println("Button up " + button);
            }

            public void leftJoystick(int horizontal, int vertical) {
                System.out.println("Left joystick " + horizontal + " " + vertical);
            }

            public void rightJoystick(int horizontal, int vertical) {
                System.out.println("Right joystick " + horizontal + " " + vertical);
            }
        });
        classicController.oblivion();

    }

    public static void main(String[] args) {
        new ClassicControllerTest();
    }
}
