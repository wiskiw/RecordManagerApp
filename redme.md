

# Record Manager App

This is an Android project developed as a test assignment. The project follows the MVI (Model-View-Intent) pattern and Clean Architecture approach. Android Room library is used for persistent storage implementation.

## Overview

This Android application is designed to manage various objects with attributes and relationships. Users can create, edit, and delete objects and their relationships. The application includes a search function and persistent storage for data.

## Features

- **MVI Architecture**: Implements Model-View-Intent pattern for better state management and UI updates.
- **Clean Architecture**: Ensures a scalable and maintainable codebase.
- **Android Room**: For persistent data storage
- **Jetpack Compose**: Uses Jetpack Compose for declarative UI development.
- **Dependency Injection**: Implements Koin for dependency injection.

## Architecture

The project follows the MVI and Clean Architecture principles:

- **Presentation Layer**: Contains UI components and ViewModels.
- **Domain Layer**: Contains use cases and business logic.
- **Data Layer**: Contains repositories and DTO models.

## Getting Started

### Prerequisites

- Android Studio
- Gradle

### Installation

1. Clone the repository:
   ```sh
   git clone git@github.com:wiskiw/RecordManagerApp.git

2. Build the app using Android Studio
