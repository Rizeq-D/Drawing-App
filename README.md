# Drawing App

This is a simple Android drawing application that allows users to create drawings using different shapes and save them to their device. 
The app includes basic features such as free drawing, shape drawing (circles, rectangles, triangles), fill area, fill screen, and saving the drawing as an image file.

## Features

- **Free Draw Mode:** Allows users to draw freely on the canvas.
- **Shape Drawing:** Supports drawing circles, rectangles, and triangles by selecting the desired shape from the UI.
- **Fill Area:** Allows users to fill a specific area with the selected color using flood fill algorithm.
- **Fill Screen:** Fills the entire canvas with the selected color.
- **Brush Size Control:** Users can adjust the brush size using a seek bar.
- **Color Selection:** Users can choose from a variety of colors for their brush or fill.
- **Save Drawing:** Save the current drawing as a PNG file in the external storage of the device.
- **Clear Drawing:** Clears the entire canvas.

## How It Works

- The app uses a `Canvas` to draw on a `Bitmap` that is displayed on the screen.
- Different shapes are drawn by tracking the user's touch events (ACTION_DOWN, ACTION_MOVE, ACTION_UP).
- The `onDraw()` method renders the content onto the canvas, and it keeps the drawing persistent using the `Bitmap`.
- The flood fill algorithm is used to fill specific areas with the selected color.

## Code Overview

- **MainActivity.kt:** Handles user interaction with the UI and manages the current drawing mode (free draw, shapes, fill, etc.).
- **DrawingView.kt:** Custom view that handles all the drawing logic, including processing touch events and rendering shapes.
- **Shape Enum:** Enum class used to manage the current shape being drawn (FREE_DRAW, CIRCLE, RECTANGLE, TRIANGLE, FILL).

## Setup

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Run the app on an emulator or a physical device with Android OS.

## Permissions

The app requests the `WRITE_EXTERNAL_STORAGE` permission to save drawings to the device.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

