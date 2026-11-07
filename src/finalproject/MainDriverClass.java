package finalproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainDriverClass {

    public static void main(String[] args) {
        String[][] carColorsList = {
        {"Red", "Blue", "White", "Black", "Grey", "Silver", "Orange", "Green", "Brown"},
        {"Navy Blue", "Wine Red", "Silver", "Grey"},
        {"Yellow","Bright Red","Cyan","Lime Green","Bright Blue","Purple","Pink"},
        {"White","Black","Grey"}
        };
        
        ArrayList<User> userList = loadUsers();
        ArrayList<Cars> carsList = new ArrayList<>();
        
        userList.add(new User("testacc","test123","Customer","09136540679"));
        userList.add(new User("admin","admin123","Admin","09136540679"));
        
        carsList.add(new NewCars("Toyota", "Ativ 1.5 G CVT", 2026, "Sedan", 1069000, "Petrol", 5, "Automatic (CVT)", carColorsList[0], 50000, 3, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\n001.jpg","Dealership",25));
        carsList.add(new NewCars("BYD", "Sealion 6 DM-i", 2025, "SUV", 1598000, "Hybrid", 5, "Automatic (E-CVT)", carColorsList[1], 24300, 5, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\n002.jpg","Dealership",25));
        carsList.add(new NewCars("Hyundai", "Venue 1.6 GL", 2026, "SUV", 910000, "Petrol", 5, "Automatic (6-speed)", carColorsList[0], 15000, 7, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\n003.jpg","Dealership",5));
        carsList.add(new NewCars("Mitsubishi", "Destinator GT", 2026, "SUV", 1954000, "Petrol", 7, "Automatic (CVT)", carColorsList[0], 30000, 4, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\n004.jpg","Dealership",1));
        carsList.add(new NewCars("Ford", "Territory Hybrid Platinum", 2025, "SUV", 1339000, "Hybrid", 5, "Automatic (E-CVT)", carColorsList[1], 125000, 0, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\n005.jpg","Dealership",10));
        carsList.add(new NewCars("Nissan", "NV350 Urvan 2.5 Premium AT", 2026, "Van", 2165000, "Diesel", 15, "Automatic (5-speed)", carColorsList[3], 5000, 6, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\n006.jpg","Dealership",30));
        carsList.add(new NewCars("Lamborghini", "Revuelto", 2025, "Sports Car", 80000000, "Hybrid", 2, "Automatic (8-speed)", carColorsList[2], 1350000, 2, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\n007.jpg","Dealership",2));
        carsList.add(new UsedCars("Chevrolet", "Optra 1.8 LT", 2005, "Sedan", 265000, "Petrol", 5, "Manual (5-speed)", "Beige", 2, 4.5, "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\u001.jpg","John Doe",1));
        
        WelcomeFrame welcome = new WelcomeFrame(userList, carsList);
        welcome.setVisible(true);
    }
    
    public static ArrayList<User> loadUsers() {
        ArrayList<User> userList = new ArrayList<>();
        File file = new File("users.txt");

        if (!file.exists()) {
            return userList;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);
                if (data.length >= 4) {
                    String username = data[0];
                    String password = data[1];
                    String role = data[2];
                    String phoneNumber = data[3];

                    userList.add(new User(username, password, role, phoneNumber));
                }
            }
            System.out.println("Loaded " + userList.size() + " user(s) from " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }
}
