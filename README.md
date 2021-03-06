# Canary

![Canary Icon](https://raw.githubusercontent.com/csci427/canary/master/app/src/main/res/mipmap-xhdpi/ic_launcher.png)

This project utilizes the [Open-Source Integrated Clinical Environment](https://www.openice.info/) to silence music in operating rooms during life critical events. A medical professional sets an approrpate window for any input data stream. Music will be silenced if a reading comes in outside of this window. For example, if a patients heart rate is below 30 bpm or goes above 120 bpm. 

A user must create an [OpenICE adaptor](https://www.openice.info/docs/4_device-adapter-setup.html) to convert proprietary data from an existing patient monitor into ICE format. Canary then uses [rti DDS](https://www.rti.com/products/dds/) to handle distribution of streaming data. A "NO DATA" alert will be raised if Canary has been set to listen, but no incoming data is detected.

Icon from [sweetclipart](http://sweetclipart.com/cute-yellow-canary-bird-1987) licenced under [ Attribution-NonCommercial-ShareAlike 3.0 Unported](http://creativecommons.org/licenses/by-nc-sa/3.0/) licence

Canary is released under the [apache license](http://www.apache.org/licenses/LICENSE-2.0)
