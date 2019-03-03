# orchard
Domotic orchard, arduino + java!

### RXTX java lib

Issues trying to download and execute using gradle. WIth dependency from maven central, this happens:

```
java.lang.UnsatisfiedLinkError: no rxtxSerial in java.library.path thrown while loading gnu.io.RXTXCommDriver
Exception in thread "main" java.lang.UnsatisfiedLinkError: no rxtxSerial in java.library.path
        at java.lang.ClassLoader.loadLibrary(ClassLoader.java:1867)
        at java.lang.Runtime.loadLibrary0(Runtime.java:870)
        at java.lang.System.loadLibrary(System.java:1122)
        at gnu.io.CommPortIdentifier.<clinit>(CommPortIdentifier.java:83)
        at com.edu.orchard.ArduinoConnection.<init>(ArduinoConnection.java:25)
        at com.edu.orchard.OrchardApplication.main(OrchardApplication.java:11)
```
I have to download with apt and say to gradle the directory:

```
sudo apt-get install librxtx-java
```
and then 

```
compile files('/usr/share/java/RXTXcomm.jar')
```
This is needed beacause version downloaded from maven central and with apt are not the same, and this error is shown:

```
Stable Library
=========================================
Native lib Version = RXTX-2.2pre2
Java lib Version   = RXTX-2.1-7
WARNING:  RXTX Version mismatch
        Jar version = RXTX-2.1-7
        native lib Version = RXTX-2.2pre2

```