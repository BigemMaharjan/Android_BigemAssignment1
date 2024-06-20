package com.example.bigemassignment1.models;

import java.text.DecimalFormat;

public class BeverageCost {

    private String customerName;

    private String phoneNumber;

    private String email;

    private String bevType;

    double  milkPrice;

    double sugarPrice;

    private String flavorChosen;

    double flavorPrice;

    private String bevSize;

    double sizePrice;

    private String region;

    private String store;

    private String salesDate;


    // Format the total cost to two decimal places with a dollar sign
    DecimalFormat decimalFormat = new DecimalFormat("$0.00");


//    Creating Constructor
    public BeverageCost(String customerName, String phoneNumber, String email, String bevType, double milkPrice, double sugarPrice, String flavorChosen, double flavorPrice, String bevSize, double sizePrice, String region, String store, String salesDate){
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bevType = bevType;
        this.milkPrice = milkPrice;
        this.sugarPrice = sugarPrice;
        this.flavorChosen = flavorChosen;
        this.flavorPrice = flavorPrice;
        this.bevSize = bevSize;
        this.sizePrice = sizePrice;
        this.region = region;
        this.store = store;
        this.salesDate = salesDate;
    }

    public String getBeverageCost(){
        double additionalCost = milkPrice + sugarPrice;
        double totalTax = 0;
        double totalCost = 0;
        double totalBillAmount = 0;
       totalCost = additionalCost + flavorPrice + sizePrice;
        totalTax = (additionalCost + flavorPrice + sizePrice) * 0.13;
        totalBillAmount = totalCost + totalTax;
        return " Name: " + customerName + "\n" +
                " Phone Number: " + phoneNumber + "\n" +
                " Email: " + email + "\n" +
                " Beverage Type: " + bevType + "\n" +
                " Additional Price: " + decimalFormat.format( additionalCost) + "\n" +
                " Flavor: " + flavorChosen + "\n" +
                " Flavor Price: " + "$" + flavorPrice + "\n" +
                " Beverage Size: " + bevSize + "\n" +
                " Size Price: " + "$" + sizePrice + "\n" +
                " Region: " + region + "\n" +
                " Store: " + store + "\n" +
                " Sales Date: " + salesDate + "\n" +
                " Total Cost: " + decimalFormat.format(totalCost) + "\n" +
                " Tax Added: "  + decimalFormat.format(totalTax) + "\n" +
                " Total Bill Amount: "  + decimalFormat.format(totalBillAmount);
    }

}




