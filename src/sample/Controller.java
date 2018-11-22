package sample;

        import javafx.fxml.FXML;
        import javafx.scene.chart.LineChart;
        import javafx.scene.chart.NumberAxis;
        import javafx.scene.chart.XYChart;
        import javafx.scene.control.*;
        import javafx.application.Application;
        import javafx.geometry.Insets;
        import javafx.scene.Group;
        import javafx.scene.Scene;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.layout.GridPane;
        import javafx.scene.layout.StackPane;
        import javafx.stage.Stage;

        import java.awt.event.ActionEvent;

public class Controller extends Calculations {

    int tempYears, tempMonths;
    int count=0;

    @FXML
    private TextField loanID, termIDM, termIDY, percentageID;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private ChoiceBox<String> payTypeID = new ChoiceBox<String>();

    public void calcButtonClicked()
    {
        count++;
        if(loanID.getText().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Neįvedėte sumos.", ButtonType.OK);
            error.showAndWait();
        }
        else this.loan = Double.parseDouble(loanID.getText());


        if(termIDY.getText().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Neįvedėte termino metais.", ButtonType.OK);
            error.showAndWait();
        }
        else  tempYears = Integer.parseInt(termIDY.getText());

        if(termIDM.getText().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Neįvedėte termino mėnesiais.", ButtonType.OK);
            error.showAndWait();
        }
        else tempMonths = Integer.parseInt(termIDM.getText());

        if(percentageID.getText().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Neįvedėte palūkanų procento.", ButtonType.OK);
            error.showAndWait();
        }
        else this.percentage = Double.parseDouble(percentageID.getText());

        this.term = tempYears*12+tempMonths;

        if(count == 1) initializeChoiceBox();
    }

    public void initializeChoiceBox()
    {
        payTypeID.getItems().add("Linijinis");
        payTypeID.getItems().add("Anuiteto");
    }

    public void continueButtonClicked()
    {
        int counter=0;

        if(payTypeID.getItems().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Nepasirinkote mokėjimo tipo.", ButtonType.OK);
            error.showAndWait();
        }

        else
        {
            this.payType = payTypeID.getValue();
            counter++;
        }

        if(counter == 1)
        {
            Alert startAlert = new Alert(Alert.AlertType.CONFIRMATION, "Ar pradėti skaičiavimą?", ButtonType.YES, ButtonType.CANCEL);
            startAlert.showAndWait();

            if (startAlert.getResult() == ButtonType.YES)
            {
                if(this.payType == "Linijinis")
                {
                    calculateLinearCredit();

                    Alert logAlert = new Alert(Alert.AlertType.INFORMATION, "Skaičiavimai baigti. Ataskaitą taip pat galite rasti tekstiniame faile.", ButtonType.OK);
                    logAlert.setTitle("Baigta");
                    logAlert.setHeaderText(null);
                    logAlert.showAndWait();
                }


                else
                {
                    calculateAnnuityCredit();
                    Alert logAlert = new Alert(Alert.AlertType.INFORMATION, "Skaičiavimai baigti. Ataskaitą taip pat galite rasti tekstiniame faile.", ButtonType.OK);
                    logAlert.setTitle("Baigta");
                    logAlert.setHeaderText(null);
                    logAlert.showAndWait();
                }
            }

        }

    }

}
