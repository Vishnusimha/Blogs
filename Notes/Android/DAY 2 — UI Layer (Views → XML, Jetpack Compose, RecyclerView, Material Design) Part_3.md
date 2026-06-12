# Technical interview questions

## DAY 2 — UI Layer (Views → XML, Jetpack Compose, RecyclerView, Material Design)

#### Material Design principles

1. What are the core principles of Material Design?
2. How do you implement Material Design components in an Android app?
3. What is the purpose of the Material Design guidelines?
4. How do you use the Material Design color system in an Android app?
5. What are some common Material Design components available in Android?
6. How do you implement shadows and elevations in Material Design?
7. What is the significance of the Material Theming in Material Design?

#### RecyclerView and Adapters

1. What is a RecyclerView and how does it differ from ListView?
2. How do you set up a RecyclerView with an Adapter?
3. What is the purpose of the ViewHolder pattern in RecyclerView?
4. How do you handle item click events in a RecyclerView?
5. Can you explain the different types of LayoutManagers available for RecyclerView?
6. How do you implement pagination in a RecyclerView?
7. What are ItemDecorations and how do you use them in RecyclerView?
8. How do you update the data in a RecyclerView Adapter?

## Answers:

#### Day 2 Answers—Part 3

# Material Design Principles (Android) — Interview Master Guide

> Interview-ready guide to **Material Design** concepts for Android: principles, guidelines, Material Components
> implementation, color system, elevation/shadows, and Material Theming (Material 2 / Material 3).

---

## 1. What are the core principles of Material Design?

Material Design is Google’s design system for creating consistent, modern, and intuitive digital experiences across
platforms.

### ✅ Core principles (high-signal)

#### A) Material is a metaphor

- UI elements behave like **physical surfaces**:
    - cards, sheets, dialogs, floating action buttons
- Surfaces exist at different **elevation levels**
- Motion, shadows and layering convey hierarchy

#### B) Bold, graphic, intentional

- Strong visual hierarchy
- Clear typography
- Meaningful color usage
- Deliberate whitespace and spacing

#### C) Motion provides meaning

- Animations should:
    - guide the user
    - show relationships between elements
    - confirm actions
- Motion should feel:
    - natural
    - purposeful
    - consistent

---

### ⭐ Interview-ready answer

> Material Design is based on a physical “material” metaphor, strong visual hierarchy through typography and color, and
> meaningful motion that explains transitions and interactions.

---

## 2. How do you implement Material Design components in an Android app?

To implement Material Design in Android, you mainly use:

- **Material Components library**
- Material theme (Material2 / Material3)
- Material widgets in XML or Compose

---

### ✅ A) Add Material Components dependency

In most modern projects this is already included:

```gradle
dependencies {
    implementation "com.google.android.material:material:<latest_version>"
}
```

---

### ✅ B) Use a Material theme

In `themes.xml`, use a Material theme:

#### Material 3

```xml

<style name="Theme.MyApp" parent="Theme.Material3.DayNight.NoActionBar">
</style>
```

#### Material 2

```xml

<style name="Theme.MyApp" parent="Theme.MaterialComponents.DayNight.NoActionBar">
</style>
```

---

### ✅ C) Use Material components

Examples:

- `MaterialButton`
- `TextInputLayout`
- `MaterialCardView`
- `NavigationView`
- `BottomNavigationView`
- `MaterialToolbar`

Example:

```xml

<com.google.android.material.button.MaterialButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Continue"/>
```

---

### ✅ D) Compose approach (Material 3)

```kotlin
MaterialTheme {
    Button(onClick = {}) {
        Text("Continue")
    }
}
```

---

### ✅ Implementation best practices

- Use Material components instead of old widgets
- Use `MaterialTheme` to standardize typography and colors
- Ensure ripple feedback, accessibility, and dark mode support

---

## 3. What is the purpose of the Material Design guidelines?

