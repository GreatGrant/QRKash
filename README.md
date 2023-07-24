# QRKash

QRKash is a mobile wallet app that enables users to receive and send payments powered by QR code technology. This app was developed during the Financial Services Innovators (FSI) Cashless Campus Innovation Challenge (a hackathon).

![QRKash App Demo](link-to-demo-gif-or-screenshot)

## Features

- Receive and send payments using QR code technology
- Create virtual accounts for seamless transactions
- View transaction history
- And more!

## Technologies Used

- Kotlin
- Android Jetpack (ViewModel, LiveData, Room, Navigation)
- Material Design
-  Firebase Firestore
- Retrofit for API communication
- OkHttp for networking
- ML Kit Barcode Scanning for QR code recognition
- ZXing library for generating QR codes
- Glide for image loading

## Getting Started

To run QRKash on your local machine, follow these steps:

1. Clone the repository:

```bash
git clone https://github.com/YourUsername/QRKash.git
```

2. Open the project in Android Studio.
3. Build and run the app on your Android device or emulator.

## API Configuration
QRKash relies on the Flutterwave API for virtual account creation and payment transfers. To use the app with the API, you need to obtain an API key from Flutterwave and configure it in the BuildConfig.API_KEY field in the build.gradle file.

```bash
buildConfigField "String", "API_KEY", "\"your_flutterwave_api_key_here\""
```

## Screenshots

## License
This project is licensed under the MIT License.

## Acknowledgments
Thanks to Financial Services Innovators (FSI) for organizing the hackathon that inspired this app.
Special thanks to all contributors and open-source libraries used in this project.
