# CS 478 — Android Projects Overview

This repository contains three separate Android Studio projects for **UIC CS 478** (or equivalent). Each folder is a self-contained app demonstrating different course topics: activities and intents, lists and grids with external links, and fragments with embedded web content.

Anyone reading this document should understand **what each app does**, **how the pieces fit together**, and **where to open the code**.

---

## Repository layout

| Folder | App focus | Main technologies |
|--------|-----------|-------------------|
| **Project1** | Phone number entry, validation, and dialer intent | Java, classic `Activity`, `startActivityForResult`, `Toast`, `EditText` + `OnEditorActionListener` |
| **Project2** | Movie poster grid, full-screen poster, browser links | Java, `GridView`, `BaseAdapter`, context menus, `Intent.ACTION_VIEW` |
| **Project3** | Chicago attractions & restaurants browser | Java, `AppCompatActivity`, `Fragment`, `WebView`, options menu, split list/detail UI |

**Note:** The runnable Android Studio project for Project 1 lives under **`Project1/PhoneFormatApp/`**, not at the root of `Project1/`.

---

## Project 1 — Phone format and dialer (`Project1/`)

### Course goal

Implement a two-activity flow: welcome screen → enter a U.S.-style 10-digit phone number → optionally open the system phone app with that number prefilled **without placing a call**. The assignment spec is summarized in `Project1/project1.txt`.

### What the app does

1. **Main screen (`MainActivity`)**  
   - Shows a welcome message and two buttons.  
   - **Layout behavior:** In **portrait**, the buttons stack vertically; in **landscape**, they sit side by side (separate layout under `res/layout-land/`).  
   - **Enter phone number** starts `PhoneEntryActivity` for a result.  
   - **Open dialer** is only meaningful after a valid number was accepted; if the user tries too early, a **Toast** explains the protocol.

2. **Phone entry screen (`PhoneEntryActivity`)**  
   - User types a number in an `EditText`.  
   - **Accepted formats (exactly these):**  
     - Plain: `3125551212` (10 digits, no punctuation).  
     - With punctuation: `(312) 555-1212` or `(312)555-1212` — parentheses around the area code, optional space after `)`, then `xxx-xxxx`.  
   - Invalid input shows a Toast; valid input returns **normalized 10 digits** via intent extra `EXTRA_PHONE_DIGITS`.  
   - **Submit** works from the button or from the keyboard **Done** action via `TextView.OnEditorActionListener` (as required by the assignment).  
   - **Cancel** returns canceled result.

3. **After a valid number**  
   - Main activity stores the digits, updates the welcome text to show a friendly format, enables the dialer button, and persists the digits across configuration changes using `onSaveInstanceState`.  
   - **Open dialer** uses `Intent.ACTION_DIAL` with `tel:` URI so the system dialer opens with the number filled in but does not auto-call.

### Important files

| Path | Role |
|------|------|
| `Project1/PhoneFormatApp/app/src/main/java/.../MainActivity.java` | Launcher UI, result handling, dialer intent |
| `Project1/PhoneFormatApp/app/src/main/java/.../PhoneEntryActivity.java` | Validation, regex, returning digits |
| `Project1/PhoneFormatApp/app/src/main/AndroidManifest.xml` | `QUERY_ALL_PACKAGES` + `<queries>` for `DIAL`/`tel` (per assignment notes) |

### How to run

Open **`Project1/PhoneFormatApp`** in Android Studio, sync Gradle, and run on an emulator or device. The module targets **compileSdk / targetSdk 36** (see `app/build.gradle`).

---

## Project 2 — Movie poster grid (`Project2/`)

### Course goal

Show a **scrollable grid** of movies (thumbnail + title). Short and long clicks expose different navigation: full poster activity, IMDb in the browser, Rotten Tomatoes, and streaming/purchase links. Spec summary: `Project2/project2_Info.txt`.

### What the app does

1. **Main grid (`MainActivity`)**  
   - A `GridView` with **2 columns** and **9 movies** (e.g., *Kung Fu Panda*, *Avatar*, *The Lion King*, …).  
   - Spacing uses **6 dp** horizontal/vertical gap and padding (within the assignment’s “no more than 10 dp” guideline).  
   - **`MovieGridAdapter`** (`BaseAdapter`) inflates `grid_item_movie.xml`: poster image on top, title below.

2. **Short click**  
   - Opens **`PosterActivity`** with extras: title, drawable resource id for the poster, and IMDb URL.  
   - Full poster is shown in an `ImageView`; user can go back with the system back gesture/button.

3. **Tap on full poster**  
   - Opens the **IMDb** page for that movie in the default browser (`Intent.ACTION_VIEW`).

4. **Long click (context menu)**  
   - Header shows the movie title. Options:  
     - View full poster (same as short click)  
     - Open **Rotten Tomatoes** URL for that title  
     - Open a **streaming / purchase** URL (Amazon, Disney+, etc., per movie)

### Important files

