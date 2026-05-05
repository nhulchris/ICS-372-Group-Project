package com.brewbite.controller;

import com.brewbite.facade.CafeSystem;
import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.*;
import com.brewbite.service.MenuObserver;
import com.brewbite.util.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the customer ordering screen.
 *
 * Implements MenuObserver so the menu list refreshes automatically
 * if the manager adds/removes items elsewhere in the application.
 */
public class CustomerOrderController implements MenuObserver {

    @FXML private Label welcomeLabel;
    @FXML private ListView<MenuItem> menuListView;
    @FXML private ListView<OrderItem> cartListView;
    @FXML private ComboBox<Size> sizeBox;
    @FXML private TextField quantityField;
    @FXML private VBox customizationsBox;
    @FXML private Label totalLabel;
    @FXML private Label statusLabel;

    private final CafeSystem cafeSystem = CafeSystem.getInstance();
    private final ObservableList<OrderItem> cartItems = FXCollections.observableArrayList();
    private Order currentOrder;

    @FXML
    public void initialize() {
        String customerName = cafeSystem.getCurrentCustomerName();
        if (customerName == null || customerName.isBlank()) {
            customerName = "Customer";
        }
        welcomeLabel.setText("Welcome, " + customerName);

        currentOrder = cafeSystem.createOrder(customerName);

        cartListView.setItems(cartItems);

        sizeBox.setItems(FXCollections.observableArrayList(Size.values()));
        sizeBox.getSelectionModel().select(Size.SMALL);

        cafeSystem.getMenuManager().addObserver(this);
        updateMenu(cafeSystem.getMenuItems());

        // When the user clicks a menu item, refresh the customizations panel.
        menuListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, sel) -> refreshCustomizationsPanel(sel));

        updateTotal();
        statusLabel.setText("");
    }

    /**
     * Rebuilds the customization checkbox panel based on which menu item
     * is currently selected. Pastries get an empty panel and a disabled size box.
     */
    private void refreshCustomizationsPanel(MenuItem selected) {
        customizationsBox.getChildren().clear();
        sizeBox.setDisable(!(selected instanceof Beverage));

        if (!(selected instanceof Beverage beverage)) {
            return;
        }
        for (Customization c : beverage.getCustomizations()) {
            CheckBox cb = new CheckBox(c.toString());
            cb.setUserData(c);
            customizationsBox.getChildren().add(cb);
        }
    }

    @FXML
    private void handleAddToCart() {
        statusLabel.setStyle("-fx-text-fill: #B00020;");
        statusLabel.setText("");

        MenuItem selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select an item.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(quantityField.getText().trim());
            if (qty <= 0) {
                statusLabel.setText("Quantity must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid quantity.");
            return;
        }

        // For beverages, clone the menu prototype with the customer's chosen
        // size and customizations so each cart entry is independent.
        MenuItem itemToOrder = selected;

        if (selected instanceof Beverage menuBev) {
            Size chosenSize = sizeBox.getValue();
            List<Customization> chosen = new ArrayList<>();
            for (var node : customizationsBox.getChildren()) {
                if (node instanceof CheckBox cb && cb.isSelected()
                        && cb.getUserData() instanceof Customization c) {
                    chosen.add(c);
                }
            }

            itemToOrder = MenuItemFactory.createBeverage(
                    menuBev.getName(),
                    menuBev.getBasePrice(),
                    new HashMap<>(menuBev.getIngredients()),
                    chosenSize,
                    chosen
            );
        }

        OrderItem orderItem = new OrderItem(itemToOrder, qty);
        cartItems.add(orderItem);
        currentOrder.addItem(orderItem);

        quantityField.setText("1");
        updateTotal();
        statusLabel.setText("Added to cart.");
    }

    @FXML
    private void handleRemoveFromCart() {
        OrderItem selected = cartListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an item in the cart to remove.");
            return;
        }
        cartItems.remove(selected);
        currentOrder.getItems().remove(selected);
        updateTotal();
        statusLabel.setText("Item removed from cart.");
    }

    @FXML
    private void handleClearOrder() {
        cartItems.clear();
        currentOrder = cafeSystem.createOrder(currentOrder.getCustomerName());
        updateTotal();
        statusLabel.setText("Order cleared.");
    }

    @FXML
    private void handlePlaceOrder() {
        statusLabel.setStyle("-fx-text-fill: #B00020;");
        statusLabel.setText("");

        if (currentOrder.getItems().isEmpty()) {
            statusLabel.setText("Cart is empty. Add items before placing an order.");
            return;
        }

        // Verify ingredient availability also accounts for customizations.
        if (!checkInventoryIncludingCustomizations(currentOrder)) {
            statusLabel.setText("Not enough inventory to place this order.");
            return;
        }

        boolean success = cafeSystem.placeOrder(currentOrder);
        if (!success) {
            statusLabel.setText("Order could not be placed (check inventory).");
            return;
        }

        // Also deduct customization ingredients (CafeSystem handles base ingredients only).
        deductCustomizationIngredients(currentOrder);

        int placedOrderId = currentOrder.getOrderId();

        cartItems.clear();
        currentOrder = cafeSystem.createOrder(currentOrder.getCustomerName());
        updateTotal();
        statusLabel.setStyle("-fx-text-fill: #1B5E20;");
        statusLabel.setText("Order #" + placedOrderId + " placed successfully!");
    }

    /**
     * Verifies inventory has enough of every ingredient — base AND customizations.
     */
    private boolean checkInventoryIncludingCustomizations(Order order) {
        Map<String, Integer> needed = new HashMap<>();
        for (OrderItem oi : order.getItems()) {
            for (Map.Entry<String, Integer> e : oi.getItem().getIngredients().entrySet()) {
                needed.merge(e.getKey(), e.getValue() * oi.getQuantity(), Integer::sum);
            }
            if (oi.getItem() instanceof Beverage bev) {
                for (Customization c : bev.getCustomizations()) {
                    if (c.getIngredients() == null) continue;
                    for (Map.Entry<String, Integer> e : c.getIngredients().entrySet()) {
                        needed.merge(e.getKey(), e.getValue() * oi.getQuantity(), Integer::sum);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> e : needed.entrySet()) {
            Ingredient inv = cafeSystem.getInventoryManager().getInventory().getIngredient(e.getKey());
            if (inv == null || inv.getQuantity() < e.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deducts the ingredients used by any customizations.
     * (CafeSystem.placeOrder already deducted base item ingredients.)
     */
    private void deductCustomizationIngredients(Order order) {
        for (OrderItem oi : order.getItems()) {
            if (!(oi.getItem() instanceof Beverage bev)) continue;
            for (Customization c : bev.getCustomizations()) {
                if (c.getIngredients() == null) continue;
                for (Map.Entry<String, Integer> e : c.getIngredients().entrySet()) {
                    cafeSystem.getInventoryManager()
                            .getInventory()
                            .reduceIngredient(e.getKey(), e.getValue() * oi.getQuantity());
                }
            }
        }
    }

    private void updateTotal() {
        totalLabel.setText("Total: $" + String.format("%.2f", currentOrder.calculateTotal()));
    }

    @FXML
    private void handleBack() {
        cafeSystem.getMenuManager().removeObserver(this);
        SceneManager.switchScene("/com/brewbite/view/role-selection.fxml");
    }

    @Override
    public void updateMenu(List<MenuItem> menu) {
        menuListView.setItems(FXCollections.observableArrayList(menu));
    }
}
