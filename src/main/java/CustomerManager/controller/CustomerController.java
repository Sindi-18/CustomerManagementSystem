package CustomerManager.controller;

import CustomerManager.model.CustomerModel;
import CustomerManager.service.CustomerService;
import CustomerManager.view.CustomerView;

public class CustomerController {
    private final CustomerView view;
    private final CustomerService service;
    private final Runnable closeApp;

    public CustomerController(CustomerView view, CustomerService service, Runnable closeApp) {
        this.view = view;
        this.service = service;
        this.closeApp = closeApp;
        connectButtons();
    }

    /** Connect each button to its method. */
    private void connectButtons() {
        view.getSaveButton().setOnAction(event -> onSave());
        view.getClearButton().setOnAction(event -> onClear());
        view.getDeleteButton().setOnAction(event -> onDelete());
        view.getCloseMenuItem().setOnAction(event -> closeApp.run());
    }

    // =====================================================================
    // SAVE
    // =====================================================================
    private void onSave() {
        // Step 1: validate the name.
        String name = view.getEnteredName();
        if (name.isEmpty()) {
            view.showProblem("Enter the customer name.");
            view.focusName();
            return;                                  // stop; the user's input is kept
        }

        // Step 2: validate the province.
        String province = view.getChosenProvince();
        if (province == null) {
            view.showProblem("Choose a province.");
            view.focusProvince();
            return;                                  // the typed name stays in the field
        }

        // Step 3: everything is valid, so ask the service to save.
        try {
            service.addCustomer(name, province);
        } catch (Exception ex) {
            ex.printStackTrace();                    // technical details go to the log
            view.showSaveFailedAlert();              // friendly message for the user
            return;                                  // keep the form for another attempt
        }

        // Step 4: success. Tell the user, THEN clear the form.
        view.showSuccess("Customer saved: " + name + " (" + province + ")");
        view.clearForm();
    }

    // =====================================================================
    // CLEAR
    // =====================================================================
    private void onClear() {
        view.clearForm();
        view.showInfo("Form cleared.");
    }

    // =====================================================================
    // DELETE
    // =====================================================================
    private void onDelete() {
        // Step 1: a customer must be selected (the button is also disabled otherwise).
        CustomerModel selected = view.getSelectedCustomer();
        if (selected == null) {
            view.showProblem("Select a customer first.");
            return;
        }

        // Step 2: ask the user to confirm.
        if (view.confirmDelete(selected)) {
            service.deleteCustomer(selected);
            view.showSuccess("Customer deleted: " + selected.getName());
        } else {
            view.showInfo("Deletion cancelled. Nothing was removed.");
        }
    }
}
