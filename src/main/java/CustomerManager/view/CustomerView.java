package CustomerManager.view;
import CustomerManager.model.CustomerModel;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CustomerView {
    // ---- Controls (fields, because the Controller needs to reach them) ----
    private final TextField nameField = new TextField();
    private final ComboBox<String> provinceBox = new ComboBox<>();
    private final Button saveButton = new Button("Save customer");
    private final Button clearButton = new Button("Clear form");
    private final Button deleteButton = new Button("Delete selected");
    private final TableView<CustomerModel> table = new TableView<>();
    private final Label statusLabel = new Label("Ready. Fill in the form to add a customer.");
    private final Label countLabel = new Label();
    private final MenuItem closeMenuItem = new MenuItem("Close");

    private final BorderPane root = new BorderPane();

    public CustomerView(ObservableList<CustomerModel> customers) {
        buildTable(customers);                       // table first: other parts use it
        root.setTop(buildMenuBar());
        root.setCenter(buildMainContent());
        root.setBottom(buildStatusBar(customers));
        setUpButtons();
    }

    // =====================================================================
    // BUILDING THE SCREEN
    // =====================================================================

    private MenuBar buildMenuBar() {
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().add(closeMenuItem);
        return new MenuBar(fileMenu);
    }

    private VBox buildMainContent() {
        Label title = new Label("Customer Manager");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label howTo = new Label(
                "How to use:\n"
                        + "1. Type the customer name.\n"
                        + "2. Choose a province.\n"
                        + "3. Click \"Save customer\" (or press Enter).\n"
                        + "To remove a customer: click their row in the table, "
                        + "then click \"Delete selected\".");
        howTo.setWrapText(true);
        howTo.setStyle("-fx-text-fill: #444444;");

        VBox content = new VBox(12, title, howTo, buildForm(), buildButtonRow(), table);
        content.setPadding(new Insets(15));
        VBox.setVgrow(table, Priority.ALWAYS);       // table takes the spare height
        return content;
    }

    /** Input controls: a TextField for the name and a ComboBox for the province. */
    private GridPane buildForm() {
        Label nameLabel = new Label("Customer name");
        nameLabel.setLabelFor(nameField);            // links label and field (accessibility)
        nameField.setPromptText("e.g., Mary Banda");

        Label provinceLabel = new Label("Province");
        provinceLabel.setLabelFor(provinceBox);
        provinceBox.getItems().addAll(
                "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
                "Muchinga", "Northern", "North-Western", "Southern", "Western");
        provinceBox.setPromptText("Choose a province");
        provinceBox.setMaxWidth(Double.MAX_VALUE);
        showPromptWhenEmpty(provinceBox);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, nameLabel, nameField);
        form.addRow(1, provinceLabel, provinceBox);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(provinceBox, Priority.ALWAYS);
        return form;
    }

    private HBox buildButtonRow() {
        return new HBox(10, saveButton, clearButton, deleteButton);
    }

    /** Data control: each Customer object becomes one row with two columns. */
    private void buildTable(ObservableList<CustomerModel> customers) {
        TableColumn<CustomerModel, String> nameCol = new TableColumn<>("Customer name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));         // calls getName()

        TableColumn<CustomerModel, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province")); // calls getProvince()

        table.getColumns().add(nameCol);
        table.getColumns().add(provinceCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setItems(customers);                   // connect the table to the list
        table.setPlaceholder(new Label("No customers saved yet."));
    }

    private HBox buildStatusBar(ObservableList<CustomerModel> customers) {
        // The count text updates automatically when the list changes.
        countLabel.textProperty().bind(
                Bindings.concat("Customers saved: ", Bindings.size(customers)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);      // pushes the count to the right

        HBox bar = new HBox(statusLabel, spacer, countLabel);
        bar.setPadding(new Insets(8, 15, 8, 15));
        bar.setStyle("-fx-border-color: #cccccc transparent transparent transparent;");
        return bar;
    }

    /** Button rules that help the user: hints, Enter key, and Delete only when usable. */
    private void setUpButtons() {
        saveButton.setDefaultButton(true);           // Enter key = Save
        saveButton.setTooltip(new Tooltip("Add this customer to the table"));
        clearButton.setTooltip(new Tooltip("Empty the name and province fields"));
        deleteButton.setTooltip(new Tooltip("Remove the customer selected in the table"));

        // Delete is greyed out until a row is selected, so the user cannot misuse it.
        deleteButton.disableProperty().bind(
                table.getSelectionModel().selectedItemProperty().isNull());
    }

    /** After setValue(null) a ComboBox can look blank; this brings the prompt text back. */
    private void showPromptWhenEmpty(ComboBox<String> box) {
        box.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? box.getPromptText() : item);
            }
        });
    }

    // =====================================================================
    // WHAT THE CONTROLLER CAN ASK THE VIEW
    // =====================================================================

    public BorderPane getRoot()          { return root; }
    public Button getSaveButton()        { return saveButton; }
    public Button getClearButton()       { return clearButton; }
    public Button getDeleteButton()      { return deleteButton; }
    public MenuItem getCloseMenuItem()   { return closeMenuItem; }

    /** The typed name, with spaces at both ends removed. */
    public String getEnteredName()       { return nameField.getText().trim(); }

    /** The chosen province, or null if nothing is chosen. */
    public String getChosenProvince()    { return provinceBox.getValue(); }

    /** The customer selected in the table, or null if none. */
    public CustomerModel getSelectedCustomer() {
        return table.getSelectionModel().getSelectedItem();
    }

    public void focusName()     { nameField.requestFocus(); }
    public void focusProvince() { provinceBox.requestFocus(); }

    public void clearForm() {
        nameField.clear();
        provinceBox.setValue(null);
        nameField.requestFocus();
    }

    // ---- Feedback: the app "speaks" to the user ----

    public void showSuccess(String message) {
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText(message);
    }

    public void showProblem(String message) {
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setText(message);
    }

    public void showInfo(String message) {
        statusLabel.setStyle("-fx-text-fill: black;");
        statusLabel.setText(message);
    }

    public void showSaveFailedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save failed");
        alert.setHeaderText("Customer could not be saved");
        alert.setContentText("Check the details and try again.");
        alert.showAndWait();
    }

    /** Asks "Delete this customer?" and returns true only if the user clicks Delete. */
    public boolean confirmDelete(CustomerModel customer) {
        ButtonType delete = new ButtonType("Delete");
        Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + customer.getName() + " (" + customer.getProvince() + ")?",
                delete, ButtonType.CANCEL);
        ask.setHeaderText("Confirm deletion");
        // Cancel, or closing the dialog, counts as "no".
        return ask.showAndWait().orElse(ButtonType.CANCEL) == delete;
    }

}
