B9
===========

Java Robotics library for the Raspberry Pi.

B9 is a Java library to provide robotics devices support and uses the excellent [Pi4J](https://github.com/Pi4J) library.

It is split into two modules:

  B9-core
  B9-pi

B9-pi contains classes which have a direct dependency on Pi4J, while the B9-core can be built and run in any environment.

Stuff that's in there now..

* Controller for Blue Robotics BlueESC (T100/T200) marine thrusters
* GPS parsing and integration via the Pi's onboard serial
* Spacial maths. Decimal degrees, headings - position, target and angle calculations.
* Binary motor control (forward and reverse 'tank tracks')
* HMC5883L Compass Module
* Compact Binary Symmetric Serialization (for UDP broadcast of telemetry and command packets)
* Abstract Time (system time, GPS time, simulated time for running test cases)
* Wii Classic Controller (I2C)
* Raspberry Pi camera (via the raspistill executable)

In development...

* RockBLOCK satellite communications

This project is part of the [Robotics Mission to Antarctica](http://robotics.catchpole.net/)

![Nyx2](http://robotics.catchpole.net/images/nyx2-surf.jpg "Nyx2")