| Path | Role |
|------|------|
| `Project2/app/src/main/java/.../MainActivity.java` | Grid, click listeners, context menu, URL arrays |
| `Project2/app/src/main/java/.../PosterActivity.java` | Full-screen poster + IMDb on image tap |
| `Project2/app/src/main/java/.../MovieGridAdapter.java` | Grid cell binding |
| `Project2/app/src/main/res/layout/activity_main.xml` | `GridView` column count and spacing |
| `Project2/app/src/main/res/drawable/` | Poster images (referenced by the adapter) |

### How to run

Open **`Project2`** in Android Studio. The Gradle file enables Compose in the build features, but the **movie UI is implemented in Java** with classic views (`GridView`, activities). Run on **API 28+** (see `app/build.gradle.kts`).

### Extra materials in the folder

- `Project2/REFINED_SPEC.txt`, `Project2/LLM_REPORT.txt`, `Project2/Links.txt`, `Project2/VideoLink.txt` — assignment deliverables (refined specification, LLM usage report, links, video reference).

---

## Project 3 — Chicago guide: attractions & restaurants (`Project3/`)

### Course goal

A small **city guide** app: choose **Attractions** or **Restaurants**, then browse a list of places and see each place’s **official website inside the app** using a `WebView`, with a **master/detail style** layout and navigation between the two categories.

### What the app does

1. **Home (`MainActivity`)**  
   - Two buttons: **Attractions** and **Restaurants**.  
   - Each shows a short Toast and starts the corresponding activity.

2. **Browse flow (`BaseBrowseActivity` + subclasses)**  
   - **`AttractionsActivity`** — five Chicago attractions (Lincoln Park Zoo, Navy Pier, MSI, Art Institute, TILT at 360 CHICAGO) with real URLs.  
   - **`RestaurantsActivity`** — six well-known Chicago restaurants with real URLs.  
   - Both extend **`BaseBrowseActivity`**, which hosts:  
     - A **toolbar** (`MaterialToolbar`) with an **options menu** to jump between Attractions and Restaurants (starts the other activity and finishes the current one to avoid stacking duplicates).  
     - **`ItemListFragment`** — `ListView` of names using activated single-choice rows.  
     - **`WebDetailFragment`** — `WebView` with JavaScript and DOM storage enabled, custom `WebViewClient`, and **state saved/restored** so rotation does not lose the page.  

3. **Master/detail behavior**  
   - Before any selection, the list uses the full width; the detail pane is hidden.  
   - After a list item is chosen, the layout **splits**: list ~1/3 width, web detail ~2/3 (`LinearLayout` weights in `activity_browse.xml`).  
   - **Back** when a site is open first clears the selection and removes the detail fragment (handled with `OnBackPressedCallback`), then a second back exits the screen as usual.

### Important files

| Path | Role |
|------|------|
| `Project3/app/src/main/java/.../MainActivity.java` | Entry buttons |
| `Project3/app/src/main/java/.../BaseBrowseActivity.java` | Toolbar, fragments, split layout, menu, back handling |
| `Project3/app/src/main/java/.../AttractionsActivity.java` | Attraction names + URLs |
| `Project3/app/src/main/java/.../RestaurantsActivity.java` | Restaurant names + URLs |
| `Project3/app/src/main/java/.../ItemListFragment.java` | List UI + selection callback |
| `Project3/app/src/main/java/.../WebDetailFragment.java` | WebView loading and state |
| `Project3/app/src/main/AndroidManifest.xml` | `INTERNET` permission for WebView |
| `Project3/app/src/main/res/menu/guide_menu.xml` | Switch between attractions and restaurants |

### How to run

Open **`Project3`** in Android Studio. Targets **compileSdk / targetSdk 36**, **minSdk 36** (see `app/build.gradle`). Requires network access for websites to load.

---

## Shared concepts across projects

- **Explicit intents** — Moving between your own activities with `Intent` and extras.  
- **Implicit intents** — Opening dialer (`ACTION_DIAL`) and browser (`ACTION_VIEW` + `http`/`https` URIs).  
- **User feedback** — Toasts for errors and protocol hints (Project 1 especially).  
- **Configuration changes** — Saving UI-related state (`Bundle`) in Project 1 (digits) and Project 3 (selected index + WebView state).  
- **Course tooling** — Specs and LLM/video deliverables live next to some projects (`project1.txt`, `project2_Info.txt`, `REFINED_SPEC.txt`, `LLM_REPORT.txt`, etc.).

---

## Quick start checklist

1. Install [Android Studio](https://developer.android.com/studio) with an SDK that includes **API 36** (and for Project 2, at least **API 28** for the lower minSdk).  
2. Open the **correct subfolder** as the project root (`PhoneFormatApp` for Project 1).  
3. Sync Gradle, select a run configuration, choose an emulator or device, then Run.

If you are submitting or archiving the work, zip each Studio project (or the whole repo) so `app/`, `gradle/`, and manifests stay intact.
