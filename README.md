# Brew & Bite Cafe System

A JavaFX application for ICS 372 (Object-Oriented Design and Implementation)
that simulates a small cafe with three user roles: **Customer**, **Barista**,
and **Manager**.

## Team Members
- Chris Nhul
- Garvin Yau
- Salman Ahmed

## Tech Stack
- Java 21
- JavaFX 21
- Gson 2.11 (JSON serialization/deserialization)
- Maven

## Architecture & Patterns
- **MVC** — clear separation of model (`com.brewbite.model`),
  view (FXML files under `resources/com/brewbite/view`), and
  controller (`com.brewbite.controller`).
- **Facade** — `CafeSystem` is the single entry point used by all controllers.
- **Singleton** — `CafeSystem.getInstance()`.
- **Factory Method** — `MenuItemFactory` builds beverages and pastries.
- **Observer** — `OrderManager`, `InventoryManager`, and `MenuManager` notify
  the UI controllers when state changes.

## Login Credentials
| Role     | Username   | Password |
|----------|------------|----------|
| Barista  | `barista`  | `123`    |
| Manager  | `manager`  | `123`    |

Customers do not log in; they enter their name on the next screen.

## Build & Run

### Run from source (development)
```
mvn clean javafx:run
```

### Build an executable JAR
```
mvn clean package
```
This produces `target/brewbite-1.0-SNAPSHOT.jar`. Launch it with:
```
java -jar target/brewbite-1.0-SNAPSHOT.jar
```

## Data Persistence
Application data is read from JSON files in two places:

| File                       | Purpose                                              |
|----------------------------|------------------------------------------------------|
| `resources/menu.json`      | Default menu seeded on first launch                  |
| `resources/inventory.json` | Default ingredient inventory seeded on first launch  |
| `~/brewbite-data/*.json`   | Persisted runtime state (orders, inventory and menu changes) |

When the app starts, it reads `~/brewbite-data/` first and falls back to the
bundled defaults on first run. When the app exits cleanly, all state is
saved to `~/brewbite-data/`.
