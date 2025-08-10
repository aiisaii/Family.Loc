# Family Tracker Android App

This is a complete Android application that allows family members to see each other’s real-time location using OpenStreetMap for the map display and Firebase Realtime Database for location sharing.

## Prerequisites

*   **Android Studio:** Make sure you have the latest version of Android Studio installed.
*   **Java JDK:** Android Studio usually comes with its own JDK, but ensure your environment is set up correctly.
*   **An Android Device or Emulator:** With API level 23 or higher.

## Setup Instructions

### 1. Firebase Project Setup

This app requires a Firebase project to handle authentication and real-time location data.

1.  **Go to the Firebase Console:** [https://console.firebase.google.com/](https://console.firebase.google.com/)
2.  **Create a new project:** Click on "Add project" and follow the on-screen instructions.
3.  **Add an Android App:**
    *   Inside your new project, click the Android icon to add a new Android app.
    *   **Package Name:** You **must** use `com.example.familytracker`.
    *   **App Nickname:** (Optional) You can name it "Family Tracker".
    *   **Debug signing certificate SHA-1:** (Optional) You can skip this for now.
4.  **Download `google-services.json`:**
    *   After registering the app, Firebase will provide a `google-services.json` file to download.
    *   Save this file. You will need it in the next step.
5.  **Enable Authentication:**
    *   In the Firebase console, go to the **Authentication** section (in the left-hand menu).
    *   Click the "Sign-in method" tab.
    *   Select **Email/Password** and enable it.
6.  **Setup Realtime Database:**
    *   Go to the **Realtime Database** section.
    *   Click "Create Database".
    *   Choose a location for your database.
    *   Start in **locked mode**.
    *   Go to the **Rules** tab in the Realtime Database section.
    *   Click **"Import Rules"** and select the `database.rules.json` file from this project. If you cannot import, simply copy and paste the content of `database.rules.json` into the editor and click "Publish".

### 2. Project Configuration

1.  **Replace Placeholder `google-services.json`:**
    *   Open the project in Android Studio.
    *   In the Project view (select "Project" from the dropdown, not "Android"), navigate to the `app/` directory.
    *   Copy the `google-services.json` file you downloaded from Firebase and paste it into the `app/` directory, overwriting the existing placeholder file.
2.  **Sync Gradle:**
    *   Android Studio should prompt you to sync Gradle. If not, click the "Sync Project with Gradle Files" button (an elephant icon) in the toolbar. This will download all the required dependencies.

### 3. Build and Run

1.  **Connect a Device:** Connect your Android phone (with USB debugging enabled) or start an Android emulator.
2.  **Run the App:** Click the "Run 'app'" button (a green play icon) in the Android Studio toolbar.

## How to Use

1.  **Sign Up:** On the login screen, enter an email and password and click **Sign Up**.
2.  **Sign In:** Use the same credentials to **Sign In**.
3.  **Grant Permissions:** The app will ask for location permissions. You must grant them for the app to work correctly. For continuous tracking, grant "Allow all the time" access.
4.  **View Map:** You will see a map centered on your current location. Your position will be updated every 5 seconds.
5.  **Track Family Members:** To see other family members, they must also install the app on their phones, connect it to the **same Firebase project**, and sign up. Their location markers will appear on your map in real-time.