package my.vaadin;

import javax.servlet.annotation.WebServlet;

import backend.CustomerService;
import backend.Customer;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private CustomerService customerService = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>(Customer.class);


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        //Add the grid to the components
        layout.addComponents(grid);

        // fetch list of Customers from service and assign it to Grid
        List<Customer> customerList = customerService.findAll();
        grid.setItems(customerList);

        setContent(layout);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
