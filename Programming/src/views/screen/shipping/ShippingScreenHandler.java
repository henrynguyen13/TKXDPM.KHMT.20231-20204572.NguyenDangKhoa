package views.screen.shipping;

import common.exception.InvalidDeliveryInfoException;
import controller.PlaceOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.cart.CartScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {
    private static Logger LOGGER = Utils.getLogger(ShippingScreenHandler.class.getName());
    @FXML
    private RadioButton normalShipping;
    private String selectedShippingMethod;

    @FXML
    private TextField shipmentDetail;
    @FXML
    private TextField deliveryInstruction;
    @FXML
    private DatePicker deliveryTime;
    @FXML
    private RadioButton fastShipping;

    @FXML
    private GridPane shippingListView;

    private ToggleGroup shippingToggleGroup;
    @FXML
    private Label screenTitle;

    @FXML
    private TextField name;
    @FXML
    private ImageView aimsImage;
    @FXML
    private TextField phone;

    @FXML
    private TextField address;

    @FXML
    private TextField instructions;

    @FXML
    private ComboBox<String> province;

    @FXML
    private Label labelAmount;

    @FXML
    private Label labelSubtotal;

    @FXML
    private Label labelVAT;

    @FXML
    private Label labelShippingFee;

    private Order order;
    public ShippingScreenHandler(Stage stage, String screenPath, Order order ) throws IOException {
        super(stage, screenPath);

        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });
        this.order = order;


    }

    /**
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
        this.province.getItems().addAll(Configs.PROVINCES);
        // Initialize the toggle group for radio buttons
        shippingToggleGroup = new ToggleGroup();
        normalShipping.setToggleGroup(shippingToggleGroup);
        fastShipping.setToggleGroup(shippingToggleGroup);

        shippingToggleGroup.selectToggle(normalShipping);

        shippingToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleShippingMethodChange((RadioButton) newValue);
            }
        });
        selectedShippingMethod = normalShipping.getText();
        shippingListView.setVisible(false);
        province.valueProperty().addListener((observable, oldValue, newValue) -> updateShippingFeeAndAmount(newValue));
        address.textProperty().addListener((observable, oldValue, newValue) -> updateShippingFeeAndAmount(newValue));

    }

    private void handleShippingMethodChange(RadioButton selectedRadioButton) {
        LOGGER.info("Province" + province.getValue());


        if (selectedRadioButton == fastShipping && (province.getValue() == null || !province.getValue().contains("Hà Nội"))) {
            // Show error popup for fast shipping not applicable
            showErrorPopup("Địa điểm bạn chọn không áp dụng giao hàng nhanh");
            // Reset selection to NormalShipping
            shippingToggleGroup.selectToggle(normalShipping);
            shippingListView.setVisible(false);
        } else {
            shippingListView.setVisible(true);
            selectedShippingMethod = selectedRadioButton.getText();
        }
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
    /**
     * @param event
     * @throws IOException
     * @throws InterruptedException
     * @throws SQLException
     */
    @FXML
    void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

        // add info to messages
        HashMap messages = new HashMap<>();
        messages.put("name", name.getText());
        messages.put("phone", phone.getText());
        messages.put("address", address.getText());
        messages.put("instructions", instructions.getText());
        messages.put("province", province.getValue());
        messages.put("shippingMethod", selectedShippingMethod);
        messages.put("shipmentDetail", shipmentDetail.getText());
        messages.put("deliveryInstruction", deliveryInstruction.getText());
        messages.put("deliveryTime", deliveryTime.getValue() != null ?LocalDate.parse(deliveryTime.getValue().toString()).toString(): "");


        try {
            // process and validate delivery info
            getBController().processDeliveryInfo(messages);
        } catch (InvalidDeliveryInfoException e) {
            throw new InvalidDeliveryInfoException(e.getMessage());
        }

        // calculate shipping fees
        int shippingFees = getBController().calculateShippingFee(order.getAmount(), order.getQuantity() * 0.5, address.toString(), order);
        order.setShippingFees(shippingFees);
        order.setDeliveryInfo(messages);

        // // create invoice screen
        Invoice invoice = getBController().createInvoice(order);
        BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
        InvoiceScreenHandler.setPreviousScreen(this);
        InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
        InvoiceScreenHandler.setScreenTitle("Invoice Screen");
        InvoiceScreenHandler.setBController(getBController());
        InvoiceScreenHandler.show();

        //create delivery method screen
//        BaseScreenHandler DeliveryMethodsScreenHandler = new DeliveryMethodsScreenHandler(this.stage, Configs.DELIVERY_METHODS_PATH, this.order);
//        DeliveryMethodsScreenHandler.setPreviousScreen(this);
//        DeliveryMethodsScreenHandler.setHomeScreenHandler(homeScreenHandler);
//        DeliveryMethodsScreenHandler.setScreenTitle("Delivery method screen");
//        DeliveryMethodsScreenHandler.setBController(getBController());
//        DeliveryMethodsScreenHandler.show();
    }

    /**
     * @return PlaceOrderController
     */
    public PlaceOrderController getBController() {
        return (PlaceOrderController) super.getBController();
    }

    public void notifyError() {
        // TODO: implement later on if we need
    }


    public void updateCartAmount() {
        // calculate subtotal and amount
        int subtotal = getBController().getCartSubtotal();
        int vat = (int) ((Configs.PERCENT_VAT / 100) * subtotal);
        int amount = subtotal + vat;

        // update subtotal and amount of Cart
        labelSubtotal.setText(Utils.getCurrencyFormat(subtotal));
        labelVAT.setText(Utils.getCurrencyFormat(vat));
        labelAmount.setText(Utils.getCurrencyFormat(amount));
    }


    private void updateShippingFeeAndAmount(String province) {
        order.setProvince(province);
        // Calculate shipping fees
        int shippingFees = getBController().calculateShippingFee(order.getAmount(), order.getQuantity() * 0.5, address.toString(), order);
        order.setShippingFees(shippingFees);

        // Update shipping fee property
        labelShippingFee.setText(Utils.getCurrencyFormat(shippingFees));
        // Calculate subtotal and amount
        int subtotal = getBController().getCartSubtotal();
        int vat = (int) ((Configs.PERCENT_VAT / 100.0) * subtotal);
        int amount = subtotal + vat + shippingFees;

        // Update amount property
        labelAmount.setText(Utils.getCurrencyFormat(amount));
    }
}
