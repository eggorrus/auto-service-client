package ui;

import service.*;
import entity.*;
import enums.Breakdowns;
import enums.Specailization;
import enums.Status;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.control.cell.CheckBoxTableCell;

public class MainController {

    // ЗАМЕНИТЬ DAO на Service
    private final ClientService clientService = new ClientService();
    private final MasterService masterService = new MasterService();
    private final PriceService priceService = new PriceService();
    private final WorkOrderService workOrderService = new WorkOrderService();

    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private final ObservableList<Master> masters = FXCollections.observableArrayList();
    private final ObservableList<Price> prices = FXCollections.observableArrayList();
    private final ObservableList<WorkOrder> orders = FXCollections.observableArrayList();

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Long> clientIdCol;
    @FXML private TableColumn<Client, String> clientFirstNameCol;
    @FXML private TableColumn<Client, String> clientLastNameCol;
    @FXML private TableColumn<Client, String> clientCarCol;
    @FXML private TableColumn<Client, String> clientPhoneCol;
    @FXML private TableColumn<Client, String> clientInfoCol;

    @FXML private TableView<Master> masterTable;
    @FXML private TableColumn<Master, Long> masterIdCol;
    @FXML private TableColumn<Master, String> masterFirstNameCol;
    @FXML private TableColumn<Master, String> masterLastNameCol;
    @FXML private TableColumn<Master, Specailization> masterSpecCol;
    @FXML private TableColumn<Master, Boolean> masterActiveCol;

    @FXML private TableView<WorkOrder> orderTable;
    @FXML private TableColumn<WorkOrder, Long> orderIdCol;
    @FXML private TableColumn<WorkOrder, String> orderClientCol;
    @FXML private TableColumn<WorkOrder, String> orderMasterCol;
    @FXML private TableColumn<WorkOrder, String> orderServiceCol;
    @FXML private TableColumn<WorkOrder, String> orderBreakdownCol;
    @FXML private TableColumn<WorkOrder, String> orderStatusCol;
    @FXML private TableColumn<WorkOrder, Float> orderPriceCol;

    @FXML private TableView<Price> priceTable;
    @FXML private TableColumn<Price, Long> priceIdCol;
    @FXML private TableColumn<Price, String> priceNameCol;
    @FXML private TableColumn<Price, Float> priceValueCol;

    @FXML
    private void initialize() {
        setupClientTable();
        setupMasterTable();
        setupPriceTable();
        setupWorkOrderTable();

        loadAllData();
    }

    private void setupClientTable() {
        clientTable.setEditable(true);

        clientIdCol.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        clientFirstNameCol.setCellValueFactory(c -> c.getValue().firstNameProperty());
        clientLastNameCol.setCellValueFactory(c -> c.getValue().lastNameProperty());
        clientCarCol.setCellValueFactory(c -> c.getValue().carProperty());
        clientPhoneCol.setCellValueFactory(c -> c.getValue().phoneNumberProperty());
        clientInfoCol.setCellValueFactory(c -> c.getValue().infoProperty());

        makeEditableClient(clientFirstNameCol);
        makeEditableClient(clientLastNameCol);
        makeEditableClient(clientCarCol);
        makeEditableClient(clientPhoneCol);
        makeEditableClient(clientInfoCol);

        clientTable.setItems(clients);
    }

    private void setupMasterTable() {
        masterTable.setEditable(true);

        masterIdCol.setCellValueFactory(m -> m.getValue().idProperty().asObject());
        masterFirstNameCol.setCellValueFactory(m -> m.getValue().firstNameProperty());
        masterLastNameCol.setCellValueFactory(m -> m.getValue().lastNameProperty());
        masterSpecCol.setCellValueFactory(m -> m.getValue().specailizationProperty());
        masterActiveCol.setCellValueFactory(m -> m.getValue().activeProperty().asObject());

        makeEditableMasterText(masterFirstNameCol);
        makeEditableMasterText(masterLastNameCol);
        makeEditableMasterSpec();
        makeEditableMasterActive();

        masterTable.setItems(masters);
    }

    private void setupPriceTable() {
        priceTable.setEditable(true);

        priceIdCol.setCellValueFactory(cellData -> {
            Price price = cellData.getValue();
            long id = price.getId();
            return new SimpleObjectProperty<>(id);
        });

        priceNameCol.setCellValueFactory(cellData -> cellData.getValue().nameOfServiceProperty());

        priceValueCol.setCellValueFactory(cellData -> {
            Price price = cellData.getValue();
            return new SimpleObjectProperty<>(price.getPrice());
        });

        makeEditablePriceName();
        makeEditablePriceValue();

        priceTable.setItems(prices);
    }

