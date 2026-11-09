ShopTrack Pro

A modern, offline-first shop inventory and price management application for Android.

ShopTrack Pro helps shop owners manage multiple shops, products, pricing, stock movements, and sales from a single Android device. It is designed to work offline, with locally stored data and no account, backend, or cloud connection required for its core functionality.

«Current Version: 2.2.6
Project Status: Source code is available, but a successful build and complete test run have not yet been verified. Testing is recommended before using the application for live business operations.»

---

Features

Shop Management

- Manage multiple shops independently on one device.
- Switch between different shop inventories.
- Separate owner and public browsing modes.
- Protect owner functionality using a PIN.

Product Management

- Browse and manage a product catalog.
- Search using English, Hindi, and Hinglish aliases.
- Organize products by category.
- Add and edit product details.
- Store product barcode information.

Barcode Scanning and OCR

- Scan product barcodes using the device camera.
- Match scanned barcodes against locally saved product records.
- Recognize printed Latin-script text using on-device OCR.
- Review OCR suggestions before saving product information.

Pricing and Inventory

- Configure multiple quantity and unit-based price options.
- Maintain private purchase prices and public selling prices.
- Record stock-in, stock-out, and stock adjustments.
- Monitor current stock levels.
- Configure minimum-stock thresholds.
- Receive low-stock warnings.

Sales and Reports

- Record product sales.
- Review sales history.
- View gross-profit summaries.
- Track product price changes over time.

Calculators

- Profit and loss calculations.
- Profit margin calculations.
- Basic arithmetic calculator.

Backup and Restore

- Export application records as JSON.
- Restore records using Android's document picker.
- Keep local backups for data recovery.

---

Technology Stack

Technology| Purpose
Kotlin| Primary programming language
Jetpack Compose| User interface
Material 3| UI components and design
Room| Local database
SQLite| Local data storage
ZXing Android Embedded| Barcode scanning
Google ML Kit| On-device text recognition
Gson| JSON backup and restoration
Gradle Kotlin DSL| Build configuration

---

Project Configuration

Component| Version
Android Gradle Plugin| 8.6.1
Kotlin| 2.0.21
KSP| 2.0.21-1.0.28
Compile SDK| 35
Target SDK| 35
Minimum Android Version| Android 8.0 (API 26)
Java/Kotlin Target| 17

Prerequisites

To build the project, you need:

- A compatible JDK.
- Android SDK Platform 35.
- Android SDK Build Tools.
- Gradle 8.7 or a compatible newer 8.x version.
- A compatible Android development environment.

Note: The source archive does not currently include the Gradle Wrapper ("gradlew" and "gradle/wrapper/"). You must install a compatible Gradle version separately or configure the project in an Android IDE.

---

Building the Project

Build a Debug APK

After configuring Gradle and the Android SDK, run the following command from the project root:

gradle :app:assembleDebug

The generated APK should be available at:

app/build/outputs/apk/debug/app-debug.apk

The debug APK is intended for development and testing.

Build a Signed Release APK

To distribute the application, configure a release signing key.

1. Generate a keystore

keytool -genkeypair -v \
  -keystore shoptrack-release.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias shoptrack

2. Create "keystore.properties"

Create this file in the project root:

storeFile=shoptrack-release.jks
storePassword=YOUR_KEYSTORE_PASSWORD
keyAlias=shoptrack
keyPassword=YOUR_KEY_PASSWORD

Replace the example values with your actual credentials.

3. Build the release APK

gradle :app:assembleRelease

Expected output:

app/build/outputs/apk/release/app-release.apk

The release signing configuration reads "keystore.properties" when available. Make sure the required signing configuration is present before building a distributable release.

«Security: Never upload your keystore, signing passwords, or "keystore.properties" file to GitHub. Keep your signing key securely backed up.»

«Update compatibility: Android generally requires application updates to use the same signing identity as the installed version. A newly generated keystore will not match an APK signed with a different key. Verify the existing APK's signing certificate before selecting a release key.»

---

Getting Started

1. Install and launch ShopTrack Pro.
2. Create an owner PIN.
3. Add one or more shops.
4. Select a shop to manage its inventory.
5. Add products from the catalog or create custom products.
6. Configure selling prices and optional purchase prices.
7. Record stock-in before recording sales.
8. Monitor inventory and review sales reports.
9. Export JSON backups regularly and store them securely.

---

Known Limitations

- The starter catalog is not a complete or verified database of every product and SKU. Owners can add and edit products.
- Barcode scanning works offline but only matches barcodes saved in the local catalog. It does not query a global product database.
- OCR supports Latin-script text. Hindi OCR and universal product-image recognition are not included.
- Each sales action currently records one selected product. Multi-item carts and invoices are not included.
- GST/tax handling, discounts, returns, and supplier ledgers are not included.
- Stock movements support IN, OUT, and ADJUST. Inter-shop transfers and expiry or batch tracking are not included.
- JSON backups do not include external image files.
- The owner PIN protects application screens but does not replace device security or encrypted database storage.
- Each installation maintains its own local data. Automatic synchronization between devices is not available.

---

Project Structure

ShopTrackPro/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/shoptrackpro/app/
│   │       │   ├── Backup.kt
│   │       │   ├── Catalog.kt
│   │       │   ├── Database.kt
│   │       │   ├── MainActivity.kt
│   │       │   └── Security.kt
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── .gitignore
└── README.md

---

Version Information

Detail| Value
Application| ShopTrack Pro
Version| 2.2.6
Version Code| 27
Application ID| "com.shoptrackpro.app"
Platform| Android

---

Privacy and Data Storage

ShopTrack Pro is designed around local data storage.

- Core application functionality does not require an online account.
- Shop and inventory records are stored locally on the device.
- Barcode matching uses the local product catalog.
- OCR is performed on-device.
- Users can export their records using JSON backup.

Keep backups in a secure location. Anyone with access to an unprotected backup file may be able to read its contents.

---

GitHub Distribution

The repository contains the application's source code and development resources.

For downloadable application builds, use GitHub Releases rather than committing generated APK files into the source repository.

Example release tag:

v2.2.6

Attach
