# Expense Tracker

## Overview
Expense Tracker is an application that allows users to track their daily transactions and manage expenses effectively. It provides a secure environment with JWT-based authentication and sends notifications to help users stay within their spending limits.

## Features
- Track daily income and expenses.
- Categorize and view all transactions.
- Receive notifications when nearing or exceeding budget limits.
- Secure authentication and authorization using JWT.
- User-friendly interface developed with Kotlin and Android Studio.
- Spring Boot backend with RESTful APIs.

## Technology Stack
- **Frontend:** Kotlin, Android Studio
- **Backend:** Spring Boot (Java)
- **Authentication:** JWT (JSON Web Tokens)
- **Database:** MySQL or PostgreSQL
- **API Communication:** Retrofit (Android) and RESTful APIs (Spring Boot)

## Architecture
- The Android application communicates with the Spring Boot backend using REST APIs.
- JWT tokens are used to manage user authentication and secure API endpoints.
- All transaction data is stored in a relational database.
- Notifications are triggered based on predefined user budget thresholds.

## Security
- Secure authentication using JWT tokens.
- API endpoints protected from unauthorized access.
- Sensitive data is stored and transmitted securely.

## Notifications
- Users can set budget limits in the app.
- Notifications are sent to inform users when they are close to or exceeding their spending limits.
