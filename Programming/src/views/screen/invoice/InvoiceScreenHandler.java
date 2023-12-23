package views.screen.invoice;

import common.exception.ProcessInvoiceException;
import controller.PaymentController;
import entity.invoice.Invoice;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import subsystem.VnPaySubsystem;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.payment.PaymentScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Logger;

public class InvoiceScreenHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(InvoiceScreenHandler.class.getName());
    @FXML
    private GridPane fastShippingList;

    @FXML
    private Label shipmentDetail;
    @FXML
    private Label deliveryInstruction;
    @FXML
    private Label deliveryTime;
    @FXML
    private Label pageTitle;
    @FXML
    private ImageView aimsImage;
    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label province;
    @FXML
    private Label shippingMethod;
    @FXML
    private Label address;

    @FXML
    private Label instructions;

    @FXML
    private Label subtotal;

    @FXML
    private Label shippingFees;

    @FXML
    private Label total;

    @FXML
    private VBox vboxItems;

    private Invoice invoice;

    public InvoiceScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });
        this.invoice = invoice;
        setInvoiceInfo();
        fastShippingList.setVisible(false);
        if(shippingMethod.getText().contains("Fast Delivery")) {
            fastShippingList.setVisible(true);
        }
    }

    private void setInvoiceInfo() {
        HashMap<String, String> deliveryInfo = invoice.getOrder().getDeliveryInfo();
        name.setText(deliveryInfo.get("name"));
        province.setText(deliveryInfo.get("province"));
        instructions.setText(deliveryInfo.get("instructions"));
        address.setText(deliveryInfo.get("address"));
        shippingMethod.setText(deliveryInfo.get("shippingMethod"));
        shipmentDetail.setText(deliveryInfo.get("shipmentDetail"));
        deliveryInstruction.setText(deliveryInfo.get("deliveryInstruction"));
        deliveryTime.setText(deliveryInfo.get("deliveryTime"));

        subtotal.setText(Utils.getCurrencyFormat(invoice.getOrder().getAmount()));
        shippingFees.setText(Utils.getCurrencyFormat(invoice.getOrder().getShippingFees()));
        int amount = invoice.getOrder().getAmount() + invoice.getOrder().getShippingFees();
        total.setText(Utils.getCurrencyFormat(amount));
        invoice.setAmount(amount);
        invoice.getOrder().getlstOrderMedia().forEach(orderMedia -> {
            try {
                MediaInvoiceScreenHandler mis = new MediaInvoiceScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                vboxItems.getChildren().add(mis.getContent());
            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }

        });


    }


    /**
     * @param event
     * @throws IOException
     */
    @FXML
    void confirmInvoice(MouseEvent event) throws IOException {
        var ctrl = new VnPaySubsystem();
        var url = ctrl.generatePayUrl(invoice.getAmount());
        BaseScreenHandler paymentScreen = new PaymentScreenHandler(this.stage, Configs.PAYMENT_SCREEN_PATH, invoice, url);
        paymentScreen.setBController(new PaymentController());
        paymentScreen.setPreviousScreen(this);
        paymentScreen.setHomeScreenHandler(homeScreenHandler);
        paymentScreen.setScreenTitle("Payment Screen");
        paymentScreen.show();
        LOGGER.info("Confirmed invoice");
    }

}
