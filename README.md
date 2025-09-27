# Gestion des Emplois du Temps – Android Mobile App

This is the Android mobile application of the academic project Gestion des Emplois du Temps (Timetable Management System), developed with Android Studio.
It provides students, professors, and admins with a mobile-friendly interface to manage and visualize class schedules, handle notifications, and manage make-up sessions (séances de rattrapage).

# 📌 Project Overview

The goal of this Android app is to offer a smooth, intuitive, and interactive experience on mobile devices:

Display timetables for students and professors.

Allow professors to schedule make-up sessions.

Send push notifications for timetable changes and session updates.

Allow admins to validate requests and manage schedules.

Offer offline caching and optimized performance for mobile devices.

This repository contains the Android implementation of the Timetable Management System.

# 🚀 Features

📅 Timetable Display: View class schedules for students and professors on mobile.

🔔 Notifications: Alerts when a schedule changes or a make-up session is added.

👩‍🏫 Professor Tools: Submit requests for make-up sessions directly from the app.

🧑‍💼 Admin Role:

Validate or reject professor requests.

Perform CRUD operations on professors and timetable entries.

🎓 Student Access: Personalized schedule with notifications.

📱 Mobile Optimized: Fully responsive UI, smooth navigation, and offline-friendly features.

💾 Local Storage: Cache important timetable data for offline access.

⚡ Interactive UI: Designed in line with modern mobile UX principles.

# 🛠️ Tech Stack

Language: Java / Kotlin

Framework: Android SDK, Jetpack components

UI: XML layouts, Material Design components

Networking: Retrofit / Volley for API requests

Data Storage: SQLite / Room Database

Notifications: Firebase Cloud Messaging (FCM)

Version Control: Git & GitHub

# 📂 Project Structure

``bash
app/
├── src/main/java/ # Java/Kotlin classes, Activities, ViewModels
├── src/main/res/ # Layouts, drawables, icons, colors, strings
├── src/main/assets/ # Static assets
└── AndroidManifest.xml # App configuration


---

## ▶️ Steps to Run the Project

1. **Clone the repository**  
``bash
git clone https://github.com/chorfichaymaa101/Gestion-des-emplois-du-temps-Android.git
cd Gestion-des-emplois-du-temps-Android


Open in Android Studio

Open the project in Android Studio 2023+

Allow Gradle to sync and download dependencies

Run the app

Connect an Android device or use the emulator

Press Run in Android Studio

Optional: Build APK / App Bundle

Build > Build Bundle(s) / APK(s)

Generate signed APK for production if needed
