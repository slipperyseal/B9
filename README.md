B9
===========

# Java Robotics library for the Raspberry Pi.

B9 is a Java library which provides robotics devices support and other cool stuff for the Raspberry Pi.

![Nyx](https://storage.googleapis.com/kyoto.catchpole.net/nyx-float1.jpg "Nyx")

Devices supported:

- [Blue Robotics BlueESC](https://www.bluerobotics.com/store/thrusters/t100-thruster/) T100/T200 Marine Thrusters
- [Blue Robotics BasicESC](https://www.bluerobotics.com/store/electronics/besc30-r3/) (or similar) via a [PCA9685 PWM servo driver](https://www.adafruit.com/product/815)
- [RockBLOCK](http://www.rock7mobile.com/products-rockblock) Iridium 9602 Satellite Modem
- GPS parsing and integration via the Pi's onboard serial
- HMC5883L Compass Module (I2C)
- Wii Classic Controller (I2C)
- Raspberry Pi camera - via the raspistill and raspivid executable
- Binary Motor Control - forward and reverse 'tank tracks' via GPIO

*Update: Blue Robotics have suspended production of the BlueESC (internal and external) due to failure issues. The BlueESC should not be used in mission critical situations.*

Library features:

- Spacial maths, decimal degrees, headings, position, target and angle calculations.
- Compact Binary Symmetric Serialization - for broadcast and logging of telemetry and command data
- UDP transceivers for telemetry and command data
- Abstract System Time - supports simulated time for running simulations in non-realtime

B9 uses the excellent [Pi4J](https://github.com/Pi4J) library.

# Getting Started

The net.catchpole.B9.tools package contains utilities and tools to get you started.

To use B9, begin by [installing Pi4J](http://pi4j.com/install.html#EasyPreferred) on your Raspberry Pi.
Pi4J will automatically link in it's native libraries, so it's important Pi4J is actually installed rather than just trying to use the jar files on their own.

Check out B9 either on your Raspberry Pi or on another machine which has a Java 8 installed and Maven. Change directory into B9 and then build the project...

        mvn clean install
        
The build process will create the B9 jar and copy all the dependency jars into the 'target' directory.
Copy the jar files onto the pi if you've built this on another machine. Put them in a directory such as 'target'.
If you've built the project on the pi you should be able to run one of the tools using a command such as this...

        java -cp "target/*" net.catchpole.B9.tools.HMC5883LCompassTest

Some GPIO device access may require 'sudo' root access. Raspbian seems to be allowing more of these to be accessed by non-root users.
There are also ways to add devices to the "non-root" list.

Also, by default, many devices such as serial and I2C are disabled in Raspbian.
You'll need to enabled them as needed. The serial port is often allocated to a TTY session.
There are various ways to disable this depending on the Raspbian version you re using.

From here you can make your own maven project and include B9 as a dependency. You could use the same maven copy-dependencies goal.
