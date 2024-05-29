# GalleryAssignment

## Setup Instructions

1. Clone the repository:
    ```bash
    git https://github.com/niksgoswami0212/GalleryAssignment.git
    cd GalleryAssignment
    ```

2. Open the project in Android Studio.

3. Make sure you have the required SDK and tools installed:
    - Android SDK 30
    - Android Studio Arctic Fox or later

4. Build the project:
    - Click on `Build` > `Rebuild Project`

5. Run the project on an emulator or physical device:
    - Click on `Run` > `Run 'app'`

## Features

- Lazy loading of images with coroutine support.
- Efficient memory and disk caching.
- Smooth scrolling of image grid.

## Libraries Used

- [OkHttp](https://square.github.io/okhttp/)
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

## Additional Notes

- Ensure network permissions are granted in the `AndroidManifest.xml`.
- Test the app on various screen sizes and configurations to ensure optimal performance.