Material guidelines exist to enforce **design consistency** and improve usability across apps.

### ✅ Main purposes

- Provide **consistent UI patterns**
    - app bars, navigation, dialogs
- Improve **usability**
    - predictable interactions
- Improve **accessibility**
    - touch target size, contrast ratios
- Support **responsive design**
    - phones, tablets, foldables
- Provide standards for:
    - spacing
    - typography
    - motion
    - theming

### ⭐ Interview-ready line

> Material guidelines ensure UI consistency, usability, and accessibility by standardizing components, layout,
> typography, color, and motion patterns across applications.

---

## 4. How do you use the Material Design color system in an Android app?

Material color system is designed for:

- visual hierarchy
- branding
- consistency
- light/dark mode support

---

### ✅ A) Material color roles (Material 3)

Material 3 uses **semantic roles** instead of raw colors:

- `primary`, `onPrimary`
- `secondary`, `onSecondary`
- `tertiary`
- `background`, `surface`
- `error`, `onError`

Example:

- `primary` → main brand color
- `onPrimary` → text/icon color placed on primary

---

### ✅ B) Define colors in theme

In Material 3, set:

```xml

<item name="colorPrimary">@color/my_primary</item>
<item name="colorOnPrimary">@color/my_on_primary</item>
<item name="colorSurface">@color/my_surface</item>
```

---

### ✅ C) Support light/dark mode

Use separate theme resources:

- `values/themes.xml`
- `values-night/themes.xml`

---

### ✅ D) Dynamic color (Material You)

Material 3 supports Dynamic Color (Android 12+):

- theme adapts to user wallpaper

In Compose:

```kotlin
val colorScheme = if (Build.VERSION.SDK_INT >= 31) {
    dynamicLightColorScheme(context)
} else {
    lightColorScheme()
}
```

---

### ⭐ Interview-ready summary

> Material’s color system uses semantic roles like primary/onPrimary/surface to ensure contrast and consistency across
> light/dark modes. In Material 3, dynamic color can adapt the palette from wallpaper on Android 12+.

---

## 5. What are some common Material Design components available in Android?

Material provides ready-made UI building blocks.

### ✅ Common components (XML)

#### Navigation

- `BottomNavigationView`
- `NavigationRailView`
- `NavigationView` (drawer)

#### Inputs

- `TextInputLayout`
- `TextInputEditText`
- `MaterialAutoCompleteTextView`

#### Actions

- `MaterialButton`
- `ExtendedFloatingActionButton`
- `FloatingActionButton`

#### Surfaces

- `MaterialCardView`
- `BottomSheetDialog`
- `MaterialAlertDialogBuilder`

#### Feedback

- `Snackbar`
- `ProgressIndicator` (linear/circular)
- `Chip`, `ChipGroup`

#### Bars

- `MaterialToolbar`
- `TabLayout`

---

### ✅ Compose equivalents

- `Scaffold`
- `TopAppBar`
- `NavigationBar`
- `Card`
- `TextField`
- `SnackbarHost`

---

## 6. How do you implement shadows and elevations in Material Design?

Material uses **elevation** to represent layering and hierarchy.
Elevation impacts:

- shadow
- z-ordering
- interaction priority

---

### ✅ A) Elevation in XML (Views)

Many Material views support elevation:

```xml

<com.google.android.material.card.MaterialCardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:elevation="6dp"
        app:cardElevation="6dp"/>
```

---

### ✅ B) Elevation in Compose

```kotlin
Card(
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
) { }
```

---

### ✅ C) States and lift-on-scroll

Material supports changing elevation based on scroll:

Example (Toolbar):

```xml

<com.google.android.material.appbar.MaterialToolbar
        ...
        app:liftOnScroll="true"/>
```

---

### ✅ Important rules interviewers expect

- Elevation is not just visual → it expresses UI hierarchy
- Shadows should remain consistent and meaningful
- Avoid random elevation — use standards to create predictable surfaces

