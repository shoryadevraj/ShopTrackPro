# ShopTrack Pro

<p align="center">
  <strong>Offline-first Android Shop Management</strong>
  <br>
  Manage products, prices, inventory, and sales from your Android device.
  <br><br>
  <a href="https://github.com/shoryadevraj/ShopTrackPro/releases/latest">
    <img src="https://img.shields.io/github/v/release/shoryadevraj/ShopTrackPro?style=for-the-badge&color=2ea44f" alt="Latest Release">
  </a>
  <a href="https://github.com/shoryadevraj/ShopTrackPro">
    <img src="https://img.shields.io/github/stars/shoryadevraj/ShopTrackPro?style=for-the-badge" alt="GitHub Stars">
  </a>
  <a href="https://github.com/shoryadevraj/ShopTrackPro/releases">
    <img src="https://img.shields.io/github/downloads/shoryadevraj/ShopTrackPro/total?style=for-the-badge" alt="Downloads">
  </a>
  <br><br>
  <a href="https://github.com/shoryadevraj/ShopTrackPro/releases/download/v2.3.0/ShopTrackPro-v2.3.0.apk">
    <strong>DOWNLOAD LATEST APK</strong>
  </a>
  &nbsp; | &nbsp;
  <a href="https://github.com/shoryadevraj/ShopTrackPro/releases">
    All Releases
  </a>
</p>

---

## Overview

ShopTrack Pro is an Android application designed to simplify everyday shop management.

It helps shopkeepers manage products, maintain prices, track inventory, monitor stock movements, and record sales from a single application.

Built with Kotlin and Jetpack Compose, ShopTrack Pro uses local data storage to keep essential shop information accessible without requiring a continuous internet connection.

## Latest Release

<details open>
<summary><strong>ShopTrack Pro v2.3.0</strong></summary>

**Release Version:** 2.3.0  
**Version Code:** 28  
**Minimum Android Version:** Android 8.0 (API 26)

### What's New in v2.3.0

- Fixed notifications remaining visible across different screens.
- Notifications now automatically disappear after a short duration.
- Improved category recommendations using the existing product catalog.
- Improved barcode assignment for existing products.
- Scanning a barcode to assign it to an existing product no longer creates an unnecessary duplicate product.
- Added barcode-based product search in customer-only mode.
- Added the ability to duplicate existing product details when creating new product variations.
- Added measurement unit selection with common units and custom unit support.
- Improved product search to include company and brand names.

### Download

<a href="https://github.com/shoryadevraj/ShopTrackPro/releases/download/v2.3.0/ShopTrackPro-v2.3.0.apk">
  <strong>Download ShopTrack Pro v2.3.0 APK</strong>
</a>

<a href="https://github.com/shoryadevraj/ShopTrackPro/releases/tag/v2.3.0">
  View Release Notes
</a>

</details>

## Features

### 1. Shop Management

- Manage multiple shops from one application.
- Owner and public access modes.
- PIN-based application access protection.
- Maintain separate inventory for different shops.
- Manage shop-specific product information.

### 2. Product Management

- Create, edit, and manage products.
- Maintain product categories.
- Product catalog with Hindi and Hinglish aliases.
- Search products by name.
- Search products using company or brand names.
- Search products using available aliases.
- Duplicate existing product details to create new product variations.
- Maintain different prices for products.
- Assign and manage product barcodes.
- Support for common measurement units and custom units.

### 3. Barcode Management

- Scan product barcodes.
- Assign barcodes to existing products.
- Search products using barcodes.
- Identify products using their registered barcode.
- Barcode-based product lookup in customer-only mode.
- Match scanned barcodes against the local product catalog.

### 4. Inventory Management

- Track available product stock.
- Record inventory movements.
- Monitor stock levels.
- Identify low-stock products.
- Maintain shop-wise inventory information.
- Manage product quantities using measurement units.

### 5. Sales and Reports

- Record basic product sales.
- View sales information.
- Monitor product price changes.
- Maintain product price history.
- Use built-in calculation utilities.

### 6. Backup and Restore

- Export application data in JSON format.
- Restore previously exported JSON backups.
- Maintain local copies of business information.
- Protect important business records through regular backups.

## Screenshots

<!-- Add application screenshots here -->

<!--
<p align="center">
  <img src="screenshots/home.png" width="220" alt="Home Screen">
  <img src="screenshots/products.png" width="220" alt="Products Screen">
  <img src="screenshots/inventory.png" width="220" alt="Inventory Screen">
</p>
-->

## Technology Stack

| Technology | Purpose |
|---|---|
| Kotlin | Primary programming language |
| Jetpack Compose | UI development |
| Material 3 | UI components and design |
| Room | Local database management |
| SQLite | Local data storage |
| ZXing | Barcode scanning |
| Google ML Kit | Text recognition |
| Gson | JSON serialization and deserialization |
| Gradle Kotlin DSL | Build configuration |

