# SciCalc — Android Scientific Calculator

A modern, Material Design 3 scientific calculator built with Kotlin and Jetpack Compose.

## Features

### Calculator Operations
- **Basic**: Addition, subtraction, multiplication, division, percentage, decimal, sign toggle
- **Parentheses**: Full expression grouping with `(` and `)`
- **Trigonometry**: sin, cos, tan, and their inverses (sin⁻¹, cos⁻¹, tan⁻¹)
- **Logarithms**: ln (natural log), log (base-10), and custom log base via `logb(x, n)`
- **Powers**: x², xʸ (arbitrary exponent), √ (square root), ∛ (cube root), ⁿ√ (nth root)
- **Constants**: π and e
- **Additional**: Absolute value |x|, exp, 10ˣ, factorial n!, percent %

### UI/UX
- **Material Design 3** with full light and dark theme support
- **Responsive layout** — adapts between portrait and landscape
- **Scientific keypad** in both orientations with all functions accessible
- **Press animations** — buttons scale smoothly on touch
- **Error handling** — friendly messages for division by zero, invalid expressions
- **History** — last 50 calculations tracked in-memory
- **Large display** with auto-scaling font size
- **Edge-to-edge** rendering on modern Android

### Technical
- **Clean Architecture**: Domain → UI separation
- **Jetpack Compose** for declarative UI
- **ViewModel** + StateFlow for reactive state management
- **exp4j** expression evaluation engine with custom function support
- **Min SDK 26** (Android 8.0), Target SDK 35

## Project Structure

```
app/src/main/java/com/example/scicalc/
├── MainActivity.kt              # Entry point
├── domain/
│   ├── CalcAction.kt            # Sealed class of all calculator actions
│   ├── CalculatorState.kt       # UI state data classes
│   ├── CalculatorViewModel.kt   # ViewModel — action dispatch & state management
│   └── ExpressionEvaluator.kt   # Expression parsing & evaluation engine
└── ui/
    ├── theme/
    │   ├── Color.kt             # Color definitions (light & dark)
    │   ├── Theme.kt             # Material 3 theme configuration
    │   └── Type.kt              # Typography scale
    ├── screens/
    │   └── CalculatorScreen.kt  # Main screen with portrait/landscape layouts
    └── components/
        ├── CalcButton.kt        # Reusable calculator button with animations
        └── CalculatorDisplay.kt  # Display area with expression & result
```

## Build & Run

1. Open the project in Android Studio Hedgehog (2023.1.1) or newer
2. Sync Gradle dependencies
3. Run on a device/emulator with API 26+

```bash
./gradlew assembleDebug
```

## Dependencies

| Library | Purpose |
|---------|---------|
| Jetpack Compose + Material 3 | UI framework |
| Lifecycle ViewModel Compose | State management |
| exp4j 0.4.8 | Expression evaluation |

## License

MIT
