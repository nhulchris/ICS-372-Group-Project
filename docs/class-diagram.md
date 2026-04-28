# UML Class Diagram — Brew & Bite Cafe System

```mermaid
classDiagram

class User {
  <<abstract>>
}

class Customer
class Barista
class Manager

User <|-- Customer
User <|-- Barista
User <|-- Manager

class Menu {
  -menuItems: List<MenuItem>
  +addItem(item: MenuItem)
  +removeItem(item: MenuItem)
}

class MenuItem {
  <<abstract>>
  -name: String
  -basePrice: double
  +getPrice(): double
}

class Beverage {
  -size: String
  -customizations: List<String>
}

class Pastry

MenuItem <|-- Beverage
MenuItem <|-- Pastry

Menu --> MenuItem

class Order {
  -items: List<OrderItem>
  -status: String
  -totalPrice: double
  +calculateTotal(): double
}

class OrderItem {
  -menuItem: MenuItem
  -quantity: int
}

Order --> OrderItem
OrderItem --> MenuItem

class OrderQueue {
  -orders: Queue<Order>
  +addOrder(order: Order)
  +getNextOrder(): Order
}

OrderQueue --> Order

class InventoryItem {
  -name: String
  -quantity: int
}

class Inventory {
  -items: List<InventoryItem>
  +checkAvailability(item: MenuItem): boolean
  +deductIngredients(item: MenuItem)
}

Inventory --> InventoryItem

class MenuItemFactory {
  +createItem(type: String): MenuItem
}

MenuItemFactory --> MenuItem
