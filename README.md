B9
===========

Java Robotics library for the Raspberry Pi.

B9 is a Java library to provide robotics devices support and uses the excellent [Pi4J](https://github.com/Pi4J) library.

This project is part of the [Robotics Mission to Antarctica](http://robotics.catchpole.net/)

![Nyx2](http://robotics.catchpole.net/images/nyx2-surf.jpg "Nyx2")

B9 is split into two modules:

* B9-core
* B9-pi

B9-pi contains classes which have a direct dependency on Pi4J, while the B9-core can be built and run in any environment.

B9 currently supports:

* Controller for Blue Robotics BlueESC (T100/T200) marine thrusters
* GPS parsing and integration via the Pi's onboard serial
* HMC5883L Compass Module
* Spacial maths. Decimal degrees, headings - position, target and angle calculations.
* Binary motor control (forward and reverse 'tank tracks')
* Compact Binary Symmetric Serialization - for broadcast and logging of telemetry and command data
* UDP transceivers for telemetry and command data
* Abstract Time (system time, GPS time, simulated time for running test cases)
* Wii Classic Controller (I2C)
* Raspberry Pi camera (via the raspistill and raspivid executable)

In development:

* [RockBLOCK](http://www.rock7mobile.com/products-rockblock) satellite communications
