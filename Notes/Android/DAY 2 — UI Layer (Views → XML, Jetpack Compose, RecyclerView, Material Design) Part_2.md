---
title: "Android Views, ViewGroups, Layouts and Custom Views Interview Notes"
date: 2026-06-12
slug: "android-views-viewgroups-layouts-custom-views-interview-notes"
tags: [ "Notes", "Android", "Views", "ViewGroups", "ConstraintLayout", "Custom Views", "UI Performance" ]
summary: "Personal Android interview notes covering Views, ViewGroups, rendering lifecycle, touch dispatch, ConstraintLayout, LinearLayout, RelativeLayout, custom views, Canvas drawing, and UI performance optimization."
categories: Notes
readTime: 22
---

# Technical interview questions

## DAY 2 — UI Layer (Views → XML, Jetpack Compose, RecyclerView, Material Design)

#### Views and ViewGroups

1. What is the difference between a View and a ViewGroup?
2. Can you explain the View lifecycle in Android?
3. How do you handle user interactions with views?
4. What are the different types of ViewGroups available in Android?
5. How do you optimize view rendering and improve performance?
6. What is the purpose of the onDraw method in a View?

#### Layouts (ConstraintLayout, LinearLayout, RelativeLayout)

1. When would you choose ConstraintLayout over other layout types?
2. How do you create a complex UI using ConstraintLayout?
3. What are the performance implications of using nested LinearLayouts?
4. How do you align views in RelativeLayout?
5. What are the key attributes of ConstraintLayout?
6. How do you chain views in ConstraintLayout?
7. What are the differences between ConstraintLayout, LinearLayout, and RelativeLayout?

#### Custom Views

1. How do you create a custom view in Android?
2. What methods must be overridden when creating a custom view?
3. How do you handle custom attributes for custom views?
4. Can you explain how to draw on a Canvas in a custom view?
5. How do you make a custom view responsive to user interactions?
6. What are some use cases for custom views?

## Answers:

#### Day 2 Answers—Part 2

# Android Views & ViewGroups

> A high-signal, interview-ready reference for Android **Views**, **ViewGroups**, rendering pipeline, touch dispatch,
> and UI performance optimization.

---

## 1. What is the difference between a View and a ViewGroup?

In Android, UI is built as a **View hierarchy** (a tree structure).

- **View** → typically a leaf node that represents a **single UI element**
- **ViewGroup** → a container node that can hold and **arrange child views**

---

### ✅ View (UI widget / element)

A **View** is the base class for all UI widgets.

**Responsibilities**

- Occupies a rectangular area on screen
- **Measures** itself (width/height)
- **Draws** itself
- Handles interaction **for itself** (click, touch, focus)

**Examples**

- `TextView`, `Button`, `ImageView`, `EditText`, `ProgressBar`

📌 Think of a View as: **one UI component**.

---

### ✅ ViewGroup (layout / container)

A **ViewGroup** is a subclass of `View` that acts as a container.

**Responsibilities**

- Can contain **multiple Views / ViewGroups**
- **Measures** its children
- **Positions (layouts)** its children
- **Draws children** (via `dispatchDraw()`)
- **Dispatches input events** to children

**Examples**

- `ConstraintLayout`, `LinearLayout`, `FrameLayout`, `CoordinatorLayout`, `RecyclerView`

📌 Think of a ViewGroup as: **layout manager + parent container**.

---

### ⭐ Key differences (interview-grade)

| Topic                 | View              | ViewGroup                  |
|-----------------------|-------------------|----------------------------|
| Role                  | Single UI element | Container + layout         |
| Children support      | ❌ No              | ✅ Yes                      |
| Layout responsibility | ❌ None            | ✅ Positions children       |
| Input handling        | Handles itself    | Dispatches to children     |
| Rendering             | Draws itself      | Draws itself + children    |
| Cost                  | Lighter           | Heavier (manages children) |

---

### 🔥 Important internals interviewers like