---

## 7. What is the significance of the Material Theming in Material Design?

Material Theming allows you to customize Material components to match your brand while staying consistent with Material
guidelines.

---

### ✅ Why Material Theming matters

- Ensures consistent design system across app
- Centralizes UI configuration:
    - colors
    - typography
    - shapes
- Allows app-wide style changes without touching each layout

---

### ✅ Key pillars of Material Theming

#### A) Color

Brand colors mapped to roles: primary/surface/error etc.

#### B) Typography

Material typography scale:

- display
- headline
- title
- body
- label

#### C) Shape

Rounded corners, cut corners across components.

Example:

```xml

<item name="shapeAppearanceMediumComponent">@style/ShapeAppearance.MyRounded</item>
```

---

### ✅ Material 3 Theming (ColorScheme)

Material 3 makes theming even stronger with:

- dynamic colors (Material You)
- semantic roles
- tonal palettes

---

### ⭐ Interview-ready statement

> Material Theming is important because it provides a scalable design system—colors, typography, shapes, and component
> styles can be controlled from the theme layer, ensuring consistency and easy branding across the entire app.

---

## Bonus: Best practices interviewers love (real engineering points)

### ✅ 1) Use Material components consistently

Avoid mixing:

- old AppCompat widgets + Material widgets
- inconsistent themes

### ✅ 2) Always maintain accessibility

- touch targets ≥ 48dp
- contrast in text/background
- labels/hints for TextInputLayout

### ✅ 3) Use standard spacing & typography

Spacing consistency improves UX more than decoration.

### ✅ 4) Prefer Material3 for new apps

Material 3 is the modern direction (dynamic color + updated components).

---

## Final Quick Recap

- Material Design principles: **metaphor + hierarchy + meaningful motion**
- Implementation: **Material Components + Theme.Material3**
- Guidelines: standardize UI patterns, accessibility, and responsiveness
- Colors: semantic roles + dynamic color support
- Components: inputs, navigation, surfaces, feedback
- Elevation: hierarchy + shadows + lift-on-scroll
- Material Theming: scalable branding via theme layer

---


---

# RecyclerView & Adapters — Interview Master Guide

> Interview-ready notes on Android **RecyclerView**, Adapters, ViewHolder pattern, LayoutManagers, pagination, ItemDecoration, click handling, and efficient data updates (DiffUtil/ListAdapter).

---

## 1. What is a RecyclerView and how does it differ from ListView?

### ✅ RecyclerView
`RecyclerView` is a modern, flexible, and high-performance component for displaying **large datasets** in lists or grids.

Key features:
- **View recycling** (reuse views instead of creating new ones)
- **LayoutManagers** to control list/grid layouts
- Built-in support for:
  - animations
  - ItemDecoration (dividers/spaces)
  - item touch helpers (swipe/drag)
- Designed for **performance and extensibility**

---

### ✅ ListView (legacy)
ListView is older and less flexible.

Limitations:
- built-in only vertical list
- uses `Adapter` but less control
- complex UI needs manual work
- poor animations support compared to RecyclerView

---

### 🔥 Interview-grade differences

| Feature | RecyclerView | ListView |
|---|---|---|
| Flexibility | ✅ list/grid/staggered | ❌ mostly vertical list |
| ViewHolder | ✅ built-in, required | ⚠️ manual |
| Animations | ✅ strong support | ❌ limited |
| Performance | ✅ better | ⚠️ okay but outdated |
| Layout control | ✅ via LayoutManager | ❌ limited |
| ItemDecoration | ✅ native support | ❌ limited |

📌 Interview-ready line:
> RecyclerView is the modern replacement for ListView. It offers better performance, a mandatory ViewHolder pattern, flexible layout options through LayoutManagers, built-in animations and decorations, and is easier to extend.

---

## 2. How do you set up a RecyclerView with an Adapter?

