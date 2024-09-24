package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OrdenationsByAchievements {
    public static CSVRecord[] selectionSortByAchievements(CSVRecord[] array){
        for (int index = 0; index < array.length-1; index++) {
            int minorIndex = index;
            for (int secondIndex = index + 1; secondIndex < array.length; secondIndex++) {
                if (Integer.parseInt(array[minorIndex].get(26)) < Integer.parseInt(array[secondIndex].get(26))) {
                    minorIndex = secondIndex;
                }
            }
            CSVRecord temp = array[minorIndex];
            array[minorIndex] = array[index];
            array[index] = temp;
        }
        return array;
    }
    public static CSVRecord[] insertionSortByAchievements(CSVRecord[] array){
        for (int index = 1; index < array.length; index++) {
            CSVRecord minor = array[index];
            int secondIndex = index - 1;

            double minorValue = Integer.parseInt(minor.get(26));
            while (secondIndex >= 0 && Integer.parseInt(array[secondIndex].get(26)) < minorValue) {
                array[secondIndex + 1] = array[secondIndex];
                secondIndex--;
            }

            array[secondIndex + 1] = minor;
        }
        return array;
    }
    public static CSVRecord[] mergeSortByAchievements(CSVRecord[] array) {
        if (array.length <= 1) {
            return array;
        }

        int mid = array.length / 2;
        CSVRecord[] left = new CSVRecord[mid];
        CSVRecord[] right = new CSVRecord[array.length - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        mergeSortByAchievements(left);
        mergeSortByAchievements(right);

        merge(array, left, right);

        return array;
    }

    private static void merge(CSVRecord[] array, CSVRecord[] left, CSVRecord[] right) {
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            int leftPrice = Integer.parseInt(left[i].get(26));
            int rightPrice = Integer.parseInt(right[j].get(26));

            if (leftPrice >= rightPrice) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        while (i < left.length) {
            array[k++] = left[i++];
        }

        while (j < right.length) {
            array[k++] = right[j++];
        }
    }

    public static int partition(CSVRecord[] array, int first, int last) {
        int pivot = Integer.parseInt(array[last].get(26));
        int iterator = first - 1;

        for (int index = first; index < last; index++) {
            int currentPrice = Integer.parseInt(array[index].get(26));

            if (currentPrice >= pivot) {
                iterator++;
                CSVRecord temp = array[iterator];
                array[iterator] = array[index];
                array[index] = temp;
            }
        }

        CSVRecord temp = array[iterator + 1];
        array[iterator + 1] = array[last];
        array[last] = temp;

        return iterator + 1;
    }

    public static CSVRecord[] quickSortByAchievements(CSVRecord[] array, int first, int last) {
        while (first < last) {
            int pivotPosition = partition(array, first, last);

            if (pivotPosition - first < last - pivotPosition) {
                quickSortByAchievements(array, first, pivotPosition - 1);
                first = pivotPosition + 1;
            } else {
                quickSortByAchievements(array, pivotPosition + 1, last);
                last = pivotPosition - 1;
            }
        }
        return array;
    }

    public static int medianOf3(CSVRecord[] array, int first, int mid, int last) {
        double firstPrice = Double.parseDouble(array[first].get(26));
        double midPrice = Double.parseDouble(array[mid].get(26));
        double lastPrice = Double.parseDouble(array[last].get(26));

        if ((firstPrice < midPrice && midPrice < lastPrice) || (lastPrice < midPrice && midPrice < firstPrice)) {
            return mid;
        } else if ((midPrice < firstPrice && firstPrice < lastPrice) || (lastPrice < firstPrice && firstPrice < midPrice)) {
            return first;
        } else {
            return last;
        }
    }

    public static int partitionMedian3(CSVRecord[] array, int first, int last) {
        int mid = (first + last) / 2;
        int median = medianOf3(array, first, mid, last);

        CSVRecord temp = array[median];
        array[median] = array[last];
        array[last] = temp;

        double pivot = Double.parseDouble(array[last].get(26));
        int iterator = first - 1;

        for (int index = first; index < last; index++) {
            double currentAchievements = Double.parseDouble(array[index].get(26));

            if (currentAchievements >= pivot) {
                iterator++;
                CSVRecord swapTemp = array[iterator];
                array[iterator] = array[index];
                array[index] = swapTemp;
            }
        }

        CSVRecord swapTemp = array[iterator + 1];
        array[iterator + 1] = array[last];
        array[last] = swapTemp;

        return iterator + 1;
    }

    public static CSVRecord[] quickSortByAchievementsMedianOf3(CSVRecord[] array, int first, int last) {
        while (first < last) {
            int pivotPosition = partitionMedian3(array, first, last);

            if (pivotPosition - first < last - pivotPosition) {
                quickSortByAchievementsMedianOf3(array, first, pivotPosition - 1);
                first = pivotPosition + 1;
            } else {
                quickSortByAchievementsMedianOf3(array, pivotPosition + 1, last);
                last = pivotPosition - 1;
            }
        }
        return array;
    }

    public static CSVRecord[] heapSortByAchievements(CSVRecord[] array) {
        int totalRecords = array.length;

        for (int currentIndex = totalRecords / 2 - 1; currentIndex >= 0; currentIndex--) {
            heapify(array, totalRecords, currentIndex);
        }

        for (int currentIndex = totalRecords - 1; currentIndex > 0; currentIndex--) {
            CSVRecord temporaryRecord = array[0];
            array[0] = array[currentIndex];
            array[currentIndex] = temporaryRecord;

            heapify(array, currentIndex, 0);
        }
        return array;
    }

    private static void heapify(CSVRecord[] array, int heapSize, int rootIndex) {
        int smallestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;

        if (leftChildIndex < heapSize && Integer.parseInt(array[leftChildIndex].get(26)) < Integer.parseInt(array[smallestIndex].get(26))) {
            smallestIndex = leftChildIndex;
        }

        if (rightChildIndex < heapSize && Integer.parseInt(array[rightChildIndex].get(26)) < Integer.parseInt(array[smallestIndex].get(26))) {
            smallestIndex = rightChildIndex;
        }

        if (smallestIndex != rootIndex) {
            CSVRecord temporaryRecord = array[rootIndex];
            array[rootIndex] = array[smallestIndex];
            array[smallestIndex] = temporaryRecord;

            heapify(array, heapSize, smallestIndex);
        }
    }
    public static CSVRecord[] countSortByAchievements(CSVRecord[] array) {
        int len = array.length;
        int iterator;
        int maxValue = Integer.parseInt(array[0].get(26));

        for (iterator = 1; iterator < len; iterator++) {
            if (Integer.parseInt(array[iterator].get(26)) > maxValue) {
                maxValue = Integer.parseInt(array[iterator].get(26));
            }
        }

        int[] countArray = new int[maxValue + 1];
        for (iterator = 0; iterator < len; iterator++) {
            countArray[Integer.parseInt(array[iterator].get(26))]++;
        }

        for (iterator = maxValue - 1; iterator >= 0; iterator--) {
            countArray[iterator] += countArray[iterator + 1];
        }

        CSVRecord[] outputArray = new CSVRecord[len];
        for (iterator = len - 1; iterator >= 0; iterator--) {
            outputArray[countArray[Integer.parseInt(array[iterator].get(26))] - 1] = array[iterator];
            countArray[Integer.parseInt(array[iterator].get(26))]--;
        }

        return outputArray;
    }


    public static CSVRecord[] getArray(Path filePath) throws IOException {
        File file = filePath.toFile();

        try (
                Reader reader = new FileReader(file);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
        ) {
            List<CSVRecord> records = csvParser.getRecords();

            CSVRecord[] array = new CSVRecord[records.size()];
            for (int i = 0; i < records.size(); i++) {
                array[i] = records.get(i);
            }
            return array;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    public static void writeToCvs (String fileName,CSVRecord[] array) throws IOException {
        Path databasePath = Paths.get("src\\main\\java\\database");
        File outputFile = new File(databasePath.toString(), fileName);
        File fileToGetHeader = new File(databasePath.toString(),"portuguese_supported_games.csv");

        try (
                Writer writer = new FileWriter(outputFile);
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
                Reader reader = new FileReader(fileToGetHeader);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        ) {
            printer.printRecord(csvParser.getHeaderMap().keySet());
            for (int i = 0; i < array.length; i++) {
                printer.printRecord(array[i]);
            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void reverseArray(CSVRecord[] arrayWorstCase){
        int start = 0;
        int end = arrayWorstCase.length - 1;

        while (start < end) {
            CSVRecord swap = arrayWorstCase[start];
            arrayWorstCase[start] = arrayWorstCase[end];
            arrayWorstCase[end] = swap;

            start++;
            end--;
        }
    }
    public static void mainOrdenationsByAchievements() throws IOException {

        Path pathToGames = Paths.get("src\\main\\java\\database\\games.csv");
        CSVRecord[] arrayToOrder = getArray(pathToGames);
        assert arrayToOrder != null;
        long startTime, endTime, duration;
        CSVRecord[] ordenArray;
        System.out.println("Ordenações por achievements Medio caso:");

        // Selection Sort
        System.out.println("Selection sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = selectionSortByAchievements(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_selectionSort_medioCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = insertionSortByAchievements(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_insertionSort_medioCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = mergeSortByAchievements(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_mergeSort_medioCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByAchievements(arrayToOrder.clone(), 0, arrayToOrder.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_quickSort_medioCaso.csv", ordenArray);

        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByAchievementsMedianOf3(arrayToOrder.clone(), 0, arrayToOrder.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_quickSort_mediana_de_3_medioCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = heapSortByAchievements(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_heapSort_medioCaso.csv", ordenArray);

        // Counting Sort
        System.out.println("Counting sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = countSortByAchievements(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_countingSort_medioCaso.csv", ordenArray);

        System.out.println("\nPS: Para analise de melhor caso, usaremos o array ja ordenado por qualquer um dos métodos anteriores.");
        Path pathToCsvOrden = Paths.get("src\\main\\java\\database\\games_achievements_insertionSort_medioCaso.csv");

        CSVRecord[] arrayBetterCase = getArray(pathToCsvOrden);
        assert arrayBetterCase != null;
        System.out.println("Ordenações por achievements Melhor caso:");

        // Selection Sort
        System.out.println("Selection sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = selectionSortByAchievements(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_selectionSort_melhorCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = insertionSortByAchievements(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_insertionSort_melhorCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = mergeSortByAchievements(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_mergeSort_melhorCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Melhor Caso: ");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByAchievements(arrayBetterCase.clone(), 0, arrayBetterCase.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_quickSort_melhorCaso.csv", ordenArray);

        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByAchievementsMedianOf3(arrayBetterCase.clone(), 0, arrayBetterCase.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_quickSort_mediana_de_3_melhorCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = heapSortByAchievements(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_heapSort_melhorCaso.csv", ordenArray);

        // Counting Sort
        System.out.println("Counting sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = countSortByAchievements(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_countingSort_melhorCaso.csv", ordenArray);


        System.out.println("\nPS: Para analise de Pior caso, usaremos o array em ordem crescente.");
        CSVRecord[] arrayWorstCase = getArray(pathToCsvOrden);
        assert arrayWorstCase != null;
        reverseArray(arrayWorstCase);
        System.out.println("Ordenações por achievements Pior caso:");

        // Selection Sort
        System.out.println("Selection sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = selectionSortByAchievements(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_selectionSort_piorCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = insertionSortByAchievements(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_insertionSort_piorCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = mergeSortByAchievements(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_mergeSort_piorCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Pior Caso: ");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByAchievements(arrayWorstCase.clone(), 0, arrayWorstCase.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_quickSort_piorCaso.csv", ordenArray);


        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Pior Caso: ");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByAchievementsMedianOf3(arrayWorstCase.clone(), 0, arrayWorstCase.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_quickSort_mediana_de_3_piorCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = heapSortByAchievements(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_heapSort_piorCaso.csv", ordenArray);

        // Counting Sort
        System.out.println("Counting sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = countSortByAchievements(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_achievements_countingSort_piorCaso.csv", ordenArray);
    }
}
