package com.brewbite.controller;

import com.brewbite.facade.CafeSystem;
import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.Beverage;
import com.brewbite.model.Ingredient;
import com.brewbite.model.Order;
import com.brewbite.model.Pastry;
import com.brewbite.service.InventoryObserver;
import com.brewbite.service.MenuObserver;
import com.brewbite.util.SceneManager;
import com.brewbite.model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagerController implements InventoryObserver, MenuObserver {

    @FXML
    private ListView<Order> orderListView;

    @FXML
    private TableView<InventoryRow> inventoryTable;

    @FXML
    private TableColumn<InventoryRow, String> ingredientColumn;

    @FXML
    private TableColumn<InventoryRow, Integer> stockColumn;

    @FXML
    private TextField amountField;

    @FXML
    private Label statusLabel;

    @FXML
    private ListView<MenuItem> menuListView;

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField itemPriceField;

    @FXML
    private ComboBox<String> itemTypeBox;

    private final CafeSystem cafeSystem = CafeSystem.getInstance();

    private final ObservableList<Order> orders =
            FXCollections.observableArrayList();

    private final ObservableList<InventoryRow> inventory =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // =====================
        // ORDERS
        // =====================
        orders.setAll(cafeSystem.getOrderManager().getOrders());
        orderListView.setItems(orders);

        // =====================
        // INVENTORY TABLE
        // =====================
        ingredientColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        stockColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );

        loadInventory();
        inventoryTable.setItems(inventory);
        statusLabel.setText("");
        menuListView.setItems(FXCollections.observableArrayList(cafeSystem.getMenuItems()));

        itemTypeBox.setItems(FXCollections.observableArrayList("beverage", "pastry"));
        cafeSystem.getOrderManager().addObserver(updatedOrders -> {orders.setAll(updatedOrders);});
        cafeSystem.getInventoryManager().addObserver(this);
        cafeSystem.getMenuManager().addObserver(this);
    }

    private void loadInventory() {

        inventory.clear();

        for (Map.Entry<String, Ingredient> entry :
        cafeSystem.getInventoryManager().getIngredients().entrySet()) {

            inventory.add(new InventoryRow(entry.getKey(), entry.getValue().getQuantity()));
        }
    }

    @FXML
    private void handleRestock() {

        InventoryRow selected = inventoryTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Select an ingredient first.");
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(amountField.getText());

            if (amount <= 0) {
                statusLabel.setText("Amount must be > 0.");
                return;
            }

        } catch (Exception e) {
            statusLabel.setText("Invalid amount.");
            return;
        }

        cafeSystem.restockIngredient(selected.getName(), amount);

        statusLabel.setText("Restocked " + selected.getName());

        amountField.clear();


    }

    // =====================
    // INNER CLASS FOR TABLE
    // =====================
    public static class InventoryRow {

        private String name;
        private int amount;

        public InventoryRow(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public int getAmount() {
            return amount;
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/brewbite/view/role-selection.fxml");
    }

    @Override
    public void updateInventory(Map<String, Ingredient> ingredients) {

        inventory.clear();

        for (Map.Entry<String, Ingredient> entry : ingredients.entrySet()) {
            inventory.add(new InventoryRow(
                    entry.getKey(),
                    entry.getValue().getQuantity()
            ));
        }

        inventoryTable.setItems(inventory);
    }

    @FXML
    private void handleAddMenuItem() {

        String name = itemNameField.getText();
        String priceText = itemPriceField.getText();
        String type = itemTypeBox.getValue();

        if (name == null || name.isEmpty() || type == null) {
            statusLabel.setText("Fill all fields.");
            return;
        }

        double price;

        try {
            price = Double.parseDouble(priceText);
        } catch (Exception e) {
            statusLabel.setText("Invalid price.");
            return;
        }

        MenuItem item = MenuItemFactory.createMenuItem(type, name, price, new HashMap<>(), null, new ArrayList<>());
        cafeSystem.addMenuItem(item);
        itemNameField.clear();
        itemPriceField.clear();

        statusLabel.setText("Item added.");
    }

    @FXML
    private void handleRemoveMenuItem() {

        MenuItem selected = menuListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Select item to remove.");
            return;
        }

        cafeSystem.removeMenuItem(selected);

        statusLabel.setText("Item removed.");
    }

    @Override
    public void updateMenu(java.util.List<MenuItem> menu) {
        menuListView.setItems(
                javafx.collections.FXCollections.observableArrayList(menu)
        );
    }
}