    private void setupWorkOrderTable() {
        orderIdCol.setCellValueFactory(o -> o.getValue().idProperty().asObject());

        orderClientCol.setCellValueFactory(o -> {
            WorkOrder order = o.getValue();
            Client client = order.getClient();
            if (client != null) {
                return new SimpleStringProperty(
                        client.getFirstName() + " " + client.getLastName() + "\n" + client.getCar()
                );
            }
            return new SimpleStringProperty("");
        });

        orderMasterCol.setCellValueFactory(o -> {
            WorkOrder order = o.getValue();
            Master master = order.getMaster();
            if (master != null) {
                return new SimpleStringProperty(
                        master.getFirstName() + " " + master.getLastName() + "\n" + master.getSpecailization()
                );
            }
            return new SimpleStringProperty("");
        });

        orderServiceCol.setCellValueFactory(o -> {
            WorkOrder order = o.getValue();
            Price price = order.getPrice();
            if (price != null) {
                return new SimpleStringProperty(price.getNameOfService());
            }
            return new SimpleStringProperty("");
        });

        orderBreakdownCol.setCellValueFactory(o -> {
            WorkOrder order = o.getValue();
            Breakdowns breakdown = order.getBreakdowns();
            if (breakdown != null) {
                return new SimpleStringProperty(breakdown.toString());
            }
            return new SimpleStringProperty("");
        });

        orderStatusCol.setCellValueFactory(o -> {
            WorkOrder order = o.getValue();
            Status status = order.getCurrentStatus();
            if (status != null) {
                return new SimpleStringProperty(status.toString());
            }
            return new SimpleStringProperty("");
        });

        orderPriceCol.setCellValueFactory(o -> o.getValue().finalPriceProperty().asObject());

        orderTable.setItems(orders);
    }

    private void loadAllData() {
        try {
            clients.setAll(clientService.findAll());
            masters.setAll(masterService.findAll());
            prices.setAll(priceService.findAll());
            orders.setAll(workOrderService.findAll());
        } catch (Exception e) {
            showErrorAlert("Ошибка загрузки данных: " + e.getMessage());
        }
    }

    private void makeEditableClient(TableColumn<Client, String> col) {
        col.setCellFactory(TextFieldTableCell.forTableColumn());
        col.setOnEditCommit(e -> {
            Client c = e.getRowValue();
            String v = e.getNewValue();

            if (col == clientFirstNameCol) c.setFirstName(v);
            else if (col == clientLastNameCol) c.setLastName(v);
            else if (col == clientCarCol) c.setCar(v);
            else if (col == clientPhoneCol) c.setPhoneNumber(v);
            else if (col == clientInfoCol) c.setInfo(v);

            try {
                clientService.update(c);
                clientTable.refresh();
            } catch (Exception ex) {
                showErrorAlert("Ошибка обновления: " + ex.getMessage());
            }
        });
    }

    private void makeEditableMasterText(TableColumn<Master, String> col) {
        col.setCellFactory(TextFieldTableCell.forTableColumn());
        col.setOnEditCommit(e -> {
            Master m = e.getRowValue();
            String v = e.getNewValue();

            if (col == masterFirstNameCol) m.setFirstName(v);
            else if (col == masterLastNameCol) m.setLastName(v);

            try {
                masterService.update(m);
                masterTable.refresh();
            } catch (Exception ex) {
                showErrorAlert("Ошибка обновления: " + ex.getMessage());
            }
        });
    }

