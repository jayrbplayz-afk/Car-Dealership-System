package finalproject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TransactionReceipt {

    private String transactionId;
    private String customerId;
    private Cars purchasedCar;
    private String paymentType;
    private String accountName;
    private double paidAmount;
    private String timestamp;

    public TransactionReceipt(String customerId, Cars purchasedCar, String accountName, String paymentType, double paidAmount) {
        this.transactionId = "TXN-" + (100000 + new Random().nextInt(900000));
        this.customerId = customerId;
        this.purchasedCar = purchasedCar;
        this.accountName = accountName;
        this.paymentType = paymentType;
        this.paidAmount = paidAmount;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getTransactionId() {
        return transactionId; 
    }
    
    public String getCustomerId() {
        return customerId; 
    }
    
    public Cars getPurchasedCar() { 
        return purchasedCar; 
    }
    
    public String getAccountName() {
        return accountName; 
    }
    
    public String getPaymentMethod() {
        return paymentType; 
    }
    
    public double getPaidAmount() {
        return paidAmount; 
    }
    
    public String getTimestamp() { 
        return timestamp; 
    }
}