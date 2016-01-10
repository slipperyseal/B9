B9
===========

Java Robotics library for the Raspberry Pi.

B9 is a Java library to provide robotics devices support and uses the excellent [Pi4J](https://github.com/Pi4J) library.

Stuff that's in there now..

* Controller for Blue Robotics BlueESC (T100/T200) marine thrusters
* GPS parsing and integration via the Pi's onboard serial
* Spacial maths. Decimal degrees, headings - position, target and angle calculations.
* Binary motor control (forward and reverse 'tank tracks')
* Raspberry Pi camera (via the raspistill executable)
* Compass Module
* Compact Binary Symmetric Serialization (for UDP broadcast of telemetry and command packets)
* Abstract Time (system time, GPS time, simulated time for running test cases)
* Wii Classic Controller (I2C)

In development soon...

* RockBLOCK satellite communications

This project is part of the [Robotics Mission to Antarctica](http://robotics.catchpole.net/)

![Nyx2](http://robotics.catchpole.net/images/nyx2-surf.jpg "Nyx2")
