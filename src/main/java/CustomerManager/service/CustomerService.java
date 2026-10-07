package CustomerManager.service;

import CustomerManager.model.CustomerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerService {
    private final ObservableList<CustomerModel> customers =
            FXCollections.observableArrayList();

    /** The list that the table is connected to. */
    public ObservableList<CustomerModel> getCustomers() {
        return customers;
    }

    public void addCustomer(String name, String province) {
        customers.add(new CustomerModel(name, province));
    }

    public void deleteCustomer(CustomerModel customer) {
        customers.remove(customer);
    }
}
