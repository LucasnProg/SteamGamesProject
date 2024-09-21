package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrdenationsByDate {
    public static CSVRecord[] selectionSortByDates(CSVRecord[] array){
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int index = 0; index < array.length-1; index++) {
            int minorIndex = index;
            for (int secondIndex = index + 1; secondIndex < array.length; secondIndex++) {
                LocalDate date1 = LocalDate.parse(array[secondIndex].get(2), formatDate);
                LocalDate date2 = LocalDate.parse(array[minorIndex].get(2), formatDate);
                if (date1.isBefore(date2)) {
                    minorIndex = secondIndex;
                }
            }
            CSVRecord temp = array[minorIndex];
            array[minorIndex] = array[index];
            array[index] = temp;
        }
        return array;
    }
    public static CSVRecord[] insertionSortByDates(CSVRecord[] array){
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int index = 1; index < array.length; index++) {
            CSVRecord minor = array[index];
            int secondIndex = index - 1;
            LocalDate minorValue = LocalDate.parse(minor.get(2), formatDate);

            while (secondIndex >= 0) {
                LocalDate date = LocalDate.parse(array[secondIndex].get(2), formatDate);

                if (date.isAfter(minorValue)) {
                    array[secondIndex + 1] = array[secondIndex];
                } else {
                    break;
                }
                secondIndex--;
            }

            array[secondIndex + 1] = minor;
        }
        return array;
    }
    public static CSVRecord[] mergeSortByDates(CSVRecord[] array) {
        if (array.length <= 1) {
            return array;
        }
        int mid = array.length / 2;
        CSVRecord[] left = new CSVRecord[mid];
        CSVRecord[] right = new CSVRecord[array.length - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        mergeSortByDates(left);
        mergeSortByDates(right);

        merge(array, left, right);

        return array;
    }

    private static void merge(CSVRecord[] array, CSVRecord[] left, CSVRecord[] right) {
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            LocalDate leftDate = LocalDate.parse(left[i].get(2), formatDate);
            LocalDate rightDate = LocalDate.parse(right[j].get(2), formatDate);

            if (leftDate.isBefore(rightDate) || leftDate.isEqual(rightDate)) {
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
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate pivot = LocalDate.parse(array[last].get(2), formatDate);
        int iterator = first - 1;

        for (int index = first; index < last; index++) {
            LocalDate currentDate = LocalDate.parse(array[index].get(2), formatDate);

            if (currentDate.isBefore(pivot) || currentDate.isEqual(pivot)) {
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

    public static CSVRecord[] quickSortByDates(CSVRecord[] array, int first, int last) {
        if (first < last) {
            int pivotPosition = partition(array, first, last);

            quickSortByDates(array, first, pivotPosition - 1);
            quickSortByDates(array, pivotPosition + 1, last);
        }
        return array;
    }

    public static int medianOf3(CSVRecord[] array, int first, int mid, int last) {
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate firstDate = LocalDate.parse(array[first].get(2), formatDate);
        LocalDate midDate = LocalDate.parse(array[mid].get(2), formatDate);
        LocalDate lastDate = LocalDate.parse(array[last].get(2), formatDate);

        if ((firstDate.isBefore(midDate) && midDate.isBefore(lastDate)) || (lastDate.isBefore(midDate) && midDate.isBefore(firstDate))) {
            return mid;
        } else if ((midDate.isBefore(firstDate) && firstDate.isBefore(lastDate)) || (lastDate.isBefore(firstDate) && firstDate.isBefore(midDate))) {
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

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate pivot = LocalDate.parse(array[last].get(2), formatDate);

        int iterator = first - 1;

        for (int index = first; index < last; index++) {
            LocalDate currentDate = LocalDate.parse(array[index].get(2), formatDate);

            if (currentDate.isBefore(pivot) || currentDate.isEqual(pivot)) {
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

    public static CSVRecord[] quickSortByDatesMedianOf3(CSVRecord[] array, int first, int last) {
        if (first < last) {
            int pivotPosition = partitionMedian3(array, first, last);
            quickSortByDatesMedianOf3(array, first, pivotPosition - 1);
            quickSortByDatesMedianOf3(array, pivotPosition + 1, last);
        }

        return array;
    }

    public static CSVRecord[] heapSortByDates(CSVRecord[] array) {
        int n = array.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            CSVRecord temp = array[i];
            array[i] = array[0];
            array[0] = temp;

            heapify(array, i, 0);
        }
        return array;
    }

    private static void heapify(CSVRecord[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (left < n && LocalDate.parse(array[left].get(2), formatDate).isAfter(LocalDate.parse(array[largest].get(2), formatDate))) {
            largest = left;
        }

        if (right < n && LocalDate.parse(array[right].get(2), formatDate).isAfter(LocalDate.parse(array[largest].get(2), formatDate))) {
            largest = right;
        }

        if (largest != i) {
            CSVRecord swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;

            heapify(array, n, largest);
        }
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
    public static void mainOrdenationsByDate() throws IOException {

        Path pathToGamesFormated = Paths.get("src\\main\\java\\database\\games_formated_release_data.csv");
        CSVRecord[] arrayToOrder = getArray(pathToGamesFormated);
        assert arrayToOrder != null;
        long startTime, endTime, duration;
        CSVRecord[] ordenArray;
        System.out.println("Ordenações por datas Medio caso:");

        // Selection Sort
        System.out.println("Selection sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = selectionSortByDates(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_selectionSort_medioCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = insertionSortByDates(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_insertionSort_medioCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = mergeSortByDates(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_mergeSort_medioCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByDates(arrayToOrder.clone(), 0, arrayToOrder.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_quickSort_medioCaso.csv", ordenArray);

        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByDatesMedianOf3(arrayToOrder.clone(), 0, arrayToOrder.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_quickSort_mediana_de_3_medioCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Médio Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = heapSortByDates(arrayToOrder.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_heapSort_medioCaso.csv", ordenArray);

        System.out.println("\nPS: Para analise de melhor caso, usaremos o array ja ordenado por qualquer um dos métodos anteriores.");
        Path pathToCsvOrden = Paths.get("src\\main\\java\\database\\games_release_date_mergeSort_medioCaso.csv");

        CSVRecord[] arrayBetterCase = getArray(pathToCsvOrden);
        assert arrayBetterCase != null;
        System.out.println("Ordenações por datas Melhor caso:");

        // Selection Sort
        System.out.println("Selection sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = selectionSortByDates(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_selection_melhorCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = insertionSortByDates(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_insertionSort_melhorCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = mergeSortByDates(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_mergeSort_melhorCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByDates(arrayBetterCase.clone(), 0, arrayBetterCase.length - 1);
        try {
            ordenArray = quickSortByDates(arrayBetterCase.clone(), 0, arrayBetterCase.length - 1);
        } catch (StackOverflowError e) {
            System.err.println("StackOverflowError capturado: " + e.getMessage());
        }
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_quickSort_melhorCaso.csv", ordenArray);

        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByDatesMedianOf3(arrayBetterCase.clone(), 0, arrayBetterCase.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_quickSort_mediana_de_3_melhorCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Melhor Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = heapSortByDates(arrayBetterCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_heapSort_melhorCaso.csv", ordenArray);

        System.out.println("\nPS: Para analise de Pior caso, usaremos o array em ordem descrescente.");
        CSVRecord[] arrayWorstCase = getArray(pathToCsvOrden);
        assert arrayWorstCase != null;
        reverseArray(arrayWorstCase);
        System.out.println("Ordenações por datas Pior caso:");

        // Selection Sort
        System.out.println("Selection sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = selectionSortByDates(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_selection_piorCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = insertionSortByDates(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_insertionSort_piorCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = mergeSortByDates(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_mergeSort_piorCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        try {
            ordenArray = quickSortByDates(arrayWorstCase.clone(), 0, arrayWorstCase.length - 1);
        } catch (StackOverflowError e) {
            System.err.println("StackOverflowError capturado: " + e.getMessage());
        }
        //ordenArray = quickSortByDates(arrayWorstCase.clone(), 0, arrayWorstCase.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_quickSort_piorCaso.csv", ordenArray);


        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = quickSortByDatesMedianOf3(arrayWorstCase.clone(), 0, arrayWorstCase.length - 1);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_quickSort_mediana_de_3_piorCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Pior Caso:");
        startTime = System.currentTimeMillis();
        ordenArray = heapSortByDates(arrayWorstCase.clone());
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        writeToCvs("games_release_date_heapSort_piorCaso.csv", ordenArray);

    }
}