Setup requires:
1) RecyclerView in XML
2) `LayoutManager`
3) Adapter implementation
4) Optional: ItemDecoration/Animator

---

### ✅ A) XML
```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

---

### ✅ B) Adapter + ViewHolder (basic example)

```kotlin
class UserAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<UserAdapter.UserVH>() {

    class UserVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserVH(view)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.text.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
```

---

### ✅ C) Setup in Activity/Fragment
```kotlin
recyclerView.layoutManager = LinearLayoutManager(requireContext())
recyclerView.adapter = UserAdapter(users)
```

---

### ⭐ Best practice
Prefer `ListAdapter + DiffUtil` for production.
(covered in Q8)

---

## 3. What is the purpose of the ViewHolder pattern in RecyclerView?

### ✅ What is ViewHolder?
`ViewHolder` is a wrapper object that **holds references to item views**.

### ✅ Why it exists (performance)
Without ViewHolder:
- `findViewById()` is called repeatedly during scroll
- causes performance issues and jank

With ViewHolder:
- views are cached once during creation
- re-binding becomes faster
- RecyclerView can recycle holders efficiently

---

### 🔥 Interview-ready answer
> ViewHolder reduces expensive view lookups by caching view references and enables RecyclerView to recycle the same view objects across different positions, improving scrolling performance.

---

## 4. How do you handle item click events in a RecyclerView?

There are multiple clean approaches.

---

### ✅ A) Click inside ViewHolder (recommended)

```kotlin
class UserAdapter(
    private val items: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserVH>() {

    inner class UserVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.text)

        fun bind(item: String) {
            text.text = item
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(items[position])
    }
}
```

Usage:
```kotlin
recyclerView.adapter = UserAdapter(users) { user ->
    // handle click
}
```

---

### ✅ B) AdapterPosition caution
Avoid using `position` directly in click logic because it can become invalid.

Correct:
```kotlin
val pos = bindingAdapterPosition
if (pos != RecyclerView.NO_POSITION) {
    onClick(items[pos])
}
```

---

### ✅ C) Interface-based click listener (older style)
Still valid but more boilerplate.

---

## 5. Can you explain the different types of LayoutManagers available for RecyclerView?

LayoutManager decides how items are laid out.

---

### ✅ A) LinearLayoutManager
- vertical or horizontal list
- most common

```kotlin
recyclerView.layoutManager = LinearLayoutManager(context)
```

---

### ✅ B) GridLayoutManager
- grid structure (rows/columns)
- supports span count

```kotlin
recyclerView.layoutManager = GridLayoutManager(context, 2)
```

Advanced:
- span size lookup for mixed layouts.

---

### ✅ C) StaggeredGridLayoutManager
- Pinterest-style staggered grid
- different item heights

```kotlin
recyclerView.layoutManager =
    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
```

---

### ✅ D) Custom LayoutManager
Used for special cases like carousels, circular lists.

---

### ⭐ Interview-ready comparison
- Linear → simple lists
- Grid → equal-sized grid items
- Staggered grid → variable-sized items

---

## 6. How do you implement pagination in a RecyclerView?

Pagination means loading more items when user reaches end.

### ✅ Common strategies
- scroll listener based pagination
- Paging 3 library (recommended)

---

### ✅ A) Manual pagination using `OnScrollListener`

```kotlin
recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
        val lm = rv.layoutManager as LinearLayoutManager
        val total = lm.itemCount
        val lastVisible = lm.findLastVisibleItemPosition()

