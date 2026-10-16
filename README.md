# 💱 Currency Converter Utility App

An Android utility mobile application built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, and **Retrofit 2**. This app delivers focused, "at-a-glance" live currency conversion for daily financial activities.

Developed for **CP3406 / CP5307 Assessment 1: Utility App**.

---

## 📱 App Overview & Core Features

The **Currency Converter Utility App** provides rapid, real-time exchange rate calculations using the free, open **Frankfurter REST API**.

### Key Features:
* 💱 **Live Exchange Rates**: Asynchronously fetches live conversion rates across major world currencies (`USD`, `AUD`, `EUR`, `GBP`, `JPY`, `CAD`, `INR`, `NZD`, `SGD`).
* ⚡ **Instant Conversion & Calculation**: Reactive UI automatically recalculates amounts as you type or change selected currencies.
* ⇄ **Currency Swap**: One-tap currency swapping button to instantly reverse source and target currencies.
* 🌓 **Dynamic Theme Switching**: Real-time app theme toggle between **System Default**, **Light Mode**, and **Dark Mode**.
* 🌐 **Live In-App Multi-Language Translation**: Instant real-time UI translation supporting **English**, **Spanish**, **French**, **German**, and **Japanese**.
* 🛡️ **Robust State & Error Handling**: Gracefully handles network loading spinners and offline / connection error banners without crashing.

---

## 🏗️ Technical Architecture & Stack

The application strictly follows modern Android development best practices and MVVM architecture:

* **Language**: Kotlin 2.0
* **UI Framework**: Jetpack Compose with Material Design 3 (`Scaffold`, `NavigationBar`, `Card`, `ExposedDropdownMenuBox`, `RadioButton`, `OutlinedTextField`)
* **Architecture Pattern**: MVVM (Model-View-ViewModel) with Unidirectional Data Flow
* **State Management**: Kotlin `StateFlow` (`CurrencyUiState`, `SettingsUiState`) and `ViewModel`
* **Networking**: Retrofit 2 + Gson Converter Factory + Kotlin Coroutines (`viewModelScope`)
* **REST API**: Frankfurter Open Exchange Rates API (`https://api.frankfurter.app/`)

---

## 📂 Project Structure

```
au.edu.jcu.assessment.utilityapp
├── MainActivity.kt                  # Entry Activity & Root Navigation
├── api
│   ├── CurrencyResponse.kt          # Gson Data Model for API JSON
│   ├── CurrencyApi.kt               # Retrofit GET Endpoint Interface
│   └── RetrofitInstance.kt          # Retrofit Client Singleton
├── viewmodel
│   ├── CurrencyUiState.kt           # State Data Class for Utility Screen
│   ├── CurrencyViewModel.kt         # Business Logic & Rate Calculation
│   ├── SettingsUiState.kt           # State Data Class for Settings Screen
│   └── SettingsViewModel.kt         # Theme & Language Management
├── screen
│   ├── utility_screen.kt            # Currency Converter UI Composable
│   └── setting_screen.kt            # Theme, Language & App Info Screen
└── ui.theme
    ├── Color.kt, Theme.kt, Type.kt   # Material 3 Color Schemes & Typography
```

---

## 🚀 How to Run the App

1. Clone or download this repository.
2. Open the project in **Android Studio (Ladybug / Jellyfish or newer)**.
3. Sync Gradle dependencies.
4. Select an Android Emulator or connected physical device (Android 8.0 / API 26 or higher recommended).
5. Click **Run (`Shift + F10`)**.
6. Ensure your device has internet access to fetch live exchange rates.

---

## 📄 Self-Reflection & Submission
This project satisfies all requirements for CP3406/CP5307 Assessment 1.
* A 15+ commit Git history documenting continuous step-by-step development.
* Submission includes project `.zip`, GitHub repository link, and Gibbs' Reflective Cycle self-reflection PDF.
