# Food Ordering And Tracking App

## Prerequisites

Before running this Android project, ensure that you have the following:

- **Java Development Kit (JDK)**: Version 11 or higher.
- **Android Studio**: Latest version recommended.
    - You can download Android Studio from [here](https://developer.android.com/studio).
- **Android SDK**: Installed via Android Studio.
- **USB Cable**: To connect your Android device to the development machine.

## Installation

1. **Install Android Studio**

   If you have not installed Android Studio:
    - Download Android Studio from [the official website](https://developer.android.com/studio).
    - Follow the installation instructions for your operating system.
    - During installation, make sure to install the Android SDK and any recommended components.

2. **Clone the Repository**

   Clone the project to your local machine using Git:
   ```bash
   git clone https://github.com/leokwsw/FoodOrderingAndTrackingApp.git
   ```

3. **Open in Android Studio**

    - Open Android Studio.
    - Select `Open an existing project`.
    - Browse to the directory where the project is cloned and select it.

4. **Sync Project with Gradle**

    - Once the project is loaded in Android Studio, you may need to sync it with Gradle.
      Click `Sync Now` if prompted.

5. **Connect Your Android Device**

    - Connect your Android phone to your computer using a USB cable.
    - Make sure USB Debugging is enabled on your phone. You can enable it in:
        - **Settings > Developer options > USB Debugging**.

6. **Select Device**

    - In Android Studio, select your connected Android device from the list of available devices.
      Make sure the device is recognized by Android Studio.

## Running the Project

1. **Build the Project**

    - Click on the `Build` menu and then select `Rebuild Project` to make sure there are no build
      issues.

2. **Run the App**

    - Click on the green `Run` button or use the shortcut `Shift + F10`.
    - Select your connected device from the prompt if necessary.

3. **Install APK on Device**

   If you prefer to install the APK directly:
    - Open a terminal in the project root directory.
    - Run the following command to generate the APK:
      ```bash
      ./gradlew assembleDebug
      ```
    - Navigate to `app/build/outputs/apk/debug` to find the `app-debug.apk` file.
    - Copy the APK to your Android device and install it.

## Troubleshooting

- **Device Not Recognized**: Ensure USB Debugging is enabled on your device and that the appropriate
  drivers are installed on your computer.
- **Build Issues**: If you encounter build issues, try running `Invalidate Caches / Restart` in
  Android Studio.
- **Gradle Sync Issues**: Make sure your internet connection is active and that the project is
  configured with the latest Gradle version.

## Additional Tips

- **Using an Emulator**: You can use an Android Emulator if you don't have a physical device.
- **Permissions**: Ensure that the app has the necessary permissions to run correctly on your
  Android device.