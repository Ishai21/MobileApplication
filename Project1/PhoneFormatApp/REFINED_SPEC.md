## Refined functional specification — Phone Format Checker

### Overview
The app has **two activities**:
- **`MainActivity`**: shows a welcome message and two buttons.
- **`PhoneEntryActivity`**: allows the user to enter a phone number and validates its format.

The app never places a call. When it opens the system Phone/Dialer, it uses **`ACTION_DIAL`** (shows the number, does not call).

### MainActivity UI + rotation behavior
MainActivity contains:
- A **welcome TextView** centered above the buttons.
- Two buttons:
  - **Button 1**: “Enter phone number”
  - **Button 2**: “Open Phone app”

Orientation rules:
- **Portrait**: buttons are arranged **vertically** (Button 1 above Button 2).
- **Landscape**: buttons are arranged **horizontally** (Button 1 left of Button 2).
- The welcome text remains **centered above** the buttons in both orientations.

### Button pressing protocol (corner case required)
- On a fresh launch, there is **no saved phone number**.
- **Button 2** (“Open Phone app”) is disabled until a valid number is saved.
- If the user attempts to open the dialer without a saved valid number (e.g., via unexpected UI state), the app shows a **Toast** saying the user must enter a valid number first.

### PhoneEntryActivity behavior
PhoneEntryActivity displays:
- Instructions describing the legal formats
- An `EditText` for the phone number
- “Submit” and “Cancel” buttons

Input is accepted via:
- Pressing **Submit**, or
- Pressing the keyboard **Done/Enter**, handled through `TextView.OnEditorActionListener` on the `EditText`.

#### Legal phone number formats (and only these)
The input string must match **exactly one** of the following:

1) **No punctuation** (exactly 10 digits):
- Example: `3125551212`
- Regex: `^\d{10}$`

2) **Parentheses + optional space + mandatory dash**:
- Example with space: `(312) 555-1212`
- Example without space: `(312)555-1212`
- Regex: `^\(\d{3}\)\s?\d{3}-\d{4}$`

No other formats are accepted. Examples that are rejected:
- `312-555-1212`
- `312 555 1212`
- `(312)-555-1212`
- `(312) 555 1212`
- `+1 3125551212`
- Any string with leading/trailing characters beyond the formats above

#### Result returned to MainActivity
- If the input is legal, PhoneEntryActivity returns:
  - `RESULT_OK`
  - extra string `EXTRA_PHONE_DIGITS` containing exactly **10 digits** (punctuation removed)
- If canceled, it returns `RESULT_CANCELED` and no number is saved.
- If invalid, the activity shows a **Toast** explaining the legal formats and stays on the entry screen (so the user can correct it).

### Opening the Phone app (Dialer)
When a valid phone number is saved and the user presses Button 2:
- MainActivity launches an implicit intent:
  - `Intent.ACTION_DIAL`
  - `Uri` of the form `tel:<10digits>`
- If the device cannot resolve a dialer app, a Toast is shown.

### Toast messages (minimum set)
The app uses `Toast.makeText(...).show()` to indicate:
- Invalid phone number format
- Protocol violation (trying to open dialer before saving a valid number)
- Cancel/no number saved

### Platform requirements
- Intended for **Pixel 9 AVD, Android 16 (API 36)**.
- Manifest includes `android.permission.QUERY_ALL_PACKAGES` (as required by the assignment prompt).

