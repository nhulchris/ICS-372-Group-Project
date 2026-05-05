package com.brewbite.controller;

import com.brewbite.facade.CafeSystem;
import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.*;
import com.brewbite.service.InventoryObserver;
import com.brewbite.service.MenuObserver;
import com.brewbite.service.OrderObserver;
import com.brewbite.util.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Manager dashboard.
 *
 * Implements all three observer interfaces because the manager view shows
 * orders, inventory, and the menu — all of which must update live as the
 * application's state changes.
 */
public class ManagerController
        implements OrderObserver, InventoryObserver, MenuObserver {

    @FXML private ListView<Order> orderListView;
    @FXML private TableView<InventoryRow> inventoryTable;
    @FXML private TableColumn<InventoryRow, String>  ingredientColumn;
    @FXML private TableColumn<InventoryRow, Integer> stockColumn;
    @FXML private TextField amountField;
    @FXML private TextField newIngredientField;
    @FXML private Label statusLabel;
    @FXML private Label salesLabel;

    @FXML private ListView<MenuItem> menuListView;
    @FXML private TextField itemNameField;
    @FXML private TextField itemPriceField;
    @FXML private TextField itemIngredientsField;
    @FXML private ComboBox<String> itemTypeBox;

    private final CafeSystem cafeSystem = CafeSystem.getInstance();

    private final ObservableList<Order> orders = FXCollections.observableArrayList();
    private final ObservableList<InventoryRow> inventory = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> menu = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Orders
        orderListView.setItems(orders);
        cafeSystem.getOrderManager().addObserver(this);
        updateOrders(cafeSystem.getOrderManager().getOrders());

        // Inventory
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        inventoryTable.setItems(inventory);
        cafeSystem.getInventoryManager().addObserver(this);
        updateInventory(cafeSystem.getInventoryManager().getIngredients());

        // Menu
        menuListView.setItems(menu);
        cafeSystem.getMenuManager().addObserver(this);
        updateMenu(cafeSystem.getMenuItems());

        itemTypeBox.setItems(FXCollections.observableArrayList("beverage", "pastry"));
        itemTypeBox.getSelectionModel().select("beverage");

        statusLabel.setText("");
    }

    // -----------------------------------------------------------------
    // OBSERVER METHODS
    // -----------------------------------------------------------------
    @Override
    public void updateOrders(List<Order> updated) {
        orders.setAll(updated);
        refreshSalesLabel();
    }

    @Override
    public void updateInventory(Map<String, Ingredient> ingredients) {
        inventory.clear();
        for (Map.Entry<String, Ingredient> entry : ingredients.entrySet()) {
            inventory.add(new InventoryRow(entry.getKey(), entry.getValue().getQuantity()));
        }
    }

    @Override
    public void updateMenu(List<MenuItem> menuItems) {
        menu.setAll(menuItems);
    }

    private void refreshSalesLabel() {
        double total = 0.0;
        long completed = 0;
        for (Order o : orders) {
            if (o.getStatus() == OrderStatus.COMPLETED) {
                total += o.calculateTotal();
                completed++;
            }
        }
        salesLabel.setText(String.format("Completed: %d  |  Sales: $%.2f", completed, total));
    }

    // -----------------------------------------------------------------
    // INVENTORY ACTIONS
    // -----------------------------------------------------------------
    @FXML
    private void handleRestock() {
        InventoryRow selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an ingredient first.");
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(amountField.getText().trim());
            if (amount <= 0) {
                statusLabel.setText("Amount must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid amount.");
            return;
        }
        cafeSystem.restockIngredient(selected.getName(), amount);
        statusLabel.setText("Restocked " + selected.getName() + " by " + amount + ".");
        amountField.clear();
    }

    @FXML
    private void handleAddIngredient() {
        String name = newIngredientField.getText();
        if (name == null || name.isBlank()) {
            statusLabel.setText("Enter a name for the new ingredient.");
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(amountField.getText().trim());
            if (amount <= 0) {
                statusLabel.setText("Enter a starting amount > 0 in 'Restock amount'.");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Enter a starting amount in 'Restock amount'.");
            return;
        }
        cafeSystem.restockIngredient(name.trim(), amount);
        statusLabel.setText("Added ingredient " + name + " with " + amount + " in stock.");
        newIngredientField.clear();
        amountField.clear();
    }

    // -----------------------------------------------------------------
    // MENU ACTIONS
    // -----------------------------------------------------------------
    @FXML
    private void handleAddMenuItem() {
        String name = itemNameField.getText();
        String priceText = itemPriceField.getText();
        String type = itemTypeBox.getValue();

        if (name == null || name.isBlank() || type == null) {
            statusLabel.setText("Type and name are required.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText.trim());
            if (price < 0) {
                statusLabel.setText("Price must be >= 0.");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid price.");
            return;
        }

        Map<String, Integer> ingredients = parseIngredientsText(itemIngredientsField.getText());

        MenuItem item = MenuItemFactory.createMenuItem(
                type, name.trim(), price, ingredients,
                Size.SMALL, new ArrayList<>()
        );
        cafeSystem.addMenuItem(item);
        statusLabel.setText("Added '" + name + "' to the menu.");
        handleClearForm();
    }

    @FXML
    private void handleRemoveMenuItem() {
        MenuItem selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a menu item to remove.");
            return;
        }
        cafeSystem.removeMenuItem(selected);
        statusLabel.setText("Removed '" + selected.getName() + "'.");
    }

    @FXML
    private void handleModifyMenuItem() {
        MenuItem selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a menu item to modify.");
            return;
        }

        // Apply edits from the form fields if provided.
        String newName = itemNameField.getText();
        if (newName != null && !newName.isBlank()) {
            selected.setName(newName.trim());
        }
        String priceText = itemPriceField.getText();
        if (priceText != null && !priceText.isBlank()) {
            try {
                double newPrice = Double.parseDouble(priceText.trim());
                if (newPrice < 0) {
                    statusLabel.setText("Price must be >= 0.");
                    return;
                }
                selected.setBasePrice(newPrice);
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid price.");
                return;
            }
        }
        String ingText = itemIngredientsField.getText();
        if (ingText != null && !ingText.isBlank()) {
            selected.setIngredients(parseIngredientsText(ingText));
        }

        // Trigger observer refresh so the menu list re-renders the updated item.
        cafeSystem.getMenuManager().notifyMenuChanged();

        statusLabel.setText("Updated '" + selected.getName() + "'.");
        handleClearForm();
    }

    @FXML
    private void handleClearForm() {
        itemNameField.clear();
        itemPriceField.clear();
        itemIngredientsField.clear();
        itemTypeBox.getSelectionModel().select("beverage");
    }

    /**
     * Parses an "ingredients" text field in the format "name:qty, name:qty"
     * into a map. Malformed entries are silently skipped.
     */
    private Map<String, Integer> parseIngredientsText(String text) {
        Map<String, Integer> map = new HashMap<>();
        if (text == null || text.isBlank()) return map;

        for (String entry : text.split(",")) {
            String[] parts = entry.split(":");
            if (parts.length != 2) continue;
            try {
                map.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            } catch (NumberFormatException ignored) {
                // skip malformed entries silently
            }
        }
        return map;
    }

    @FXML
    private void handleBack() {
        cafeSystem.getOrderManager().removeObserver(this);
        cafeSystem.getInventoryManager().removeObserver(this);
        cafeSystem.getMenuManager().removeObserver(this);
        SceneManager.switchScene("/com/brewbite/view/role-selection.fxml");
    }

    // -----------------------------------------------------------------
    // INNER ROW CLASS FOR INVENTORY TABLE
    // -----------------------------------------------------------------
    public static class InventoryRow {
        private final String name;
        private final int amount;

        public InventoryRow(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
        public String getName()  { return name; }
        public int getAmount()   { return amount; }
    }
}