        if (!isLoading && lastVisible >= total - 5) {
            loadMore()
        }
    }
})
```

Key concepts:
- `isLoading` flag prevents duplicate calls
- threshold prevents late loading
- update adapter after new data arrives

---

### ✅ B) Paging 3 (best practice)
Paging 3 handles:
- pagination
- caching
- load states
- retry
- error handling

Architecture:
- `PagingSource`
- `Pager`
- `PagingDataAdapter`

Example:
```kotlin
val pager = Pager(PagingConfig(pageSize = 20)) {
    MyPagingSource(api)
}
```

Then:
```kotlin
adapter.submitData(lifecycle, pagingData)
```

📌 Interview-ready line:
> In production I prefer Paging 3 because it manages paging, caching, load states, retries and lifecycle-aware collection cleanly compared to manual pagination.

---

## 7. What are ItemDecorations and how do you use them in RecyclerView?

`ItemDecoration` allows you to add:
- spacing
- dividers
- drawing behind/over items

without modifying item layouts.

---

### ✅ Common use cases
- equal spacing in grid
- divider between rows
- sticky headers (advanced)

---

### ✅ A) DividerItemDecoration
```kotlin
val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
recyclerView.addItemDecoration(divider)
```

---

### ✅ B) Custom spacing decoration
```kotlin
class SpaceDecoration(private val spacePx: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = spacePx
    }
}

recyclerView.addItemDecoration(SpaceDecoration(16))
```

📌 Interview key point:
> ItemDecoration is preferred because spacing is applied centrally and doesn’t pollute item layouts with extra margins.

---

## 8. How do you update the data in a RecyclerView Adapter?

This is one of the most important interview questions.

### ✅ Wrong approach (avoid)
```kotlin
notifyDataSetChanged()
```

Why it's bad:
- refreshes entire list
- no animations
- poor performance

---

### ✅ A) Use DiffUtil (recommended)
DiffUtil calculates minimal changes.

```kotlin
class UserDiff : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(old: User, new: User) = old.id == new.id
    override fun areContentsTheSame(old: User, new: User) = old == new
}
```

Use `ListAdapter`:

```kotlin
class UserAdapter : ListAdapter<User, UserVH>(UserDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH = ...
    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(getItem(position))
    }
}
```

Update list:
```kotlin
adapter.submitList(newList)
```

✅ Benefits:
- only changed rows refresh
- smooth animations
- scalable

---

### ✅ B) Use notifyItem* methods (manual updates)
Good when you know exact change:

- `notifyItemInserted(index)`
- `notifyItemRemoved(index)`
- `notifyItemChanged(index)`
- `notifyItemRangeInserted(start, count)`

---

### ✅ C) Payload updates (advanced optimization)
Used when only part of row changes (e.g., like state)

```kotlin
notifyItemChanged(position, PAYLOAD_LIKE)
```

In adapter:
```kotlin
override fun onBindViewHolder(holder: VH, pos: Int, payloads: MutableList<Any>) {
    if (payloads.isNotEmpty()) {
        // update partial UI
    } else {
        super.onBindViewHolder(holder, pos, payloads)
    }
}
```

---

## Bonus: RecyclerView performance best practices (interview favorite)

### ✅ 1) Use fixed size when possible
```kotlin
recyclerView.setHasFixedSize(true)
```

### ✅ 2) Avoid nested scrolling issues
- For nested lists use:
  - `RecyclerView.RecycledViewPool`
  - disable nested scrolling carefully:
    `recyclerView.isNestedScrollingEnabled = false`

### ✅ 3) Avoid heavy work in `onBindViewHolder()`
Move:
- image loading → Glide/Coil
- formatting → precomputed / background thread

### ✅ 4) Stable IDs (optional)
```kotlin
setHasStableIds(true)
override fun getItemId(position: Int) = getItem(position).id
```
Useful when animations/state should persist.

---

## Final Interview Takeaways
- RecyclerView is flexible + high-performance compared to ListView
- ViewHolder caches views to avoid repeated lookups
- LayoutManagers control arrangement
- Pagination: manual scroll listener or Paging 3 (preferred)
- ItemDecorations add spacing/dividers cleanly
- Data updates: DiffUtil/ListAdapter > notifyDataSetChanged

---
