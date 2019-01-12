package ui.controllers.tiles.completeOrders;

import db.LoginManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Status;
import model.entities.OrdersEntity;
import model.entities.OrdersProductsEntity;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ui.views.TileView;

import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("Duplicates")
public class ViewController {
    private static Stage stage;
    private Session session;
    public ScrollPane scrollPane;
    public TableView<OrdersTable> tableBrowseOrders;
    public TableColumn<OrdersTable, String>  TransactionDocumentColumn;
    public TableColumn<OrdersTable, Integer> idColumn;
    public TableColumn<OrdersTable, String> nameColumn;
    public TableColumn<OrdersTable, String> addressColumn;
    public TableColumn<OrdersTable, String> phoneColumn;
    public TableColumn<OrdersTable, String>  deliveryColumn;
    public TableColumn<OrdersTable, String>  statusColumn;
    public TableColumn<OrdersTable, String>  paymentColumn;
    public TableColumn<OrdersTable, Void> productsColumn;
    public TableColumn<OrdersTable, Void> confirmColumn;
    private final ObservableList<OrdersTable> data = FXCollections.observableArrayList();

    public void initialize() {
        createTableView();
        session = LoginManager.getSession();
        runQuery();
    }

    private void createTableView() {
        tableBrowseOrders.setItems(data);
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("orderId"));
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(
                new PropertyValueFactory<>("customerAdress"));
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phoneNumber"));
        deliveryColumn.setCellValueFactory(
                new PropertyValueFactory<>("deliveryName"));
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("currentStatus"));
        paymentColumn.setCellValueFactory(
                new PropertyValueFactory<>("payment"));
        TransactionDocumentColumn.setCellValueFactory(
                new PropertyValueFactory<>("transactionDocument"));
        productsColumn.setCellFactory(createButtonCellFactory(true));
        confirmColumn.setCellFactory(createButtonCellFactory(false));
    }

    private Callback<TableColumn<OrdersTable, Void>, TableCell<OrdersTable, Void>> createButtonCellFactory(boolean isItProducts) {
        return new Callback<TableColumn<OrdersTable, Void>, TableCell<OrdersTable, Void>>() {
            @Override
            public TableCell<OrdersTable, Void> call(TableColumn<OrdersTable, Void> param) {
                return new TableCell<OrdersTable, Void>() {
                    private Button button = new Button((isItProducts ? "Show" : "Confirm"));
                    {
                        setAlignment(Pos.CENTER);
                        if (isItProducts)
                            button.setOnAction((e) -> showProducts(data.get(getIndex())));
                        else
                            button.setOnAction(e -> doOtherStuff(data.get(getIndex())));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        };
    }

    private void doOtherStuff(OrdersTable ordersTable) {
        session.beginTransaction();
        Query query = session.
                createSQLQuery("CALL music_store.update_order_status((:orderId), (:newStatus), (:userId))")
                .setParameter("orderId", ordersTable.getOrderId())
                .setParameter("newStatus", 1)
                .setParameter("userId", LoginManager.getUsername());
        query.executeUpdate();
        session.getTransaction().commit();
        data.removeAll(ordersTable);
    }

    private void showProducts(OrdersTable ordersTable) {
        @SuppressWarnings("ConstantConditions") final Parent parent;
        try {
            parent = FXMLLoader
                    .load(this.getClass().getClassLoader().getResource("fxml/tiles/CompleteOrdersProducts.fxml"));
            stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Products");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Unable to load manage users view.");
            alert.showAndWait();
        }

        TypedQuery<OrdersProductsEntity> ordersProductsEntityTypedQuery =
                session.createQuery("from OrdersProductsEntity where orderId=(:id)", OrdersProductsEntity.class).setParameter("id", ordersTable.getOrderId());
        List<OrdersProductsEntity> ordersProductsEntityList = ordersProductsEntityTypedQuery.getResultList();
        for (OrdersProductsEntity ordersProductsEntity : ordersProductsEntityList) {
            try {
                System.out.println(ordersProductsEntity.getOrderId());
                try {
                    ProductsViewController.insertData(
                            new OrdersProductsTable(
                                    ordersProductsEntity.getProductId(),
                                    ordersProductsEntity.getAlbumViewEntity().getName(),
                                    ordersProductsEntity.getAlbumViewEntity().getName(),
                                    ordersProductsEntity.getQuantity()));
                } catch (ObjectNotFoundException e){//
                }
                try {
                    ProductsViewController.insertData(
                            new OrdersProductsTable(
                                    ordersProductsEntity.getProductId(),
                                    ordersProductsEntity.getInstrumentViewEntity().getManufacturerName(),
                                    ordersProductsEntity.getInstrumentViewEntity().getInstrumentName(),
                                    ordersProductsEntity.getQuantity()
                            )
                    );
                } catch (ObjectNotFoundException e){//
                }
                try {
                    ProductsViewController.insertData(
                            new OrdersProductsTable(
                                    ordersProductsEntity.getProductId(),
                                    ordersProductsEntity.getOtherViewEntity().getProducer(),
                                    ordersProductsEntity.getOtherViewEntity().getName(),
                                    ordersProductsEntity.getQuantity()
                            )
                    );
                } catch (ObjectNotFoundException e){//
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void closeDialogWindow() {
        stage.close();
    }

    private void runQuery() {
        TypedQuery<OrdersEntity> completeOrdersQuery = session.
                createQuery("from OrdersEntity where currentStatus=(:par)", OrdersEntity.class).setParameter("par", Status.in_progress);
        List<OrdersEntity> ordersEntityList = completeOrdersQuery.getResultList();
        for (OrdersEntity ordersEntity : ordersEntityList) {
            data.add(new OrdersTable(ordersEntity));
        }
    }
    @FXML
    public void goBack() {
        session.close();
        TileView.initialize(LoginManager.getAccessLevel());
    }
    @SuppressWarnings("unused")
    public static class OrdersTable {
        private final SimpleIntegerProperty orderId;
        private final SimpleStringProperty deliveryName;
        private final SimpleStringProperty customerName;
        private final SimpleStringProperty customerAdress;
        private final SimpleStringProperty phoneNumber;
        private final SimpleStringProperty currentStatus;
        private final SimpleStringProperty payment;
        private final SimpleStringProperty transactionDocument;
        private OrdersTable(OrdersEntity ordersEntity) {
            this.orderId = new SimpleIntegerProperty(ordersEntity.getOrderId());
            this.deliveryName = new SimpleStringProperty(ordersEntity.getDeliveryEntity().getName());
            this.customerName = new SimpleStringProperty(ordersEntity.getDeliveryEntity().getName());
            this.customerAdress = new SimpleStringProperty(ordersEntity.getCustomerName());
            this.phoneNumber = new SimpleStringProperty(ordersEntity.getPhoneNumber());
            this.currentStatus = new SimpleStringProperty(ordersEntity.getCurrentStatus().toString());
            this.payment = new SimpleStringProperty(ordersEntity.getPayment().toString());
            this.transactionDocument = new SimpleStringProperty(ordersEntity.getTransactionDocument().toString());
        }
        public int getOrderId() {
            return orderId.get();
        }
        public void setOrderId(int value) {
            orderId.set(value);
        }
        public String getDeliveryName() {
            return deliveryName.get();
        }
        public void setDeliveryName(String value) {
            deliveryName.set(value);
        }
        public String getCustomerName() {
            return customerName.get();
        }
        public void setCustomerName(String value) {
            customerName.set(value);
        }
        public String getCustomerAdress() {
            return customerAdress.get();
        }
        public void setCustomerAdress(String value) {
            customerAdress.set(value);
        }
        public String getPhoneNumber() {
            return phoneNumber.get();
        }
        public void setPhoneNumber(String value) {
            phoneNumber.set(value);
        }
        public String getCurrentStatus() {
            return currentStatus.get();
        }
        public void setCurrentStatus(String value) {
            currentStatus.set(value);
        }
        public String getPayment() {
            return payment.get();
        }
        public void setPayment(String value) {
            payment.set(value);
        }
        public String getTransactionDocument() {
            return transactionDocument.get();
        }
        public void setTransactionDocument(String value) {
            transactionDocument.set(value);
        }
    }
}