- **ViewGroup is also a View**, so it participates in measure/layout/draw like any other View.
- Each child has `LayoutParams` defined by the parent:
    - `LinearLayout.LayoutParams`, `ConstraintLayout.LayoutParams`, etc.
- Every View has a `ViewParent` (usually a ViewGroup).

---

## 2. Can you explain the View lifecycle in Android?

The View lifecycle describes how Android **creates**, **measures**, **positions**, and **renders** views, and how it
refreshes them efficiently.

Pipeline overview:

1) Inflate / Create
2) Attach to window
3) Measure
4) Layout
5) Draw
6) Updates (`invalidate()` / `requestLayout()`)
7) Detach from window

---

### A) Inflation / Creation phase

Views are created:

- From XML using `LayoutInflater`
- Programmatically using constructors

**Common constructors**

- `View(Context)`
- `View(Context, AttributeSet)`
- `View(Context, AttributeSet, defStyleAttr)`

**Hooks**

- `init {}` (Kotlin): initialize paints, read attrs
- `onFinishInflate()`: called after inflation (especially useful for custom ViewGroups)

---

### B) Attach / Detach phase

#### ✅ `onAttachedToWindow()`

Called when the view becomes part of a window.

Use for:

- register listeners (`ViewTreeObserver`)
- start animations
- start periodic updates

#### ✅ `onDetachedFromWindow()`

Called when view is removed.

Use for:

- stop animations
- unregister listeners
- cancel coroutines/handlers (avoid leaks)

---

### C) Measure phase → `onMeasure()`

**Goal:** Decide the view’s size.

Flow:

- Parent calls `child.measure(widthMeasureSpec, heightMeasureSpec)`
- Framework triggers `onMeasure()`

`MeasureSpec` contains:

- **EXACTLY** → fixed size or match_parent
- **AT_MOST** → wrap_content within max limit
- **UNSPECIFIED** → parent doesn’t impose constraints (rare)

Result stored in:

- `measuredWidth`, `measuredHeight`

📌 Key points:

- Measure decides **size**, not position.
- Custom views must call `setMeasuredDimension(w, h)`.

---

### D) Layout phase → `onLayout()`

**Goal:** Decide the view’s position.

Layout assigns:

- `left, top, right, bottom`

For normal Views, `onLayout()` is rarely overridden.
For ViewGroups, `onLayout()` is **critical** (positions children).

📌 ViewGroup typically calls:

- `child.layout(l, t, r, b)` for each child.

---

### E) Draw phase → `onDraw()` / `dispatchDraw()`

Android performs traversal from `ViewRootImpl.performTraversals()`:

- `performMeasure()`
- `performLayout()`
- `performDraw()`

Call sequence:

- `draw(Canvas)`
    1) draw background
    2) call `onDraw(canvas)` for View content
    3) ViewGroup calls `dispatchDraw(canvas)` to draw children
    4) draw foreground (scrollbars/ripple/etc.)

✅ `onDraw()` → draw View content  
✅ `dispatchDraw()` → draw ViewGroup’s children

---

### F) Size change callback → `onSizeChanged()`

Called when dimensions change (after layout).
Good for:

- recalculating geometry
- recomputing paths/gradients based on size

---

### G) Update methods: `invalidate()` vs `requestLayout()`

#### ✅ `invalidate()`

Triggers **redraw only**

- results in `onDraw()`

Use when:

- visuals changed (colors, progress, alpha)

#### ✅ `requestLayout()`

Triggers **measure + layout + draw**

Use when:

- size may change (text length, padding, layout params)

---

## 3. How do you handle user interactions with views?

Android uses a **dispatch system** for touch events, so input can flow correctly through parents and children.

---

### A) Click and long-click

```kotlin
view.setOnClickListener {
    // handle click
}

view.setOnLongClickListener {
    true // consume event
}
```

---

### B) Touch dispatch pipeline (VERY important)

Event path:

