# QRKash

QRKash is a mobile wallet app that enables users to receive and send payments powered by QR code technology. This app was developed during the Financial Services Innovators (FSI) Cashless Campus Innovation Challenge (a hackathon).

![QRSPlash](https://github.com/GreatGrant/QRKash/assets/62026220/7cfe2177-0a8f-4ec5-897e-3fcde622c21e)


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
![1](https://github.com/GreatGrant/QRKash/assets/62026220/093491b8-b63f-4a10-b465-ae2ccdb7aae1)

![20](https://github.com/GreatGrant/QRKash/assets/62026220/6bb4cf4c-8588-41d0-84ac-46363fac79d3)

![2](https://github.com/GreatGrant/QRKash/assets/62026220/1c95ecd8-1e37-4549-86b7-cdd7fd7130d8)

![3](https://github.com/GreatGrant/QRKash/assets/62026220/b273b623-8c79-4f2c-b3ab-57ff247caff2)

![4](https://github.com/GreatGrant/QRKash/assets/62026220/d1e9455a-c040-4246-98ff-bee49374ff68)

![5](https://github.com/GreatGrant/QRKash/assets/62026220/e5ca3812-7ca2-4fc7-8291-ea15f33e468f)




## License
This project is licensed under the MIT License.

## Acknowledgments
Thanks to Financial Services Innovators (FSI) for organizing the hackathon that inspired this app.
Special thanks to all contributors and open-source libraries used in this project.
