[1]: ../master/src/main/java/utilities/WhatsAppDriver.java

WhatsApp Automation API
======
In this project you can find the [WhatsAppDriver][1] Class.

How to use
------
```java
WhatsAppDriver whatsapp = new WhatsAppDriver(Browser.CHROME);
```
The [WhatsAppDriver][1] c'tor asks for a Browser which is an Enum. You can choose between Chrome, FireFox and PhantomJS (Which is not relevant because we need the GUI for the QR Code verfication)

```java
whatsapp.open();
```
The **open()** function opens the browser and redirects to WhatsApp Web page.

```java
whatsapp.waitForConnection();
```
The **waitForConnection()** function waits for the QR code to be scanned by a WhatsApp phone app.

From now on, we can use the more "fun" functions which pretty much speaks for themselves.
```java
whatsapp.openConvWith("Some contact or group name");
whatsapp.getCurrentConvImg();
whatsapp.getCurrentConvName();
whatsapp.getVisableMessages();
whatsapp.sendMsg("Some text");
```

## P.S. - This project is based on [Selenium](http://www.seleniumhq.org/).