1) `Activity.dispatchTouchEvent()`
2) Window → `DecorView`
3) Parent `ViewGroup.dispatchTouchEvent()`
4) `onInterceptTouchEvent()` decides interception (ViewGroup only)
5) Child `dispatchTouchEvent()`
6) Child `onTouchEvent()` handles/consumes

---

### C) `onTouchEvent()` basic handling

```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
        MotionEvent.ACTION_DOWN -> return true
        MotionEvent.ACTION_MOVE -> { /* update */
        }
        MotionEvent.ACTION_UP -> {
            performClick()
            return true
        }
    }
    return super.onTouchEvent(event)
}
```

📌 Key point:

- return `true` on ACTION_DOWN to keep receiving the rest of the gesture.

---

### D) `performClick()` (Accessibility)

For custom views, call `performClick()` on ACTION_UP to support accessibility.

```kotlin
override fun performClick(): Boolean {
    super.performClick()
    return true
}
```

---

### E) Gestures using `GestureDetector`

```kotlin
private val detector = GestureDetector(
    context,
    object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean = true
    }
)

override fun onTouchEvent(event: MotionEvent): Boolean {
    return detector.onTouchEvent(event) || super.onTouchEvent(event)
}
```

---

### F) Resolving touch conflicts

When a child wants to handle scroll/swipe:

```kotlin
parent.requestDisallowInterceptTouchEvent(true)
```

Used in:

- nested scrolling scenarios
- horizontal swipe inside vertical scroll

---

## 4. What are the different types of ViewGroups available in Android?

ViewGroups can be classified by purpose.

---

### A) Layout ViewGroups

#### ✅ ConstraintLayout (modern default)

- flat hierarchy
- best for complex UI
- chains/guidelines/barriers

#### ✅ LinearLayout

- simple vertical/horizontal stacking
- avoid deep nesting
- weights can be expensive

#### ✅ FrameLayout

- stacks children
- good for overlays / fragment containers

#### ⚠️ RelativeLayout (legacy)

Mostly replaced by ConstraintLayout.

---

### B) Scrolling containers

- `NestedScrollView` (modern)
- `ScrollView` (older)

Avoid nesting multiple scroll containers.

---

### C) High-performance lists

#### ✅ RecyclerView

- view recycling
- `LayoutManager` controls layout
- efficient updates with DiffUtil/ListAdapter

---

### D) Behavior containers

- `CoordinatorLayout` (collapsing toolbar, snackbars)
- `DrawerLayout` (navigation drawer)
- `ViewPager2` (swipe pages)
- `MotionLayout` (advanced animations)

---

## 5. How do you optimize view rendering and improve performance?

Performance = fewer layout passes + less overdraw + less work on main thread.

---

### A) Reduce hierarchy depth (Top priority)

Deep hierarchies cause slower traversal.

✅ Use:

- ConstraintLayout to flatten
- `<merge>` to remove wrappers

---

### B) Reuse layouts efficiently

#### ✅ `<include>`

reuse layout blocks

#### ✅ `<merge>`

eliminates an extra parent container

#### ✅ `ViewStub`

lazy inflate heavy UI only when needed

---

### C) Reduce layout passes

Avoid repeated `requestLayout()` calls.
Batch UI updates when possible.

---

### D) Reduce overdraw

Overdraw = drawing pixels multiple times.

Fix by:

- removing stacked backgrounds
- avoiding unnecessary overlays
- avoiding heavy alpha layers

Tool:

- Developer options → **Debug GPU overdraw**

---

### E) Avoid heavy work in `onDraw()`

❌ Don’t allocate objects in onDraw
✅ reuse:

- `Paint`, `Rect`, `Path`

---

### F) RecyclerView optimizations (High ROI)

✅ Prefer:

- `ListAdapter + DiffUtil`
- stable IDs (when suitable)
- payload updates

Avoid:

- `notifyDataSetChanged()`

Other:

- `setHasFixedSize(true)` when size doesn’t change
- shared `RecycledViewPool` for nested lists

---

