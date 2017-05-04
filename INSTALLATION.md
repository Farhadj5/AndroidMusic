You have to use Android Studio for this.

Run on a real device
Set up your device as follows:

Connect your device to your development machine with a USB cable. If you're developing on Windows, you might need to install the appropriate USB driver for your device.

Enable USB debugging on your device by going to Settings > Developer options.
Note: On Android 4.2 and newer, Developer options is hidden by default. To make it available, go to Settings > About phone and tap Build number seven times. Return to the previous screen to find Developer options.

Run the app from Android Studio as follows:

In Android Studio, click the app module in the Project window and then select Run > Run (or click Run  in the toolbar).
In the Select Deployment Target window, select your device, and click OK.
Android Studio installs the app on your connected device and starts it.

Run on an emulator
Before you run your app on an emulator, you need to create an Android Virtual Device (AVD) definition. An AVD definition specifies the characteristics of an Android phone device that you want to simulate in the Android Emulator.

Create an AVD Definition as follows:

Launch the Android Virtual Device Manager by selecting Tools > Android > AVD Manager, or by clicking the AVD Manager icon  in the toolbar.
In the Your Virtual Devices screen, click Create Virtual Device.
In the Select Hardware screen, select a phone device, such as Nexus 5, and then click Next.
In the System Image screen, click Download for one of the recommended system images. Agree to the terms to complete the download.
After the download is complete, select the system image from the list and click Next.
On the next screen, leave all the configuration settings as they are and click Finish.
Back in the Your Virtual Devices screen, select the device you just created and click Launch this AVD in the emulator  .
While the emulator starts up, close the Android Virtual Device Manager window and return to your project so you can run the app:

Once the emulator is booted up, click the app module in the Project window and then select Run > Run (or click Run  in the toolbar).
In the Select Deployment Target window, select the emulator and click OK.
Android Studio installs the app on the emulator and starts it.


