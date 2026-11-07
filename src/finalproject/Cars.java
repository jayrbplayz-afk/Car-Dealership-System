package finalproject;

import java.util.Random;

public abstract class Cars {

    private String vehId;
    private String make;
    private String model;
    private int yearModel;
    private String vehCategory;
    private double basePrice;
    private String imageLink;
    private String fuelType;
    private int noOfSeats;
    private String transmissionType;
    private String seller;
    private boolean soldOut;
    protected double downPayment;
    protected int stockQty;
    protected double totalPrice;

    static Random rand = new Random();

    public Cars(String make, String model, int yearModel, String vehCategory, double basePrice, String fuelType, int noOfSeats, String transmissionType, String imageLink, String seller, int stockQty) {
        this.make = make;
        this.model = model;
        this.yearModel = yearModel;
        this.vehCategory = vehCategory;
        this.basePrice = basePrice;
        this.fuelType = fuelType;
        this.noOfSeats = noOfSeats;
        this.transmissionType = transmissionType;
        this.imageLink = imageLink;
        this.seller = seller;
        this.stockQty = stockQty;
        this.soldOut = (stockQty <= 0);
    }

    public String getVehId() {
        return vehId;
    }

    public void setVehId(String vehId) {
        this.vehId = vehId;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYearModel() {
        return yearModel;
    }

    public String getVehCategory() {
        return vehCategory;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getSeats() {
        return noOfSeats;
    }

    public String getTransmission() {
        return transmissionType;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageLink() {
        return imageLink;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
        this.soldOut = (stockQty <= 0);
    }

    public boolean isSoldOut() {
        return soldOut || stockQty <= 0;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public String getSeller() {
        return seller;
    }
    
    public void decreaseStock() {
        if (stockQty > 0) {
            stockQty--;
            if (stockQty <= 0) {
                soldOut = true;
            }
        }
    }

    @Override
    public String toString() {
        return "[" + vehId + "] " + yearModel + " " + make + " " + model;
    }

    public abstract double getTotalPrice();
}

class UsedCars extends Cars {

    private static int usedCarCounter = 1;
    private String usedCarColor;
    private int prevOwnerCount;
    private double conditionScale;

    public UsedCars(String make, String model, int yearModel, String vehCategory, double basePrice, String fuelType, int noOfSeats, String transmissionType, String usedCarColor, int prevOwnerCount, double conditionScale, String imageLink, String seller, int stockQty) {

        super(make, model, yearModel, vehCategory, basePrice, fuelType, noOfSeats, transmissionType, imageLink, seller, stockQty);
        super.setVehId(String.format("U-%03d", usedCarCounter++));
        this.prevOwnerCount = prevOwnerCount;
        this.conditionScale = conditionScale;
        this.usedCarColor = usedCarColor;
        super.setTotalPrice(calcFinalPrice());
    }

    @Override
    public double getTotalPrice() {
        super.totalPrice = totalPrice;
        super.setTotalPrice(calcFinalPrice());
        return totalPrice;
    }
    
    public double getDownPayment(){
        downPayment = calcFinalPrice() * 0.15;
        return downPayment;
    }

    public double calcFinalPrice() {
        double finalPrice = super.getBasePrice();
        double decreasePercentage = 0.70 - (0.14 * conditionScale);
        finalPrice = finalPrice - (finalPrice * decreasePercentage);
        return finalPrice;
    }

    public int getPrevOwnerCount() {
        return prevOwnerCount;
    }

    public double getConditionScale() {
        return conditionScale;
    }

    public String getUsedCarColor() {
        return usedCarColor;
    }
}

class NewCars extends Cars {

    private static int newCarCounter = 1;
    private String[] carColorPicker;
    private String newCarColor;
    private double dealerMarkup;
    private int warranty;

    public NewCars(String make, String model, int yearModel, String vehCategory, double basePrice, String fuelType, int noOfSeats, String transmissionType, String[] carColorPicker, double dealerMarkup, int warranty, String imageLink, String seller, int stockQty) {

        super(make, model, yearModel, vehCategory, basePrice, fuelType, noOfSeats, transmissionType, imageLink, seller, stockQty);
        super.setVehId(String.format("N-%03d", newCarCounter++));
        this.dealerMarkup = dealerMarkup;
        this.warranty = warranty;
        this.carColorPicker = carColorPicker;
        super.setTotalPrice(calcFinalPrice());
    }

    @Override
    public double getTotalPrice() {
        super.totalPrice = totalPrice;
        super.setTotalPrice(calcFinalPrice());
        return totalPrice;
    }
    
    public double getDownPayment(){
        downPayment = calcFinalPrice() * 0.20;
        return downPayment;
    }

    public double calcFinalPrice() {
        return super.getBasePrice() + dealerMarkup;
    }

    public double getDealerMarkup() {
        return dealerMarkup;
    }

    public int getWarranty() {
        return warranty;
    }

    public String[] getCarColorPicker() {
        return carColorPicker;
    }
    
    public String getNewCarColor(){
        return carColorPicker[0];
    }
    
    public void setNewCarColor(String newCarColor){
        this.newCarColor = newCarColor;
    }
}
