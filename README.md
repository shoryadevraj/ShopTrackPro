# ShopTrack Pro

**Offline shop inventory and price management for Android.**

ShopTrack Pro helps shop owners manage products, prices, stock, and sales directly on an Android device. The app stores its working data locally and does not require an account, backend, or cloud connection for its core features.

> **Project status:** Version 2.2.6. This source package has not been verified by a successful build or a complete test run in this environment. Build and test it before relying on it for live business records.

## Features

- **Multiple shops:** Create and manage separate shop inventories on one device.
- **Owner and public modes:** Public browsing is read-only; owner functions are protected by a PIN.
- **Product catalog:** English, Hindi, and Hinglish search aliases, categories, editable entries, and barcode fields.
- **Barcode scanning:** Scan barcodes and match them against barcode values saved in the local catalog.
- **Label text recognition:** On-device OCR for Latin-script text, with suggestions that should be reviewed before saving.
- **Pricing:** Multiple quantity or unit price options, optional private purchase price, and public selling price.
- **Stock management:** Stock-in, stock-out, adjustments, current stock, minimum-stock thresholds, and low-stock warnings.
- **Sales and reports:** Record sales, review sales history, and view gross-profit summaries.
- **Price history:** Keep a record of product price changes.
- **Calculators:** Profit/loss and margin calculations, plus a basic arithmetic calculator.
- **Backup and restore:** Export and restore database records as JSON using Android's document picker.

## Important limitations

- The starter catalog is a base catalog, not a guaranteed database of every product or verified SKU. Owners can add and edit entries.
- Barcode scanning works offline, but a scanned code is matched only if that barcode has been saved in the local catalog. It does not query a global product database.
- OCR recognizes Latin-script text. It is not universal product-image recognition, and Hindi OCR is not included.
- The current sales flow records one selected product per sale action. A multi-item cart, invoices, GST/tax handling, discounts, returns, and supplier ledger are not included.
- Stock movement supports IN, OUT, and ADJUST. Inter-shop transfers and expiry/batch tracking are not included.
- JSON backup does not include external image files.
- The owner PIN protects app screens; it is not a substitute for Android device security or encrypted database storage.
- Each installation keeps its own local data. Data is not automatically synchronized between phones.

## Tech stack

- Kotlin
- Jetpack Compose and Material 3
- Room with SQLite for local persistence
- ZXing Android Embedded for barcode scanning
- Google ML Kit Text Recognition for on-device OCR
- Gson for JSON backup/restore
- Gradle Kotlin DSL

## Project requirements

The project configuration specifies:

| Component | Version |
|---|---:|
| Android Gradle Plugin | 8.6.1 |
| Kotlin | 2.0.21 |
| KSP | 2.0.21-1.0.28 |
| Compile SDK | 35 |
| Target SDK | 35 |
| Minimum Android version | Android 8.0 (API 26) |
| Java/Kotlin bytecode target | 17 |

You will need a compatible JDK, Android SDK/platform 35, and a compatible Gradle installation. **This source archive currently does not include the Gradle Wrapper (`gradlew` and `gradle/wrapper/`).** If you use a terminal, install a compatible Gradle version (Gradle 8.7 or later in the 8.x line is suitable for AGP 8.6.1) and Android build tools, or open the project in an Android IDE that can configure the required build environment.

## Build a debug APK

From the project root, with Gradle and the Android SDK configured, run:

```bash
gradle :app:assembleDebug
```

The APK should be generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

A debug APK is intended for testing. Do not treat it as a production release build.

## Create a signed release APK

To distribute updates, sign every release with the same private signing key. Keep the keystore and passwords private and backed up securely. **Never commit your keystore, `keystore.properties`, or passwords to GitHub.** The included `.gitignore` excludes common keystore files and `keystore.properties`.

1. Create a keystore with Java `keytool`:

   ```bash
   keytool -genkeypair -v -keystore shoptrack-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias shoptrack
   ```

2. Create `keystore.properties` in the project root. Replace the example values locally; do not publish this file:

   ```properties
   storeFile=shoptrack-release.jks
   storePassword=YOUR_KEYSTORE_PASSWORD
   keyAlias=shoptrack
   keyPassword=YOUR_KEY_PASSWORD
   ```

3. Build the release variant:

   ```bash
   gradle :app:assembleRelease
   ```

   Expected output:

   ```text
   app/build/outputs/apk/release/app-release.apk
   ```

   The release signing configuration in `app/build.gradle.kts` reads `keystore.properties` when it exists. Without a configured keystore, the release APK will not be signed by this custom release configuration.

**Important:** A new keystore will not match an APK previously signed with a different key. Android normally requires an update to be signed with the same signing identity as the installed app. Check the existing APK's certificate before choosing a key for future updates.

## First use

1. Install and open ShopTrack Pro.
2. Create an owner PIN.
3. Create one or more shops.
4. Select a shop and add products from the catalog or create new entries.
5. Add selling prices and, if needed, private purchase prices.
6. Record stock-in before recording sales.
7. Export JSON backups regularly and keep them somewhere safe.

## Repository structure

```text
ShopTrackPro-v2.2.6/
├── app/
│   ├── src/main/
│   │   ├── java/com/shoptrackpro/app/
│   │   └── res/
│   └── build.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

## Version

- App version: **2.2.6**
- Version code: **27**
- Application ID: `com.shoptrackpro.app`

## Distribution on GitHub

Keep the Android source code in the repository. For convenient downloads, publish the installable APK as an asset attached to a GitHub Release (for example, `v2.2.6`) rather than committing generated APKs into the source tree. The `.gitignore` intentionally excludes APK and AAB build outputs.

## Developer

**Designed and developed by Shorya Devraj.**
