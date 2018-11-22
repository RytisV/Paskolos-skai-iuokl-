package sample;

public class Data{

    boolean linear;

    double loan;
    int term;
    double percentage;
    String payType;

    public void  showData(){
        System.out.println("Loan:" + loan + "\nTerm: " + term + "\nPercentage: " + percentage + "\nPaytype: " +payType);
    }
}