### G) Animation best practices

Prefer property animations (`translationX`, `alpha`) over layout param animations (relayout cost).

---

### H) Tooling (industry-grade)

- Layout Inspector
- Android Studio Profiler
- Perfetto / Systrace
- JankStats
- Macrobenchmark

---

## 6. What is the purpose of the onDraw method in a View?

`onDraw(Canvas)` is where a View renders its **custom pixels**.

---

### A) What happens inside `onDraw()`

- draw shapes
- draw text
- draw bitmaps
- draw graphs / progress

Example:

```kotlin
override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawCircle(cx, cy, radius, paint)
}
```

---

### B) When `onDraw()` is called

- first time rendering
- after `invalidate()`
- after animations / visual updates

---

### C) invalidate variants

- `invalidate()` → redraw on UI thread
- `postInvalidate()` → safe from background thread
- `postInvalidateOnAnimation()` → redraw next frame (smooth)

---

### D) Custom View pipeline

1) `onMeasure()` → decide size
2) `onSizeChanged()` → compute geometry
3) `onDraw()` → draw pixels

---

### E) `setWillNotDraw(false)` (ViewGroup case)

Some ViewGroups skip drawing by default.
If you want a ViewGroup to draw in `onDraw()`:

```kotlin
setWillNotDraw(false)
```

---

### Golden rules (interview-grade)

✅ Keep `onDraw()`:

- fast
- deterministic
- allocation-free

❌ Never:

- decode bitmaps
- allocate objects repeatedly
- do heavy computation

---

## Final quick recap (what interviewers love)

- **Measure = size**
- **Layout = position**
- **Draw = pixels**
- **invalidate() = redraw**
- **requestLayout() = re-measure + re-layout**
- **Touch dispatch = dispatch → intercept → touch**
- **Performance = flat hierarchy + low overdraw + no main-thread heavy work**

---

# Android Layouts (ConstraintLayout, LinearLayout, RelativeLayout)

> High-signal, interview-ready notes for Android **Layouts**, including **ConstraintLayout internals**, responsive UI
> strategies, and layout performance optimization.

---

## 1. When would you choose ConstraintLayout over other layout types?

ConstraintLayout is the **default modern choice** for XML UI when you need **flexibility + performance**. It allows
complex alignments with a **flat hierarchy**, which reduces measure/layout cost.

### ✅ Choose ConstraintLayout when

- The UI is **complex** (multiple alignments, center + edges, baseline alignment).
- You want a **flat hierarchy** (avoid nesting).
- You need **responsive UI** across screen sizes.
- You benefit from advanced tools:
    - **Chains** (distribution)
    - **Barriers** (dynamic alignment)
    - **Guidelines** (responsive anchors)
    - **Groups** (visibility toggles)
    - **Flow** (wrap-like behaviour)
    - **DimensionRatio** (aspect ratio)

### ✅ Choose alternatives when

- **LinearLayout** → very simple vertical/horizontal stacking with few views.
- **FrameLayout** → stacking/overlay (loading on top, fragment container).
- **RelativeLayout** → legacy layouts only (ConstraintLayout is preferred today).

📌 Interview-ready line:
> I choose ConstraintLayout for complex UI because it reduces nested hierarchies and gives fine-grained positioning with
> better rendering performance.

---

## 2. How do you create a complex UI using ConstraintLayout?

The key to complex UI is combining **constraints** with helper features like **chains/guidelines/barriers**.

### ✅ Core rules

- Every view should have at least:
    - **one horizontal constraint** (start/end)
    - **one vertical constraint** (top/bottom)
- Use **0dp** for match constraints:
    - `android:layout_width="0dp"`
    - `android:layout_height="0dp"`

### ✅ Typical building blocks

#### A) Use match-constraints (0dp) for flexible sizing

- Great for text areas that should expand/shrink based on available space.

#### B) Use Guidelines for responsive anchors

```xml

<androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.5"/>
```

#### C) Use Barriers for dynamic layouts

