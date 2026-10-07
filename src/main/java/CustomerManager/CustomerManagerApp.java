package CustomerManager;
import CustomerManager.controller.CustomerController;
import CustomerManager.service.CustomerService;
import CustomerManager.view.CustomerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application{
    @Override
    public void start(Stage stage) {
        CustomerService service = new CustomerService();
        CustomerView view = new CustomerView(service.getCustomers());
        new CustomerController(view, service, stage::close);

        stage.setTitle("Customer Manager");
        stage.setScene(new Scene(view.getRoot(), 580, 620));
        stage.setMinWidth(480);
        stage.setMinHeight(520);
        stage.show();

        Platform.runLater(view::focusName);          // cursor starts in the name field
    }

    public static void main(String[] args) {
        launch(args);
    }
}
