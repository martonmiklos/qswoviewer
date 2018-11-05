# QSWOViewer
SWO output viewer for the STLinkCubeProgrammer.

## Why?
Because the built in SWO viewer of the Atollic IDE SUCKS!

Yes it is the standard crap what the average Java programmers producing...
 * The trace output cannot be logged
 * On Linux I cannot copy from the trace output
 * When I stop the debugging the output get cleared
 * It cannot start automatically when the debugging is started
 
## How?
I have pulled the relevant jar files to jd-gui and decompiled it see the contents of the java folder. 

Then I have started to convert the JAVA code to a Qt based one.

For long term I would like to create a QtCreator plugin for this. Together with the bare metal debug helpers longer term it might create a more solid dev environment than the Java based shit (Atollic).

## When?
The code is in unuable state ATM, but I got it compiled. 
