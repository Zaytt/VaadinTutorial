package my.vaadin;

import javax.servlet.annotation.WebServlet;

import backend.CustomerService;
import backend.Customer;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

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
    private TextField filterText = new TextField();

    private CustomerForm form = new CustomerForm(this);



    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        // Configure grid and textfield
        grid.setColumns("firstName", "lastName", "email");
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                form.setVisible(false);
            } else {
                form.setCustomer(event.getValue());
            }
        });
        filterText.setPlaceholder("Filter by name...");
        filterText.addValueChangeListener(e -> updateList());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        // Add clear button
        Button clearFilterButton = new Button(VaadinIcons.CLOSE);
        clearFilterButton.setDescription("Clear the current filter");
        clearFilterButton.addClickListener(e -> filterText.clear());

        // Add add customer button
        Button addCustomerButton = new Button("Add new customer");
        addCustomerButton.addClickListener(event -> {
            grid.asSingleSelect().clear();
            form.setCustomer(new Customer());
        });


        // Create a CSS Layout
        CssLayout filteringLayout = new CssLayout();
        filteringLayout.addComponents(filterText, clearFilterButton);
        filteringLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);


        // Make a toolbar
        HorizontalLayout toolbar = new HorizontalLayout(filteringLayout, addCustomerButton);


        // Add the form and grid to the layout
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        layout.addComponents(toolbar, main);

        // fetch list of Customers from service and assign it to Grid
        this.updateList();

        form.setVisible(false);

        setContent(layout);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    public void updateList(){
        List<Customer> customerList = customerService.findAll(filterText.getValue());
        grid.setItems(customerList);
    }
}