    private void makeEditableMasterSpec() {
        masterSpecCol.setCellFactory(
                ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Specailization.values()))
        );
        masterSpecCol.setOnEditCommit(e -> {
            Master m = e.getRowValue();
            m.setSpecailization(e.getNewValue());
            try {
                masterService.update(m);
                masterTable.refresh();
            } catch (Exception ex) {
                showErrorAlert("Ошибка обновления: " + ex.getMessage());
            }
        });
    }

    private void makeEditableMasterActive() {
        masterActiveCol.setCellFactory(CheckBoxTableCell.forTableColumn(masterActiveCol));
        masterActiveCol.setOnEditCommit(e -> {
            Master m = e.getRowValue();
            m.setActive(e.getNewValue());
            try {
                masterService.update(m);
                masterTable.refresh();
            } catch (Exception ex) {
                showErrorAlert("Ошибка обновления: " + ex.getMessage());
            }
        });
    }

    private void makeEditablePriceName() {
        priceNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        priceNameCol.setOnEditCommit(e -> {
            Price p = e.getRowValue();
            p.setNameOfService(e.getNewValue());
            try {
                priceService.update(p);
                priceTable.refresh();
            } catch (Exception ex) {
                showErrorAlert("Ошибка обновления: " + ex.getMessage());
            }
        });
    }

    private void makeEditablePriceValue() {
        priceValueCol.setCellFactory(
                TextFieldTableCell.forTableColumn(new javafx.util.converter.FloatStringConverter())
        );
        priceValueCol.setOnEditCommit(e -> {
            Price p = e.getRowValue();
            p.setPrice(e.getNewValue());
            try {
                priceService.update(p);
                priceTable.refresh();
            } catch (Exception ex) {
                showErrorAlert("Ошибка обновления: " + ex.getMessage());
            }
        });
    }

    @FXML
    private void addClient() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Новый клиент");

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField carField = new TextField();
        TextField phoneField = new TextField();
        TextField infoField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Имя:"), firstNameField);
        grid.addRow(1, new Label("Фамилия:"), lastNameField);
        grid.addRow(2, new Label("Машина:"), carField);
        grid.addRow(3, new Label("Телефон:"), phoneField);
        grid.addRow(4, new Label("Инфо:"), infoField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Client client = new Client();
                client.setFirstName(firstNameField.getText());
                client.setLastName(lastNameField.getText());
                client.setCar(carField.getText());
                client.setPhoneNumber(phoneField.getText());
                client.setInfo(infoField.getText());
                return client;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(client -> {
            try {
                clientService.create(client);
                loadAllData();
            } catch (Exception e) {
                showErrorAlert("Ошибка создания: " + e.getMessage());
            }
        });
    }

    @FXML
    private void deleteClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление клиента");
            alert.setHeaderText("Вы уверены, что хотите удалить клиента?");
            alert.setContentText(selected.getFirstName() + " " + selected.getLastName());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    clientService.delete(selected.getId());
                    loadAllData();
                } catch (Exception e) {
                    showErrorAlert("Ошибка удаления: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void addMaster() {
        Dialog<Master> dialog = new Dialog<>();
        dialog.setTitle("Новый мастер");

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        ComboBox<Specailization> specBox = new ComboBox<>();
        specBox.setItems(FXCollections.observableArrayList(Specailization.values()));
        specBox.getSelectionModel().selectFirst();
        CheckBox activeBox = new CheckBox("Активен");
        activeBox.setSelected(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Имя:"), firstNameField);
        grid.addRow(1, new Label("Фамилия:"), lastNameField);
        grid.addRow(2, new Label("Специализация:"), specBox);
        grid.addRow(3, activeBox);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Master master = new Master();
                master.setFirstName(firstNameField.getText());
                master.setLastName(lastNameField.getText());
                master.setSpecailization(specBox.getValue());
                master.setActive(activeBox.isSelected());
                return master;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(master -> {
            try {
                masterService.create(master);
                loadAllData();
            } catch (Exception e) {
                showErrorAlert("Ошибка создания: " + e.getMessage());
            }
        });
    }

    @FXML
    private void deleteMaster() {
        Master selected = masterTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление мастера");
            alert.setHeaderText("Вы уверены, что хотите удалить мастера?");
            alert.setContentText(selected.getFirstName() + " " + selected.getLastName());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    masterService.delete(selected.getId());
                    loadAllData();
                } catch (Exception e) {
                    showErrorAlert("Ошибка удаления: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void addPrice() {
        Dialog<Price> dialog = new Dialog<>();
        dialog.setTitle("Новая услуга");

        TextField nameField = new TextField();
        TextField priceField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Услуга:"), nameField);
        grid.addRow(1, new Label("Цена:"), priceField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    Price price = new Price();
                    price.setNameOfService(nameField.getText());
                    price.setPrice(Float.parseFloat(priceField.getText()));
                    return price;
                } catch (NumberFormatException e) {
                    showErrorAlert("Неверный формат цены");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(price -> {
            try {
                priceService.create(price);
                loadAllData();
            } catch (Exception e) {
                showErrorAlert("Ошибка создания: " + e.getMessage());
            }
        });
    }

    @FXML
    private void deletePrice() {
        Price selected = priceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление услуги");
            alert.setHeaderText("Вы уверены, что хотите удалить услугу?");
            alert.setContentText(selected.getNameOfService());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    priceService.delete(selected.getId());
                    loadAllData();
                } catch (Exception e) {
                    showErrorAlert("Ошибка удаления: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void addWorkOrder() {
        Dialog<WorkOrder> dialog = new Dialog<>();
        dialog.setTitle("Новый рабочий наряд");

        ComboBox<Client> clientCombo = new ComboBox<>();
        clientCombo.setItems(clients);
        clientCombo.setPromptText("Выберите клиента");
        clientCombo.setCellFactory(lv -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? "" :
                        client.getFirstName() + " " + client.getLastName() + " (" + client.getCar() + ")");
            }
        });
        clientCombo.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? "" :
                        client.getFirstName() + " " + client.getLastName() + " (" + client.getCar() + ")");
            }
        });

        ComboBox<Master> masterCombo = new ComboBox<>();
        masterCombo.setItems(masters);
        masterCombo.setPromptText("Выберите мастера");
        masterCombo.setCellFactory(lv -> new ListCell<Master>() {
            @Override
            protected void updateItem(Master master, boolean empty) {
                super.updateItem(master, empty);
                setText(empty || master == null ? "" :
                        master.getFirstName() + " " + master.getLastName() +
                                " (" + master.getSpecailization() + ")");
            }
        });
        masterCombo.setButtonCell(new ListCell<Master>() {
            @Override
            protected void updateItem(Master master, boolean empty) {
                super.updateItem(master, empty);
                setText(empty || master == null ? "" :
                        master.getFirstName() + " " + master.getLastName() +
                                " (" + master.getSpecailization() + ")");
            }
        });

        ComboBox<Price> priceCombo = new ComboBox<>();
        priceCombo.setItems(prices);
        priceCombo.setPromptText("Выберите услугу");
        priceCombo.setCellFactory(lv -> new ListCell<Price>() {
            @Override
            protected void updateItem(Price price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? "" :
                        price.getNameOfService() + " - " + price.getPrice() + " руб.");
            }
        });
        priceCombo.setButtonCell(new ListCell<Price>() {
            @Override
            protected void updateItem(Price price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? "" :
                        price.getNameOfService() + " - " + price.getPrice() + " руб.");
            }
        });

        ComboBox<Breakdowns> breakdownCombo = new ComboBox<>();
        breakdownCombo.setItems(FXCollections.observableArrayList(Breakdowns.values()));
        breakdownCombo.setPromptText("Выберите поломку");

        ComboBox<Status> statusCombo = new ComboBox<>();
        statusCombo.setItems(FXCollections.observableArrayList(Status.values()));
        statusCombo.setValue(Status.ACCEPTED);

        TextField priceField = new TextField();
        priceField.setPromptText("Итоговая цена");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Клиент:"), clientCombo);
        grid.addRow(1, new Label("Мастер:"), masterCombo);
        grid.addRow(2, new Label("Услуга:"), priceCombo);
        grid.addRow(3, new Label("Поломка:"), breakdownCombo);
        grid.addRow(4, new Label("Статус:"), statusCombo);
        grid.addRow(5, new Label("Итоговая цена:"), priceField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        priceCombo.setOnAction(e -> {
            Price selectedPrice = priceCombo.getValue();
            if (selectedPrice != null) {
                priceField.setText(String.valueOf(selectedPrice.getPrice()));
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    WorkOrder order = new WorkOrder();

                    if (clientCombo.getValue() != null) {
                        order.setClient(clientCombo.getValue());
                    } else {
                        showErrorAlert("Выберите клиента");
                        return null;
                    }

                    if (masterCombo.getValue() != null) {
                        order.setMaster(masterCombo.getValue());
                    } else {
                        showErrorAlert("Выберите мастера");
                        return null;
                    }

                    if (priceCombo.getValue() != null) {
                        order.setPrice(priceCombo.getValue());
                    } else {
                        showErrorAlert("Выберите услугу");
                        return null;
                    }

                    if (breakdownCombo.getValue() != null) {
                        order.setBreakdowns(breakdownCombo.getValue());
                    } else {
                        showErrorAlert("Выберите поломку");
                        return null;
                    }

                    order.setCurrentStatus(statusCombo.getValue());

                    if (!priceField.getText().isEmpty()) {
                        order.setFinalPrice(Float.parseFloat(priceField.getText()));
                    } else {
                        showErrorAlert("Введите итоговую цену");
                        return null;
                    }

                    return order;
                } catch (NumberFormatException e) {
                    showErrorAlert("Неверный формат цены");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(order -> {
            try {
                workOrderService.create(order);
                loadAllData();
            } catch (Exception e) {
                showErrorAlert("Ошибка создания: " + e.getMessage());
            }
        });
    }

    @FXML
    private void deleteWorkOrder() {
        WorkOrder selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление заказ-наряда");
            alert.setHeaderText("Вы уверены, что хотите удалить заказ-наряд?");
            alert.setContentText("ID: " + selected.getId());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    workOrderService.delete(selected.getId());
                    loadAllData();
                } catch (Exception e) {
                    showErrorAlert("Ошибка удаления: " + e.getMessage());
                }
            }
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void refreshData() {
        loadAllData();
    }
}