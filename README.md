# Health Infinity

## Overview
The **Health Infinity** is a native Android app built with **Kotlin + Jetpack Compose**.  
It simulates a live sensor data feed (e.g., heart rate), processes it in real-time, and visualizes it using a **custom Canvas-based graph**.  

The app demonstrates:

- Handling **high-frequency data streams** (default: 100ms, scalable to 1ms)
- Maintaining **smooth UI performance** (60 FPS)
- Implementing **memory-efficient and scalable architecture**

---

## Architecture

**Pattern Used:** MVVM (Model-View-ViewModel)

**Why MVVM?**

- Separates UI (Composable) from data/business logic
- Allows **state retention on configuration changes**
- Provides a testable ViewModel independent of UI
- Ensures **thread safety** with coroutines and mutexes

**Components:**

1. **Model (SignalDataSource)**
   - Emits random integer (0–100) every 100ms
   - Runs on a background thread, independent of UI rendering

2. **ViewModel (SignalViewModel)**
   - Maintains a **fixed-size buffer** of last 30 seconds (300 points)
   - Calculates moving average if toggled
   - Publishes immutable `StateFlow` to the UI

3. **View (SignalScreen + StylishSignalGraph)**
   - Observes `SignalUiState`
   - Draws scrolling line graph using **Canvas**
   - Optionally overlays moving average
   - Fully rotation-safe

---

## Memory Management

- **Fixed-size buffer:** Stores only the last 300 points
- **Immutable snapshots:** UI reads copies of the buffer, preventing memory leaks
- **Compose Canvas:** No persistent bitmaps; paths are recreated per frame
- **Lifecycle-safe coroutines:** All background tasks run in `viewModelScope` and are canceled when ViewModel is destroyed

---

## Scaling (High-Frequency Data)

- **Data ingestion decoupled from UI rendering**  
  Background signal collected at 1ms (1000Hz), UI updates at 60 FPS:

```kotlin
while(true) { emitUiState(); delay(16) } // 60 FPS
````

* **Downsampling:** Only a fixed number of points are drawn for smooth rendering
* **Thread safety:** Mutex ensures concurrent reads/writes to buffer are safe
* **Background dispatcher:** `Dispatchers.Default` ensures UI thread is never blocked

---

## Features

* Real-time scrolling line graph
* Moving average overlay (last 10 points) toggle
* Smooth 60 FPS rendering
* Rotation-safe: preserves data on configuration change
* Memory-optimized for indefinite runtime

---

## Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/real-time-signal-visualizer.git
```

2. Open the project in **Android Studio Bumblebee or higher**.
3. Sync Gradle and build the project.
4. Run on an **emulator** or **physical device** (Android 8.0+ recommended).

---

## Usage

1. Launch the app.
2. Observe the real-time graph updating every 100ms.
3. Toggle the "Show Moving Average" switch to overlay the smooth line.

---

## Screenshots

| Live Graph                                 | Moving Average Overlay                           |
| ------------------------------------------ | ------------------------------------------------ |
|(![HealthVertical](https://github.com/user-attachments/assets/ef5dad81-61eb-4955-bbc3-3acbee2ab8cd)) | (![HealthHorizontal](https://github.com/user-attachments/assets/6a269134-128f-478a-aeb0-f016311fb73f)
) |

---

## Optional Demo Video

<p align="center">
  <a href="https://github.com/your-username/your-repo-name/raw/main/demo-video.mp4">
    <img src="https://github.com/user-attachments/assets/08dd1166-4261-4ec6-bf52-247e2a73a1e7" alt="Demo App Video" width="600"/>
  </a>
</p>

> Click on the thumbnail to watch the demo video


---

## Commit Guidelines

* **Logical milestone per commit** (e.g., setup project, data engine, ViewModel logic, graph rendering)
* Avoid dumping all code in a single commit
* Commit history tells the story of your development process

---

## Performance & UI Guard

* 60 FPS rendering with `delay(16)` for frame pacing
* Moving average calculated only on snapshot points
* Rotation-safe state via ViewModel
* Downsampling ensures smooth graph updates at high data frequency

---

## Summary Table

| Aspect         | Approach                                    | Result                      |
| -------------- | ------------------------------------------- | --------------------------- |
| Memory Usage   | Fixed-size buffer + immutable snapshots     | Constant memory, no leaks   |
| UI Performance | Canvas + downsampling + 60 FPS frame pacing | Smooth, jank-free           |
| Thread Safety  | Mutex + background dispatcher               | Safe concurrent access      |
| Scaling        | Decoupled data collection and UI rendering  | Can handle 1000+ Hz streams |
| Config Changes | ViewModel state retention                   | Graph survives rotation     |

---

