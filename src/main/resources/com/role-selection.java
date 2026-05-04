<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<AnchorPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.brewbite.controller.RoleSelectionController"
            prefWidth="400" prefHeight="300">

    <children>

        <VBox spacing="15"
              layoutX="100"
              layoutY="80">

            <Label text="Brew &amp; Bite" style="-fx-font-size: 20px;" />

            <Button text="Customer"
                    prefWidth="200"
                    onAction="#handleCustomer"/>

            <Button text="Barista"
                    prefWidth="200"
                    onAction="#handleBarista"/>

            <Button text="Manager"
                    prefWidth="200"
                    onAction="#handleManager"/>

        </VBox>

    </children>
</AnchorPane>