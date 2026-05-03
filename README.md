# Wellness Tracker App

A comprehensive Android application for tracking personal wellness and daily health routines. Built with Kotlin and Android Studio.

## Features

### 1. Daily Habit Tracker
- Add, edit, and delete daily wellness habits
- Track completion progress for each day
- View streak counts for habits
- Categories for different types of habits
- Real-time progress visualization

### 2. Mood Journal
- Log mood entries with date/time
- Emoji selector for different moods (Happy, Good, Neutral, Sad, Anxious)
- Add optional notes to mood entries
- View recent mood entries
- Track emotional wellness over time

### 3. Hydration Tracker
- Track daily water intake
- Quick-add buttons for common amounts (250ml, 500ml, 750ml, 1000ml)
- Set and track daily water goals
- View progress with visual indicators
- 7-day average statistics
- Recent days history

### 4. Settings & Customization
- Dark mode toggle
- Notification settings for hydration reminders
- Customizable water goals and reminder intervals
- Data export/import functionality
- Share progress feature
- Clear all data option

## Technical Architecture

### Architecture Components
- **Activities**: MainActivity with bottom navigation
- **Fragments**: HabitsFragment, MoodFragment, WaterFragment, SettingsFragment
- **Data Persistence**: SharedPreferences for local storage
- **State Management**: Retained across sessions
- **Responsive UI**: Adapts to phones and tablets, portrait & landscape

### Data Models
- `Habit`: Tracks habit name, description, category, completion dates, and streak
- `MoodEntry`: Stores mood type, emoji, note, and timestamp
- `WaterEntry`: Manages daily water intake with timestamps

### Data Managers
- `HabitManager`: Handles habit CRUD operations
- `MoodManager`: Manages mood entries
- `WaterManager`: Tracks water intake and goals
- `SettingsManager`: Manages app settings

### Adapters
- `HabitAdapter`: RecyclerView adapter for habits list
- `MoodAdapter`: RecyclerView adapter for mood entries
- `WaterAdapter`: RecyclerView adapter for water history

## Project Structure

```
app/src/main/
├── java/com/example/wellnesstracker/
│   ├── MainActivity.kt
│   ├── models/
│   │   ├── Habit.kt
│   │   ├── MoodEntry.kt
│   │   └── WaterEntry.kt
│   ├── data/
│   │   ├── HabitManager.kt
│   │   ├── MoodManager.kt
│   │   ├── WaterManager.kt
│   │   └── SettingsManager.kt
│   ├── adapters/
│   │   ├── HabitAdapter.kt
│   │   ├── MoodAdapter.kt
│   │   └── WaterAdapter.kt
│   └── fragments/
│       ├── HabitsFragment.kt
│       ├── MoodFragment.kt
│       ├── WaterFragment.kt
│       └── SettingsFragment.kt
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── fragment_habits.xml
    │   ├── fragment_mood.xml
    │   ├── fragment_water.xml
    │   ├── fragment_settings.xml
    │   ├── dialog_add_habit.xml
    │   ├── dialog_add_mood.xml
    │   ├── item_habit.xml
    │   ├── item_mood_entry.xml
    │   └── item_water_day.xml
    ├── drawable/
    │   ├── ic_*.xml (various icons)
    │   ├── badge_background.xml
    │   ├── circle_background.xml
    │   ├── dialog_background.xml
    │   └── progress_gradient.xml
    ├── values/
    │   ├── colors.xml
    │   ├── dimens.xml
    │   ├── strings.xml
    │   └── themes.xml
    ├── color/
    │   └── bottom_nav_color.xml
    └── menu/
        └── bottom_navigation.xml
```

## Setup Instructions

### Prerequisites
- Android Studio (latest version)
- JDK 11 or higher
- Android SDK with API level 24 or higher

### Installation

1. **Clone or download the project**
   ```bash
   git clone <repository-url>
   cd WellnessTracker
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the WellnessTracker folder

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - If not, click "Sync Now" in the notification bar

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon)
   - Select your device/emulator
   - The app will install and launch

## Dependencies

The app uses the following libraries:
- **Material Components**: For UI components
- **Fragment KTX**: For fragment support
- **RecyclerView**: For lists
- **Gson**: For JSON serialization
- **WorkManager**: For background tasks (notifications)

All dependencies are managed in `app/build.gradle.kts`.

## Key Features Implementation

### Data Persistence
- All data is stored locally using SharedPreferences
- No database required
- Data persists across app restarts
- Gson library handles JSON serialization

### Navigation
- Bottom navigation with 4 tabs
- Fragment-based navigation
- Smooth transitions between screens

### UI/UX
- Material Design 3 components
- Modern card-based layouts
- Progress indicators and visual feedback
- Responsive design for different screen sizes

### Notifications (To be implemented)
- WorkManager for scheduling hydration reminders
- Customizable reminder intervals
- Notification permissions handling

## Usage

### Adding a Habit
1. Navigate to the Habits tab
2. Tap the "Add Habit" button
3. Enter habit name and description
4. Tap "Save"

### Tracking Mood
1. Go to the Mood tab
2. Tap "Add Mood Entry"
3. Select your current mood
4. Optionally add a note
5. Tap "Save Mood"

### Tracking Water Intake
1. Navigate to the Water tab
2. Use quick-add buttons or custom amount
3. View progress toward daily goal
4. Check statistics and recent days

### Customizing Settings
1. Go to Settings tab
2. Toggle dark mode
3. Enable/disable notifications
4. Adjust water goals and reminder intervals
5. Export, import, or clear data

## Future Enhancements

Potential features for future versions:
- Home screen widget showing habit completion
- Sensor integration for step counting
- MPAndroidChart integration for mood trends
- Cloud backup and sync
- Social sharing features
- Advanced analytics and insights

## Permissions

The app requires the following permissions:
- `POST_NOTIFICATIONS`: For hydration reminders
- `SCHEDULE_EXACT_ALARM`: For precise reminder timing
- `USE_EXACT_ALARM`: For alarm functionality
- `WAKE_LOCK`: For background notifications

## License

This project is created for educational purposes as a university assignment.

## Support

For issues or questions, please contact the development team.

---

**Version**: 1.0.0  
**Last Updated**: 2024  
**Target SDK**: 36  
**Min SDK**: 24











