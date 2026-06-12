# Jetpack Compose UI

---

# Jetpack Compose Layout Hierarchy (Best Practice)

Think of a screen as **5 structural layers**.

```
Activity / NavHost
      ↓
Scaffold (screen structure)
      ↓
Screen Container (Box / Column / Row)
      ↓
Section Layouts (Cards, Lists, Groups)
      ↓
UI Elements (Text, Image, Button, etc.)
```

Now let's break this down properly.

---

# 1. Root Layer — Activity / NavHost

This is where Compose starts.

In modern apps this is usually inside **Navigation**.

```kotlin
setContent {
    MyTheme {
        ProfileScreen()
    }
}
```

Or inside navigation:

```kotlin
NavHost(navController, startDestination = "profile") {
    composable("profile") {
        ProfileScreen()
    }
}
```

**Responsibility**

• Apply theme
• Setup navigation
• Call screen composable

You normally **don't place UI here**.

---

# 2. Screen Structure — `Scaffold`

This is the **recommended outer layout for almost every screen**.

It provides built-in Material layout slots.

```
Scaffold
 ├── TopBar
 ├── BottomBar
 ├── FloatingActionButton
 ├── Drawer
 └── Content
```

Example:

```kotlin
@Composable
fun ProfileScreen() {

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) },
        floatingActionButton = { FloatingActionButton(onClick = {}) {} }
    ) { padding ->

        ProfileContent(
            modifier = Modifier.padding(padding)
        )

    }
}
```

Important detail:

```
Scaffold { paddingValues -> }
```

You **must pass padding to content** to avoid overlapping with top bars.

---

# 3. Screen Container — Layout Composables

Inside the scaffold **content block**, you define the layout structure.

These are the **main layout primitives**.

### Column

Vertical stacking.

```
Text
Text
Button
```

```kotlin
Column {
    Text("Name")
    Text("Email")
}
```

---

### Row

Horizontal layout.

```
Image  Text  Icon
```

```kotlin
Row {
    Image(...)
    Text("John")
}
```

---

### Box

Used for **stacking or overlays**.

Example:

```
Profile Image
      +
Edit Icon on top
```

```kotlin
Box {
    Image(...)
    Icon(...)
}
```

---

### LazyColumn / LazyRow

For lists.

```
RecyclerView equivalent
```

```kotlin
LazyColumn {
    items(users) {
        UserItem(it)
    }
}
```

---

# 4. Section Layouts (Reusable UI Blocks)

This is where you group UI logically.

Examples:

```
Profile Header
Settings Section
Stats Section
Buttons Section
```

Example:

```kotlin
@Composable
fun ProfileHeader() {
    Row {
        Image(...)
        Column {
            Text("John Doe")
            Text("Android Developer")
        }
    }
}
```

Then inside screen:

```kotlin
Column {

    ProfileHeader()

    Spacer(modifier = Modifier.height(16.dp))

    ProfileStats()

}
```

---

# 5. UI Elements (Leaf Nodes)

These are the **actual components**.

Examples:

```
Text
Image
Button
Card
Icon
TextField
Checkbox
```

Example:

```kotlin
Text(
    text = "Hello",
    fontSize = 20.sp
)
```

---

# Full Realistic Example

Let's combine everything.

```kotlin
@Composable
fun ProfileScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            ProfileHeader()

            Spacer(modifier = Modifier.height(24.dp))

            ProfileStats()

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {}) {
                Text("Edit Profile")
            }

        }

    }
}
```

Hierarchy becomes:

```
Activity
 └── ProfileScreen
      └── Scaffold
           ├── TopAppBar
           └── Content
                └── Column
                     ├── ProfileHeader
                     ├── ProfileStats
                     └── Button
```

---

# When to Use Each Layout

| Layout     | Use Case               |
| ---------- | ---------------------- |
| Scaffold   | Whole screen structure |
| Column     | Vertical stacking      |
| Row        | Horizontal alignment   |
| Box        | Overlapping / stacking |
| LazyColumn | Scrollable lists       |
| LazyRow    | Horizontal lists       |

---

# Why Your Texts Overlapped

Because Compose **does not position items automatically**.

Example:

```
Text("Hello")
Text("World")
```

They render **in the same coordinate (0,0)** unless inside a layout.

Correct:

```kotlin
Column {
    Text("Hello")
    Text("World")
}
```

---

# Golden Rule in Compose

Think like **UI Lego blocks**.

```
Screen
   ↓
Layout
   ↓
Section
   ↓
Components
```

Not like the old **deep XML nesting**.

---

# The 90% Real-World Compose Pattern

Most screens follow this structure:

```
Scaffold
   ↓
LazyColumn / Column
   ↓
Cards / Sections
   ↓
Rows / Columns
   ↓
Text / Icons / Images
```

---

# One More Pro Tip (Very Important)

Always structure screens like this:

```
Screen()
   ├── ScreenContent()
   ├── Header()
   ├── Section()
   └── Item()
```

Never write **1000 lines inside one composable**.

Compose apps scale through **small reusable composables**.

---

If you're serious about **mastering Compose UI**, the **next 4 things you should understand deeply** are:

* 1️⃣ **Modifier system (most powerful concept)**
* 2️⃣ **Alignment & Arrangement**
* 3️⃣ **State & recomposition**
* 4️⃣ **Lazy layouts performance**

If you want, I can also show you the **"Compose UI mental model senior Android engineers use"** which makes designing **any screen instantly easy.**