A barrier aligns views based on the largest/smallest view in a set.

#### D) Use Chains to distribute views

Perfect for toolbars, button rows, filters.

### ✅ Example: Profile header (complex but flat)

```xml

<androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp">

    <ImageView
            android:id="@+id/avatar"
            android:layout_width="56dp"
            android:layout_height="56dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"/>

    <TextView
            android:id="@+id/name"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="Vishnu"
            app:layout_constraintStart_toEndOf="@id/avatar"
            app:layout_constraintEnd_toStartOf="@id/action"
            app:layout_constraintTop_toTopOf="@id/avatar"/>

    <TextView
            android:id="@+id/subtitle"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="Android Developer"
            app:layout_constraintStart_toStartOf="@id/name"
            app:layout_constraintEnd_toEndOf="@id/name"
            app:layout_constraintTop_toBottomOf="@id/name"/>

    <Button
            android:id="@+id/action"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Follow"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

🔥 Why this is good:

- No nesting
- Dynamic sizing using `0dp`
- Easy to scale for tablets

---

## 3. What are the performance implications of using nested LinearLayouts?

Nested LinearLayouts can create **deep view hierarchies** and cause extra **measure/layout passes**, especially with
weights.

### ✅ Why nesting is expensive

Android UI traversal:

1) Measure
2) Layout
3) Draw

With nesting:

- More nodes in the tree
- Each pass becomes heavier
- Updates trigger larger re-traversals

### ⚠️ Weight makes it worse

`layout_weight` may cause **multi-pass measurement**, because Android often needs to measure children twice to determine
final sizes.

### ✅ Best practices

- Flatten layouts using **ConstraintLayout**
- Avoid deep nesting of LinearLayouts
- Avoid weights unless necessary
- Use:
    - `<merge>` to remove extra wrappers
    - `ViewStub` to lazily inflate heavy sections

📌 Interview-ready statement:
> Nested LinearLayouts increase hierarchy depth and traversal cost. If weights are used, measurement often becomes
> multi-pass. ConstraintLayout improves performance by flattening the hierarchy.

---

## 4. How do you align views in RelativeLayout?

RelativeLayout positions views **relative to the parent** or **relative to other views** using attributes.

### ✅ Align relative to parent

- `android:layout_alignParentStart="true"`
- `android:layout_alignParentEnd="true"`
- `android:layout_alignParentTop="true"`
- `android:layout_alignParentBottom="true"`
- `android:layout_centerInParent="true"`
- `android:layout_centerHorizontal="true"`
- `android:layout_centerVertical="true"`

### ✅ Align relative to other views

- `android:layout_toEndOf="@id/view1"`
- `android:layout_toStartOf="@id/view1"`
- `android:layout_below="@id/view1"`
- `android:layout_above="@id/view1"`
- `android:layout_alignTop="@id/view1"`
- `android:layout_alignBottom="@id/view1"`
- `android:layout_alignBaseline="@id/view1"`

### ✅ Example

```xml

<RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    <TextView
            android:id="@+id/title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Title"
            android:layout_alignParentStart="true"/>

    <Button
            android:id="@+id/btn"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Action"
            android:layout_toEndOf="@id/title"
            android:layout_alignBaseline="@id/title"/>

