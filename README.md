# ShopTrack Pro

<p align="center">
  <strong>Offline-first Android shop management</strong>
  <br>
  Manage products, prices, inventory, and sales from your Android device.
  <br><br>
  <a href="https://github.com/shoryadevraj/ShopTrackPro/releases/latest">
    <strong>Download Latest APK</strong>
  </a>
  &nbsp;|&nbsp;
  <a href="https://github.com/shoryadevraj/ShopTrackPro/releases">
    All Releases
  </a>
</p>

## Overview

ShopTrack Pro is an Android application designed to help shopkeepers
manage everyday shop operations. It provides tools for maintaining
product catalogs, tracking stock, managing prices, recording sales, and
monitoring inventory across multiple shops.

The application is designed around local data storage, so its core shop
data can be accessed on the device without requiring a continuous
internet connection.

## Download

The latest APK is available from GitHub Releases.

[**Download ShopTrack Pro v2.2.6
(APK)**](https://github.com/shoryadevraj/ShopTrackPro/releases/download/v2.2.6/ShopTrackPro-v2.2.6.apk)

[View all
releases](https://github.com/shoryadevraj/ShopTrackPro/releases)

### Installation

1.  Open the APK download link on your Android device.
2.  Download `ShopTrackPro-v2.2.6.apk`.
3.  Open the downloaded APK.
4.  If prompted, allow your browser or file manager to install unknown
    apps.
5.  Follow the installation instructions.

Only install APKs obtained from this repository's official Releases
page.

## Features

### Shop Management

-   Manage multiple shops from one application.
-   Owner and public access modes.
-   PIN-based application access protection.
-   Maintain shop-specific inventory.

### Product Management

-   Browse and manage product catalogs.
-   Hindi and Hinglish product aliases.
-   Search and identify products.
-   Match scanned barcodes against products in the local catalog.
-   Maintain multiple price options.
-   Track product price changes.

### Inventory Management

-   Track product stock.
-   Record inventory movements.
-   Monitor available stock.
-   Identify low-stock products.
-   Maintain shop-wise inventory information.

### Sales and Reports

-   Record basic product sales.
-   View sales information and reports.
-   Review product price history.
-   Use built-in calculation utilities.

### Backup and Restore

-   Export application data in JSON format.
-   Restore previously exported JSON backups.
-   Keep local copies of business information.

## Technology Stack

  Technology          Purpose
  ------------------- ----------------------------------------
  Kotlin              Primary programming language
  Jetpack Compose     UI development
  Material 3          UI components and design
  Room                Local database management
  SQLite              Local data storage
  ZXing               Barcode scanning
  Google ML Kit       Text recognition
  Gson                JSON serialization and deserialization
  Gradle Kotlin DSL   Build configuration

## Project Configuration

  Configuration             Version
  ------------------------- ------------------------
  Android Gradle Plugin     8.6.1
  Kotlin                    2.0.21
  KSP                       2.0.21-1.0.28
  Compile SDK               35
  Minimum Android Version   Android 8.0 (API 26)
  Java                      17
  Kotlin JVM Target         17
  Application Version       2.2.6
  Version Code              27
  Application ID            `com.shoptrackpro.app`

## Requirements

To build the project, you need:

-   Android Studio
-   Android SDK 35
-   JDK 17
-   Gradle 8.7 or a compatible Gradle 8.x installation
-   An Android device or emulator running Android 8.0 or later

**Note:** This repository does not currently include the Gradle Wrapper.
A compatible Gradle installation is required to build the project.

## Getting Started

### 1. Clone the Repository

``` bash
git clone https://github.com/shoryadevraj/ShopTrackPro.git
```

### 2. Open the Project

Open the cloned `ShopTrackPro` folder in Android Studio.

Allow Android Studio to synchronize the Gradle project and download the
required dependencies.

### 3. Configure the Android SDK

Make sure Android SDK 35 and JDK 17 are installed and configured in
Android Studio.

### 4. Build the Application

To generate a debug APK using an installed Gradle distribution, run:

``` bash
gradle :app:assembleDebug
```

The generated APK should be available at:

``` text
app/build/outputs/apk/debug/app-debug.apk
```

### 5. Run the Application

Connect an Android device with USB debugging enabled or start an Android
emulator. Run the application through Android Studio.

## Release Build

To generate a signed release APK:

1.  Create an Android signing keystore.
2.  Configure your signing credentials locally.
3.  Select the release build variant.
4.  Build the release APK.

Keep your keystore and signing credentials private. Never commit signing
files, passwords, or private keys to GitHub.

If you change the signing identity, existing users may not be able to
install the updated application over an existing installation.

## Project Structure

``` text
ShopTrackPro/
│
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── java/com/shoptrackpro/app/
│           │   ├── Backup.kt
│           │   ├── Catalog.kt
│           │   ├── Database.kt
│           │   ├── MainActivity.kt
│           │   └── Security.kt
│           │
│           ├── res/
│           │   ├── drawable/
│           │   ├── drawable-nodpi/
│           │   ├── mipmap-anydpi-v26/
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── .gitignore
└── README.md
```

## Current Limitations

The following limitations apply to the current version:

-   The starter product catalog is not a complete product database.
-   Barcode matching depends on products available in the local catalog.
-   OCR currently supports Latin-script text recognition.
-   Sales are recorded through a single-product sales action.
-   Shopping cart functionality is not available.
-   GST invoices, discounts, returns, and supplier ledgers are not
    currently supported.
-   Inter-shop stock transfers and batch tracking are not available.
-   JSON backups do not include externally stored product images.
-   PIN protection should not be considered equivalent to encrypted
    database storage.
-   The application currently does not provide cloud synchronization.

## Data and Privacy

ShopTrack Pro is designed around local data storage.

Users should regularly export backups to protect their information.

**Important:** Backup files may contain sensitive shop and business
information. Do not upload personal backup JSON files or other private
data to a public repository.

## Version Information

-   **Application:** ShopTrack Pro
-   **Latest Release:** 2.2.6
-   **Version Code:** 27
-   **Package Name:** `com.shoptrackpro.app`
-   **Platform:** Android
-   **Minimum Android Version:** Android 8.0 (API 26)

## Developer

**Shorya Devraj**

-   GitHub: [@shoryadevraj](https://github.com/shoryadevraj)
-   Repository:
    [ShopTrackPro](https://github.com/shoryadevraj/ShopTrackPro)
-   Releases: [Download ShopTrack
    Pro](https://github.com/shoryadevraj/ShopTrackPro/releases)

## License

No license has been specified yet. All rights are reserved by the author
unless a license is added to this repository.
