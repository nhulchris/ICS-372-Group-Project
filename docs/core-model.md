# Core Domain Model — Brew & Bite Cafe System

## Users

### User (Abstract)
- Represents any system user

### Customer
- Places orders

### Barista
- Fulfills orders

### Manager
- Manages menu and inventory

---

## Menu System

### Menu
- Stores all available MenuItems

### MenuItem (Abstract)
- Represents a product
- Attributes:
  - name
  - basePrice

### Beverage
- Extends MenuItem
- Supports:
  - size
  - customizations

### Pastry
- Extends MenuItem
- Simple item (no size/customization)

---

## Ordering System

### Order
- Represents a customer order
- Contains:
  - list of OrderItems
  - totalPrice
  - status

### OrderItem
- Represents one item in an order
- Contains:
  - MenuItem
  - quantity
  - selected options

### OrderQueue
- Manages orders (FIFO)

---

## Inventory System

### InventoryItem
- Represents an ingredient
- Example:
  - milk, coffee beans, sugar

### Inventory
- Tracks ingredient quantities
- Validates availability
- Deducts stock when order is placed

---

## Required Design Patterns

### Factory Method Pattern
- Used to create MenuItem objects
- Class: MenuItemFactory

### Observer Pattern
- Used for real-time updates
- Example:
  - Order updates notify Barista UI
  - Inventory updates notify Manager UI

---

## Architecture

### MVC Pattern
- Model → All domain classes
- View → JavaFX UI
- Controller → Handles user actions