</RelativeLayout>
```

📌 Modern interview note:
> RelativeLayout is mostly legacy. ConstraintLayout replaces it with more flexible constraints and better performance
> for complex screens.

---

## 5. What are the key attributes of ConstraintLayout?

ConstraintLayout uses `app:layout_constraint...` attributes.

### ✅ Position constraints

- `layout_constraintStart_toStartOf`
- `layout_constraintStart_toEndOf`
- `layout_constraintEnd_toStartOf`
- `layout_constraintEnd_toEndOf`
- `layout_constraintTop_toTopOf`
- `layout_constraintTop_toBottomOf`
- `layout_constraintBottom_toTopOf`
- `layout_constraintBottom_toBottomOf`

### ✅ Bias (fine alignment between constraints)

- `layout_constraintHorizontal_bias="0.0..1.0"`
- `layout_constraintVertical_bias="0.0..1.0"`

### ✅ Match constraints (0dp)

- `android:layout_width="0dp"`
- `android:layout_height="0dp"`

### ✅ Dimension ratio

- `layout_constraintDimensionRatio="16:9"`

### ✅ Gone margins (layout stability)

- `layout_goneMarginStart="16dp"`
- `layout_goneMarginTop="8dp"`

### ✅ Chain style

- `layout_constraintHorizontal_chainStyle="spread|spread_inside|packed"`
- `layout_constraintVertical_chainStyle="spread|spread_inside|packed"`

---

## 6. How do you chain views in ConstraintLayout?

Chains allow multiple views to behave like a group and distribute space automatically.

### ✅ Creating a chain (horizontal)

1) Constrain each view start/end to adjacent views
2) Constrain first view to parent start and last view to parent end
3) Set chain style on the first view

### ✅ Example: 3-button horizontal chain

```xml

<Button
        android:id="@+id/b1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="One"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/b2"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintHorizontal_chainStyle="spread"/>

<Button
android:id="@+id/b2"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="Two"
app:layout_constraintStart_toEndOf="@id/b1"
app:layout_constraintEnd_toStartOf="@id/b3"
app:layout_constraintTop_toTopOf="@id/b1"/>

<Button
android:id="@+id/b3"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="Three"
app:layout_constraintStart_toEndOf="@id/b2"
app:layout_constraintEnd_toEndOf="parent"
app:layout_constraintTop_toTopOf="@id/b1"/>
```

### ✅ Chain styles

- **spread** → equal distribution across space
- **spread_inside** → edges pinned, space inside
- **packed** → packed together with bias control

### ✅ Weighted chains

To distribute space proportionally:

- Set width = `0dp`
- Add:
    - `app:layout_constraintHorizontal_weight="1"`

---

## 7. What are the differences between ConstraintLayout, LinearLayout, and RelativeLayout?

### ✅ High-level summary

- **LinearLayout**: simple stacking (row/column)
- **RelativeLayout**: relative positioning (legacy)
- **ConstraintLayout**: powerful constraint-based system (modern standard)

### ✅ Comparison table

| Feature               | ConstraintLayout        | LinearLayout           | RelativeLayout            |
|-----------------------|-------------------------|------------------------|---------------------------|
| Best use case         | Complex + responsive UI | Simple linear screens  | Legacy relative alignment |
| Nesting required      | ✅ Minimal               | ❌ Often high           | ❌ Often high              |
| Performance           | ✅ Best for complex      | ✅ best for simple      | ⚠️ ok but legacy          |
| Flexibility           | ✅ Highest               | ❌ low                  | ⚠️ medium                 |
| Modern recommendation | ✅ Yes                   | ✅ yes (simple screens) | ❌ mostly no               |

### 🎯 Interview-ready closing line

> For simple forms I may use LinearLayout, but for most production UIs I prefer ConstraintLayout because it supports
> complex positioning with a flat hierarchy. RelativeLayout is mostly legacy as ConstraintLayout provides a more
> scalable
> solution.

---

# Android Custom Views — Interview Master Guide

> High-signal, interview-ready notes for Android **Custom Views**, including lifecycle hooks (`onMeasure`, `onDraw`),
> custom attributes, Canvas drawing, touch handling, and real-world use cases.

---

## 1. How do you create a custom view in Android?

Creating a custom view means building your own UI component by extending `View` (or a subclass) and implementing custom
measurement, drawing, and interaction logic.

### ✅ Step-by-step approach

#### A) Choose base class

- Extend `View` → if you will fully draw everything yourself
- Extend `TextView`, `ImageView`, etc. → if you want built-in functionality + custom behavior
- Extend `ViewGroup` → if it contains other views and you control layout

#### B) Add constructors (important for XML inflation)

A proper custom view should support XML + themes:

```kotlin
class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // init here
}
```

#### C) Initialize state + drawing tools

Create reusable objects like:

- `Paint`
- `Rect`
- `Path`

✅ Do this once in init, not in `onDraw()`.

---

### ✅ Minimal working custom view example: CircleView

```kotlin
class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    var radius = 80f
        set(value) {
            field = value
            invalidate() // redraw
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        canvas.drawCircle(cx, cy, radius, paint)
    }
}
```

---

## 2. What methods must be overridden when creating a custom view?

Not all are mandatory, but interviewers expect you to know the **key hooks**.

### ✅ Most important overrides

#### A) `onDraw(canvas: Canvas)`

- Required if you need custom drawing
- Called whenever the view needs to render pixels

#### B) `onMeasure(widthMeasureSpec, heightMeasureSpec)`

- Required if your view needs custom sizing behavior
- Must call `setMeasuredDimension(w, h)`

#### C) `onSizeChanged(w, h, oldw, oldh)`

- Called when the view’s size changes
- Best place to recompute geometry based on size

---

### ✅ Interaction overrides (if interactive)

- `onTouchEvent(event: MotionEvent)`
- `performClick()` → accessibility support

---

### ✅ Window lifecycle (good for cleanup)

- `onAttachedToWindow()`
- `onDetachedFromWindow()` → stop animations, remove callbacks

---

### ⭐ Quick mapping: which method does what?

| Method                   | Purpose                               |
|--------------------------|---------------------------------------|
| `onMeasure()`            | decides **size**                      |
| `onLayout()` (ViewGroup) | decides **position** of children      |
| `onSizeChanged()`        | recompute geometry after size changes |
| `onDraw()`               | draw pixels                           |
| `invalidate()`           | redraw                                |
| `requestLayout()`        | re-measure + re-layout                |

---

## 3. How do you handle custom attributes for custom views?

Custom attributes allow your view to be configured from XML like standard Android views.

### ✅ Steps

#### A) Define attributes in `res/values/attrs.xml`

```xml

