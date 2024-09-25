package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OrdenationsByPrice {
    public static CSVRecord[] selectionSortByPrices(CSVRecord[] array){
        for (int index = 0; index < array.length-1; index++) {
            int minorIndex = index;
            for (int secondIndex = index + 1; secondIndex < array.length; secondIndex++) {
                if (Double.parseDouble(array[minorIndex].get(6)) > Double.parseDouble(array[secondIndex].get(6))) {
                    minorIndex = secondIndex;
                }
            }
            CSVRecord temp = array[minorIndex];
            array[minorIndex] = array[index];
            array[index] = temp;
        }
        return array;
    }
    public static CSVRecord[] insertionSortByPrices(CSVRecord[] array){
        for (int index = 1; index < array.length; index++) {
            CSVRecord minor = array[index];
            int secondIndex = index - 1;

            double minorValue = Double.parseDouble(minor.get(6));
            while (secondIndex >= 0 && Double.parseDouble(array[secondIndex].get(6)) > minorValue) {
                array[secondIndex + 1] = array[secondIndex];
                secondIndex--;
            }

            array[secondIndex + 1] = minor;
        }
        return array;
    }
    public static CSVRecord[] mergeSortByPrices(CSVRecord[] array) {
        if (array.length <= 1) {
            return array;
        }

        int mid = array.length / 2;
        CSVRecord[] left = new CSVRecord[mid];
        CSVRecord[] right = new CSVRecord[array.length - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        mergeSortByPrices(left);
        mergeSortByPrices(right);

        merge(array, left, right);

        return array;
    }

    private static void merge(CSVRecord[] array, CSVRecord[] left, CSVRecord[] right) {
        int index = 0, secondIndex = 0, iterator = 0;

        while (index < left.length && secondIndex < right.length) {
            double leftPrice = Double.parseDouble(left[index].get(6));
            double rightPrice = Double.parseDouble(right[secondIndex].get(6));

            if (leftPrice <= rightPrice) {
                array[iterator++] = left[index++];
            } else {
                array[iterator++] = right[secondIndex++];
            }
        }

        while (index < left.length) {
            array[iterator++] = left[index++];
        }

        while (secondIndex < right.length) {
            array[iterator++] = right[secondIndex++];
        }
    }


    public static int partition(CSVRecord[] array, int first, int last) {
        double pivot = Double.parseDouble(array[last].get(6));
        int iterator = first - 1;

        for (int index = first; index < last; index++) {
            double currentPrice = Double.parseDouble(array[index].get(6));

            if (currentPrice <= pivot) {
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

    public static CSVRecord[] quickSortByPrices(CSVRecord[] array, int first, int last) {
        while (first < last) {
            int pivotPosition = partition(array, first, last);

            if (pivotPosition - first < last - pivotPosition) {
                quickSortByPrices(array, first, pivotPosition - 1);
                first = pivotPosition + 1;
            } else {
                quickSortByPrices(array, pivotPosition + 1, last);
                last = pivotPosition - 1;
            }
        }
        return array;
    }

    public static int medianOf3(CSVRecord[] array, int first, int mid, int last) {
        double firstPrice = Double.parseDouble(array[first].get(6));
        double midPrice = Double.parseDouble(array[mid].get(6));
        double lastPrice = Double.parseDouble(array[last].get(6));

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

        double pivot = Double.parseDouble(array[last].get(6));
        int iterator = first - 1;

        for (int index = first; index < last; index++) {
            double currentPrice = Double.parseDouble(array[index].get(6));

            if (currentPrice <= pivot) {
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

    public static CSVRecord[] quickSortByPricesMedianOf3(CSVRecord[] array, int first, int last) {
        while (first < last) {
            int pivotPosition = partitionMedian3(array, first, last);

            if (pivotPosition - first < last - pivotPosition) {
                quickSortByPricesMedianOf3(array, first, pivotPosition - 1);
                first = pivotPosition + 1;
            } else {
                quickSortByPricesMedianOf3(array, pivotPosition + 1, last);
                last = pivotPosition - 1;
            }
        }

        return array;
    }

    public static CSVRecord[] heapSortByPrices(CSVRecord[] array) {
        int length = array.length;

        for (int index = length / 2 - 1; index >= 0; index--) {
            heapify(array, length, index);
        }

        for (int index = length - 1; index > 0; index--) {
            CSVRecord temp = array[index];
            array[index] = array[0];
            array[0] = temp;

            heapify(array, index, 0);
        }
        return array;
    }

    private static void heapify(CSVRecord[] array, int length, int index) {
        int largest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        double largestPrice = Double.parseDouble(array[largest].get(6));

        if (left < length && Double.parseDouble(array[left].get(6)) > largestPrice) {
            largest = left;
        }

        if (right < length && Double.parseDouble(array[right].get(6)) > Double.parseDouble(array[largest].get(6))) {
            largest = right;
        }

        if (largest != index) {
            CSVRecord swap = array[index];
            array[index] = array[largest];
            array[largest] = swap;

            heapify(array, length, largest);
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
            for (int index = 0; index < records.size(); index++) {
                array[index] = records.get(index);
            }
            return array;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    public static void writeToCvs (String fileName,CSVRecord[] array) throws IOException {
        Path databasePath = Paths.get("src","main","java","database");
        File outputFile = new File(databasePath.toString(), fileName);
        File fileToGetHeader = new File(databasePath.toString(),"portuguese_supported_games.csv");

        try (
                Writer writer = new FileWriter(outputFile);
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
                Reader reader = new FileReader(fileToGetHeader);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        ) {
            printer.printRecord(csvParser.getHeaderMap().keySet());
            for (int index = 0; index < array.length; index++) {
                printer.printRecord(array[index]);
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
    public static void mainOrdenationsByPrice() throws IOException {

        Path pathToGames = Paths.get("src","main","java","database","games.csv");
        CSVRecord[] arrayToOrder = getArray(pathToGames);
        assert arrayToOrder != null;
        long startTime, endTime, duration, memoryBefore, memoryAfter, memoryUsed;
        CSVRecord[] ordenArray;
        System.out.println("Ordenações por preços para o dataSet desordenado:\n");

        // Selection Sort
        System.out.println("Selection sort | Dataset desordenado:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = selectionSortByPrices(arrayToOrder.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_selectionSort_medioCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Dataset desordenado:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = insertionSortByPrices(arrayToOrder.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_insertionSort_medioCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Dataset desordenado:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = mergeSortByPrices(arrayToOrder.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_mergeSort_medioCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Dataset desordenado:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = quickSortByPrices(arrayToOrder.clone(), 0, arrayToOrder.length - 1);
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_quickSort_medioCaso.csv", ordenArray);

        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Dataset desordenado:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = quickSortByPricesMedianOf3(arrayToOrder.clone(), 0, arrayToOrder.length - 1);
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_quickSort_mediana_de_3_medioCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Dataset desordenado:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = heapSortByPrices(arrayToOrder.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_heapSort_medioCaso.csv", ordenArray);



        System.out.println("\nPS: Para analise de Dataset crescente, usaremos o array ja ordenado por qualquer um dos métodos anteriores.");
        Path pathToCsvOrden = Paths.get("src","main","java","database","games_price_heapSort_medioCaso.csv");

        CSVRecord[] arrayBetterCase = getArray(pathToCsvOrden);
        assert arrayBetterCase != null;
        System.out.println("Ordenações por preços Dataset crescente:\n");

        // Selection Sort
        System.out.println("Selection sort | Dataset crescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = selectionSortByPrices(arrayBetterCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_selectionSort_melhorCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Dataset crescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = insertionSortByPrices(arrayBetterCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_insertionSort_melhorCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Dataset crescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = mergeSortByPrices(arrayBetterCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_mergeSort_melhorCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Dataset crescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = quickSortByPrices(arrayBetterCase.clone(), 0, arrayBetterCase.length - 1);
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_quickSort_melhorCaso.csv", ordenArray);

        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Dataset crescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = quickSortByPricesMedianOf3(arrayBetterCase.clone(), 0, arrayBetterCase.length - 1);
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_quickSort_mediana_de_3_melhorCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Dataset crescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = heapSortByPrices(arrayBetterCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_heapSort_melhorCaso.csv", ordenArray);


        System.out.println("\nPS: Para analise de Dataset decrescente, usaremos o array ja ordenado invertido.");
        CSVRecord[] arrayWorstCase = getArray(pathToCsvOrden);
        assert arrayWorstCase != null;
        reverseArray(arrayWorstCase);
        System.out.println("Ordenações por Preço Dataset decrescente:\n");

        // Selection Sort
        System.out.println("Selection sort | Dataset decrescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = selectionSortByPrices(arrayWorstCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_selectionSort_piorCaso.csv", ordenArray);

        // Insertion Sort
        System.out.println("Insertion sort | Dataset decrescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = insertionSortByPrices(arrayWorstCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_insertionSort_piorCaso.csv", ordenArray);

        // Merge Sort
        System.out.println("Merge sort | Dataset decrescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = mergeSortByPrices(arrayWorstCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_mergeSort_piorCaso.csv", ordenArray);

        // Quick Sort
        System.out.println("Quick sort | Dataset decrescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = quickSortByPrices(arrayWorstCase.clone(), 0, arrayWorstCase.length - 1);
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_quickSort_piorCaso.csv", ordenArray);

        // Quick Sort com Mediana de 3
        System.out.println("Quick sort (Mediana de 3) | Dataset decrescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = quickSortByPricesMedianOf3(arrayWorstCase.clone(), 0, arrayWorstCase.length - 1);
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_quickSort_mediana_de_3_piorCaso.csv", ordenArray);

        // Heap Sort
        System.out.println("Heap sort | Dataset decrescente:");
        startTime = System.currentTimeMillis();
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ordenArray = heapSortByPrices(arrayWorstCase.clone());
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        memoryUsed = Math.abs((memoryAfter - memoryBefore) / (1024 * 1024));
        System.out.println("Tempo de execução: " + duration + " Millisegundos");
        System.out.println("Memória utilizada: " + memoryUsed + " Megabytes\n");
        writeToCvs("games_price_heapSort_piorCaso.csv", ordenArray);



    }
}
