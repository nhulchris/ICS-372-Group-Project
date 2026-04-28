# Design Patterns — Brew & Bite Cafe System

## Observer Pattern

### Purpose
Used to enable real-time updates across the system.

### Implementation

- Subject:
  - OrderQueue
  - Inventory

- Observers:
  - Barista UI
  - Manager UI

### Behavior

- When a new order is added:
  - OrderQueue notifies Barista UI

- When inventory changes:
  - Inventory notifies Manager UI

### Justification

The **Observer Pattern** is used to decouple the system components while allowing automatic updates. This ensures that UI components stay synchronized with the system state without tightly coupling them to the core logic.

---

## Factory Method Pattern

### Purpose
Used to centralize creation of MenuItem objects.

### Implementation

- Creator:
  - MenuItemFactory

- Products:
  - Beverage
  - Pastry

### Behavior

- Based on input type:
  - Factory creates correct MenuItem subclass

### Justification

The **Factory Method Pattern** removes direct object creation from the client and centralizes it in one location. This improves maintainability and supports extensibility when adding new menu items.

---

## MVC Architecture

### Model
- Order
- OrderItem
- MenuItem
- Inventory

### View
- JavaFX UI (FXML)

### Controller
- Handles user input
- Updates Model and View

### Justification

The **Model-View-Controller (MVC)** pattern separates concerns between data, UI, and logic. This improves maintainability, testability, and scalability of the system.