<resources>
    <declare-styleable name="CircleView">
        <attr name="circleColor" format="color"/>
        <attr name="circleRadius" format="dimension"/>
    </declare-styleable>
</resources>
```

#### B) Read attributes in the constructor

```kotlin
private var circleColor: Int = Color.RED
private var circleRadius: Float = 80f

init {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.CircleView,
        0,
        0
    ).apply {
        try {
            circleColor = getColor(
                R.styleable.CircleView_circleColor,
                Color.RED
            )
            circleRadius = getDimension(
                R.styleable.CircleView_circleRadius,
                80f
            )
        } finally {
            recycle()
        }
    }

    paint.color = circleColor
    radius = circleRadius
}
```

📌 Always call `recycle()` to avoid memory leaks.

#### C) Use it in XML

```xml

<com.example.CircleView
        android:layout_width="120dp"
        android:layout_height="120dp"
        app:circleColor="@color/purple_500"
        app:circleRadius="40dp"/>
```

---

## 4. Can you explain how to draw on a Canvas in a custom view?

Canvas is the drawing surface used inside `onDraw(canvas)`.

### ✅ Canvas basics

The coordinate system:

- `(0,0)` is the **top-left**
- +X → right
- +Y → down

### ✅ Most used Canvas APIs

- `drawRect()`
- `drawCircle()`
- `drawLine()`
- `drawText()`
- `drawPath()`
- `drawBitmap()`

---

### ✅ Paint controls appearance

Paint defines:

- color
- style (fill/stroke)
- stroke width
- text size

```kotlin
val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.BLUE
    style = Paint.Style.STROKE
    strokeWidth = 8f
}
```

---

### ✅ Example: Draw a progress arc

```kotlin
override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    val padding = 20f
    val rect = RectF(padding, padding, width - padding, height - padding)

    // background circle
    paint.color = Color.LTGRAY
    canvas.drawArc(rect, 0f, 360f, false, paint)

    // progress arc
    paint.color = Color.GREEN
    canvas.drawArc(rect, -90f, progress * 360f, false, paint)
}
```

---

### ✅ Canvas transformations (advanced but useful)

- `canvas.save()`
- `canvas.translate(dx, dy)`
- `canvas.rotate(deg)`
- `canvas.scale(sx, sy)`
- `canvas.restore()`

Interview tip:
> Always use `save()` and `restore()` when applying transformations so it doesn’t affect later drawing.

---

## 5. How do you make a custom view responsive to user interactions?

Custom views become interactive by implementing touch/click behavior.

### ✅ A) Make it clickable

In init:

```kotlin
isClickable = true
isFocusable = true
```

---

### ✅ B) Handle gestures with `onTouchEvent()`

```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
        MotionEvent.ACTION_DOWN -> {
            // start gesture
            return true
        }
        MotionEvent.ACTION_MOVE -> {
            // update state
            invalidate()
        }
        MotionEvent.ACTION_UP -> {
            performClick()
            return true
        }
    }
    return super.onTouchEvent(event)
}
```

📌 Return `true` on ACTION_DOWN, otherwise you won’t get MOVE/UP events.

---

### ✅ C) Always override `performClick()` (accessibility)

```kotlin
override fun performClick(): Boolean {
    super.performClick()
    // trigger listener
    return true
}
```

---

### ✅ D) Use GestureDetector for advanced gestures

Example: double tap / fling.

```kotlin
private val detector = GestureDetector(
    context,
    object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            toggleState()
            invalidate()
            return true
        }
    })

