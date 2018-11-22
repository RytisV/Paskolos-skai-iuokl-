package sample;

import java.awt.*;
import java.io.*;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.Finance;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Calculations extends Data {

    public void calculateLinearCredit() {

        double[] linearCredit = new double[super.term];
        double[] linearInterest = new double[super.term];
        double[] linearPayment = new double[super.term];
        double[] linearRemainder = new double[super.term];
        linearRemainder[0] = super.loan;


        double firstMonthInterest = (super.loan * super.percentage / 100) / 12;
        linearInterest[0] = firstMonthInterest;
        linearInterest[0] = Math.round(linearInterest[0] * 100.00) / 100.00;

        double Credit = super.loan / super.term;
        for (int i = 0; i < linearCredit.length; i++) {
            linearCredit[i] = Credit;
            linearCredit[i] = Math.round(linearCredit[i] * 100.00) / 100.00;
        }

        for (int i = 1; i < linearInterest.length; i++) {
            linearRemainder[i] = linearRemainder[i - 1] - Credit;
            linearRemainder[i] = Math.round(linearRemainder[i] * 100.00) / 100.00;

            linearInterest[i] = (linearRemainder[i] * super.percentage / 100) / 12;
            linearInterest[i] = Math.round(linearInterest[i] * 100.00) / 100.00;
        }


        for (int i = 0; i < linearPayment.length; i++) {
            linearPayment[i] = linearInterest[i] + linearCredit[i];
            linearPayment[i] = Math.round(linearPayment[i] * 100.00) / 100.00;
        }

        printToFile(linearRemainder, linearPayment, linearCredit, linearInterest, super.term);
        graph("Linijinis", linearCredit, super.term, linearInterest, linearPayment);
        printGUI();
    }

    //--------------------------------------------------------------------

    public void calculateAnnuityCredit() {
        {
            double[] annuityCredit = new double[super.term];
            double[] annuityInterest = new double[super.term];
            double[] annuityPayment = new double[super.term];
            double[] annuityRemainder = new double[super.term];

            annuityRemainder[0] = super.loan;


            double payment = annuityPay(super.percentage / 100 / 12, super.term, super.loan);
            payment = Math.round(payment * 100.00) / 100.00;

            for (int i = 0; i < annuityPayment.length; i++)
                annuityPayment[i] = payment;


            double firstCredit = Finance.ppmt(super.percentage / 100 / 12, 1, super.term, super.loan);
            annuityCredit[0] = firstCredit * (-1);
            annuityCredit[0] = Math.round(annuityCredit[0] * 100.00) / 100.00;

            for (int i = 1; i < annuityCredit.length; i++) {
                double credit = Finance.ppmt(super.percentage / 100 / 12, i + 1, super.term, super.loan);
                annuityCredit[i] = credit * (-1);

                annuityRemainder[i] = annuityRemainder[i - 1] - annuityCredit[i - 1];
                annuityRemainder[i] = Math.round(annuityRemainder[i] * 100.00) / 100.00;
                annuityCredit[i] = Math.round(annuityCredit[i] * 100.00) / 100.00;
            }


            double firstInterest = Finance.ipmt(super.percentage / 100 / 12, 1, super.term, super.loan);
            annuityInterest[0] = firstInterest * (-1);
            annuityInterest[0] = Math.round(annuityInterest[0] * 100.00) / 100.00;

            for (int i = 1; i < annuityInterest.length; i++) {
                double Interest = Finance.ipmt(super.percentage / 100 / 12, i + 1, super.term, super.loan);
                annuityInterest[i] = Interest * (-1);
                annuityInterest[i] = Math.round(annuityInterest[i] * 100.00) / 100.00;
            }

            printToFile(annuityRemainder, annuityPayment, annuityCredit, annuityInterest, super.term);
            graph("Anuiteto", annuityCredit, super.term, annuityInterest, annuityPayment);
            printGUI();

        }
    }


    public double annuityPay(double i, int n, double l) {
        return (((i * (Math.pow(1 + i, n))) / (Math.pow(1 + i, n) - 1)) * l);
    }

    public void printToFile(double[] Remainder, double[] Payment, double[] Credit, double[] Interest, int months) {

        try (PrintWriter out = new PrintWriter("Ataskaita.txt")) {
            out.println("Menesis        Paskolos likutis        Kreditas        Palukanos         Bendra men. imoka");
            for (int i = 0; i < months; i++) {
                out.printf("%-15d%-24.2f%-16.2f%-18.2f%-15.2f", i + 1, Remainder[i], Credit[i], Interest[i], Payment[i]);
                out.printf("%n");
            }

        } catch (Exception e) {
            System.out.println("Error!!");
        }

    }


    private void graph(String type, double creditArray[], int term, double interestArray[], double paymentArray[]) {

        Scene scene;

        StackPane layout = new StackPane();
        scene = new Scene(layout, 600, 600);

        BorderPane chartPlace = new BorderPane();


        NumberAxis axisX = new NumberAxis();
        NumberAxis axisY = new NumberAxis();
        LineChart<Number, Number> Chart;
        Chart = new LineChart<Number, Number>(axisX, axisY);

        axisX.setLabel("Mėnesiai");
        axisY.setLabel("Suma");

        Chart.setTitle(type + " grafikas");

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();

        series1.setName("Kreditas");
        series2.setName("Palūkanos");
        series3.setName("Bendra mėn. įmoka");

        for (int i = 0; i < term; i++) {
            series1.getData().add(new XYChart.Data(i + 1, creditArray[i]));
            series2.getData().add(new XYChart.Data(i + 1, interestArray[i]));
            series3.getData().add(new XYChart.Data(i + 1, paymentArray[i]));
        }

        Chart.getData().add(series1);
        Chart.getData().add(series2);
        Chart.getData().add(series3);

        chartPlace.setTop(Chart);


        layout.getChildren().add(chartPlace);
        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle(type + " grafikas");
        window.show();
    }


    private void printGUI() {

        File txtFile = new File("Ataskaita.txt");
        if (txtFile.exists())
        {
            if (Desktop.isDesktopSupported())
            {
                try
                {
                    Desktop.getDesktop().open(txtFile);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Awt Desktop is not supported!");
            }
        }

        else
        {
            System.out.println("File does not exist!");
        }

    }

}