## Project Configuration

| Configuration | Version |
|---|---|
| Android Gradle Plugin | 8.6.1 |
| Kotlin | 2.0.21 |
| KSP | 2.0.21-1.0.28 |
| Compile SDK | 35 |
| Minimum Android Version | Android 8.0 (API 26) |
| Java | 17 |
| Kotlin JVM Target | 17 |
| Application Version | 2.3.0 |
| Version Code | 28 |
| Application ID | `com.shoptrackpro.app` |

## Installation

### Option 1: Download from GitHub Releases

1. Open the [ShopTrack Pro Releases](https://github.com/shoryadevraj/ShopTrackPro/releases) page.
2. Select the latest release.
3. Download the APK file.
4. Open the downloaded APK on your Android device.
5. Allow installation from unknown sources if prompted.
6. Complete the installation.

### Option 2: Direct APK Download

<a href="https://github.com/shoryadevraj/ShopTrackPro/releases/download/v2.3.0/ShopTrackPro-v2.3.0.apk">
  Download ShopTrack Pro v2.3.0
</a>

### Requirements

- Android 8.0 or later.
- Sufficient storage space for installation.
- Permission to install applications from the selected source.

## Getting Started

### Requirements for Development

To build the project, you need:

- Android Studio.
- Android SDK 35.
- JDK 17.
- Gradle 8.7 or a compatible Gradle 8.x installation.
- An Android device or emulator running Android 8.0 or later.

**Note:** This repository does not currently include the Gradle Wrapper. A compatible Gradle installation is required to build the project.

### 1. Clone the Repository

```bash
git clone https://github.com/shoryadevraj/ShopTrackPro.git
```

### 2. Open the Project

Open the cloned `ShopTrackPro` folder in Android Studio.

Allow Android Studio to synchronize the Gradle project and download the required dependencies.

### 3. Configure Android SDK

Make sure Android SDK 35 and JDK 17 are installed and configured.

### 4. Build the Application

Run the following command using an installed Gradle distribution:

```bash
gradle :app:assembleDebug
```

The generated debug APK should be available at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### 5. Run the Application

Connect an Android device with USB debugging enabled or start an Android emulator.

Run the application through Android Studio.

## Release Build

To generate a signed release APK:

1. Create an Android signing keystore.
2. Configure your signing credentials locally.
3. Select the release build variant.
4. Build the release APK.

Keep your keystore and signing credentials private.

Never commit signing files, passwords, or private keys to GitHub.

If you change the signing identity, existing users may not be able to install the updated application over an existing installation.

## Project Structure

```text
ShopTrackPro/
│
├── app/
│   ├── build.gradle.kts
│   │
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── shoptrackpro/
│           │           └── app/
│           │               ├── Backup.kt
│           │               ├── Catalog.kt
│           │               ├── Database.kt
│           │               ├── MainActivity.kt
│           │               └── Security.kt
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
├── README.md
└── RELEASE_NOTES_v2.3.0.md
```

## Current Limitations

The following limitations apply to the current version:

- The starter product catalog is not a complete product database.
- Barcode matching depends on products available in the local catalog.
- OCR currently supports Latin-script text recognition.
- Sales are recorded through a single-product sales action.
- Shopping cart functionality is not available.
- GST invoices, discounts, returns, and supplier ledgers are not currently supported.
- Inter-shop stock transfers and batch tracking are not available.
- JSON backups do not include externally stored product images.
- PIN protection should not be considered equivalent to encrypted database storage.
- The application currently does not provide cloud synchronization.

## Data and Privacy

ShopTrack Pro is designed around local data storage.

Users should regularly export backups to protect their information.

**Important:** Backup files may contain sensitive shop and business information. Do not upload personal backup JSON files or other private data to a public repository.

## Version History

| Version | Version Code | Highlights |
|---|---:|---|
| v2.3.0 | 28 | Notification fixes, barcode improvements, product duplication, unit selection, and improved search |
| v2.2.6 | 27 | Previous public release |

For complete release information, visit:

[GitHub Releases](https://github.com/shoryadevraj/ShopTrackPro/releases)

## Developer

**Shorya Devraj**

- GitHub: [@shoryadevraj](https://github.com/shoryadevraj)
- Repository: [ShopTrackPro](https://github.com/shoryadevraj/ShopTrackPro)
- Latest Release: [v2.3.0](https://github.com/shoryadevraj/ShopTrackPro/releases/tag/v2.3.0)
- APK Download: [Download ShopTrack Pro](https://github.com/shoryadevraj/ShopTrackPro/releases/download/v2.3.0/ShopTrackPro-v2.3.0.apk)

## License

No license has been specified yet. All rights are reserved by the author unless a license is added to this repository.

---

<p align="center">
  <strong>ShopTrack Pro</strong>
  <br>
  Simplifying everyday shop management.
  <br><br>
  Made with ❤️ by Shorya Devraj
</p>