override fun onTouchEvent(event: MotionEvent): Boolean {
    return detector.onTouchEvent(event) || super.onTouchEvent(event)
}
```

---

### ✅ E) Expose callbacks (clean architecture)

Good custom view design provides listener APIs.

```kotlin
fun interface OnValueChangeListener {
    fun onChanged(value: Float)
}

var onValueChangeListener: OnValueChangeListener? = null
```

---

## 6. What are some use cases for custom views?

Custom views are used when default Android widgets cannot meet design/UX requirements.

### ✅ Common real-world use cases

- Custom **progress bar** / donut chart
- Custom **slider** / range selector
- **Speedometer** / gauge UI (automotive style)
- Signature pad / drawing canvas
- Custom charts (line chart, bar chart)
- Custom animated buttons (like morphing FAB)
- OTP input view (single UI element split into boxes)
- Audio waveform view
- Calendar day picker view

---

### ✅ When custom views are the right choice

- You need **pixel-perfect UI**
- You want **high performance** for repeated rendering
- You want a reusable UI component across the app

### ❌ When NOT to use a custom view

- When it can be built easily using standard views + styles
- If it makes accessibility harder without need
- If it increases maintenance cost unnecessarily

---

## Bonus: Performance best practices for custom views (interview favorite)

### ✅ 1) Avoid allocations in `onDraw()`

❌ Do NOT create `Paint`, `Rect`, `Path`, arrays inside `onDraw()`

✅ Pre-create and reuse:

- `Paint`
- `RectF`
- `Path`

---

### ✅ 2) Use correct invalidation

- `invalidate()` → redraw
- `requestLayout()` → size changes
- `postInvalidateOnAnimation()` → smooth animation frames

---

### ✅ 3) Cache heavy objects

- gradients
- shaders
- bitmaps

Use `onSizeChanged()` to rebuild based on size.

---

### ✅ 4) Support accessibility

- content description
- `performClick()`
- focus handling

---

## Final Interview Takeaways

- Custom View = extend `View`, override `onMeasure()` + `onDraw()`
- Custom attrs = `attrs.xml` + `obtainStyledAttributes()` + `recycle()`
- Canvas drawing = `Canvas` + `Paint` (keep it fast)
- Interactions = `onTouchEvent()` + `performClick()`
- Real-world custom views = charts, gauges, OTP, signature pad

---
