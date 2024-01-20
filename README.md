# S&Z Tutors

Welcome to our Mobile Systems project! This guide will help you set up and run the project locally on your machine.

## Prerequisites

Make sure you have the following installed on your machine:

- [Android Studio](https://developer.android.com/studio)
- [Git](https://git-scm.com/)

## Getting Started

1. **Clone the repository to your local machine:**
    ```bash
    git clone https://github.com/JStepnicki/MobileSystems.git
    ```

2. **Open Android Studio and select "Open an existing Android Studio project."** Navigate to the cloned repository and select the "MobileSystems" directory.

3. **Create a `local.properties` file in the project root and set the path to your Android SDK:**
    ```properties
    sdk.dir=/path/to/your/android/sdk
    ```
    Replace `/path/to/your/android/sdk` with the actual path to your Android SDK.

4. **Configure Google Services:**
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project and follow the setup instructions.
   - Download the `google-services.json` file.
   - Place the `google-services.json` file in the `app/` directory of the project.

5. **Firebase Authentication:**
   - The project uses Firebase Authentication for secure user registration and login.
   - User credentials and data are securely managed through Firebase Authentication.

6. **Build and run the project using Android Studio.**

## Project Functionalities

### For Students:

#### Feature 1: Registration and Profile
- Students can register with their details securely using Firebase Authentication.
- After registration, students can create and complete their profiles.

#### Feature 2: Browse Tutors
- Students can browse through a list of available tutors.
- Filter tutors based on subjects, ratings, and availability.

#### Feature 3: Schedule Appointments
- Students can schedule appointments with tutors based on the tutor's availability.
- View upcoming and past appointments.

#### Feature 4: Rate and Review Tutors
- Students can rate and leave reviews for tutors they've had appointments with.

### For Teachers:

#### Feature 1: Registration and Profile
- Teachers can register with their details securely using Firebase Authentication.
- After registration, teachers can create and complete their profiles.

#### Feature 2: Set Availability
- Teachers can specify their availability for tutoring sessions.

#### Feature 3: Set Subjects and Prices
- Teachers can list the subjects they teach and set their tutoring prices.

#### Feature 4: View Appointments
- Teachers can view their upcoming and past appointments with students.

#### Feature 5: Manage Profile
- Teachers can update and manage their profile information.

## Troubleshooting

- If you encounter any issues related to the SDK or Google Services, ensure that your SDK path is correctly set in `local.properties` and that the `google-services.json` file is placed in the correct location.

## Contributing

If you'd like to contribute to the project, please follow our [contribution guidelines](CONTRIBUTING.md).

## License

This project is licensed under the [Apache License 2.0](LICENSE).
