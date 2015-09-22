package net.catchpole.B9.tools;

import net.catchpole.B9.devices.controlpad.ClassicController;
import net.catchpole.B9.devices.controlpad.ClassicControllerListener;
import net.catchpole.B9.devices.thrusters.PiBinaryThrusters;

// control your binary thruster robot with the Wii Classic Controller
public class WiiCar {
    public WiiCar() throws Exception {
        ClassicController classicController = new ClassicController();
        classicController.setClassicControllerListener(new ClassicControllerListener() {
            private PiBinaryThrusters piBinaryThrusters = new PiBinaryThrusters();
            private double left;
            private double right;

            public void buttonDown(int button) {
                if (button == 10) {
                    piBinaryThrusters.update(0, right);
                }
                if (button == 15) {
                    piBinaryThrusters.update(left, 0);
                }
            }

            public void buttonUp(int button) {
                if (button == 10 || button == 15) {
                    piBinaryThrusters.update(left, right);
                }
            }

            public void leftJoystick(int horizontal, int vertical) {
                if (vertical > 10) {
                    this.update(1, 1);
                } else if (vertical < -10) {
                    this.update(-1, -1);
                } else if (horizontal < -10) {
                    this.update(1, -1);
                } else if (horizontal > 10) {
                    this.update(-1, 1);
                } else {
                    this.update(0, 0);
                }
            }

            private void update(double left, double right) {
                this.left = left;
                this.right = right;
                piBinaryThrusters.update(left, right);
            }

            public void rightJoystick(int horizontal, int vertical) {
            }
        });
        classicController.oblivion();
    }

    public static void main(String[] args) throws Exception {
        new WiiCar();
    }
}