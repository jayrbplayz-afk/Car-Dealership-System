package finalproject;

import java.util.ArrayList;
import java.util.Comparator;

public class CarSearchSorter {

    // ----------------------------------- SORTING -----------------------------------
    public static void quickSort(ArrayList<Cars> list, int low, int high, Comparator<Cars> comparator) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);
            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private static int partition(ArrayList<Cars> list, int low, int high, Comparator<Cars> comparator) {
        Cars pivot = list.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Cars temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Cars temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    // ---------------------------------- SEARCHING ----------------------------------
    public static ArrayList<Cars> linearSearch(ArrayList<Cars> sortedList, String query) {
        ArrayList<Cars> result = new ArrayList<>();

        if (query == null || query.trim().isEmpty()) {
            return result;
        }

        String lowerQuery = query.toLowerCase().trim();

        for (Cars car : sortedList) {
            String name = (car.getMake() + " " + car.getModel()).toLowerCase();
            String yearStr = String.valueOf(car.getYearModel());

            if (name.contains(lowerQuery) || yearStr.contains(lowerQuery)) {
                result.add(car);
            }
        }

        return result;
    }

}
