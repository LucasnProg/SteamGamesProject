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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class Transformations {

    public static String convertDate(String dateStr) {
        DateTimeFormatter inputFormat, outputFormat;
        LocalDate date;
        try {
            inputFormat = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
            outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(dateStr, inputFormat);
            dateStr = date.format(outputFormat);
            return dateStr;
        } catch (DateTimeParseException e) {
            try {
                inputFormat = DateTimeFormatter.ofPattern("MMM yyyy dd", Locale.ENGLISH);
                outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                date = LocalDate.parse(dateStr + " 01", inputFormat);
                dateStr = date.format(outputFormat);
                return dateStr;
            } catch (Exception exception) {
                System.out.println("Error: " + exception.getMessage());
                throw e;
            }
        }
    }

    public static void toReleaseDate(Path filePath) throws IOException {
        File games = filePath.toFile();
        File gamesFormated = new File(games.getParent(), "games_formated_release_data.csv");

        try (
                Reader reader = new FileReader(games);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
                Writer writer = new FileWriter(gamesFormated);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(csvParser.getHeaderMap().keySet().toArray(new String[0])))

        ) {
            for (CSVRecord record : csvParser) {
                List<String> row = record.toList();
                row.set(2, convertDate(record.get(2)));

                csvPrinter.printRecord(row);
            }
        } catch (IOException e) {
            System.out.println("Erro: "+e.getMessage());
        }

    }

    public static void getLinuxGames(Path filePath) throws IOException {
        File gamesFormated = filePath.toFile();
        File linuxGames = new File(gamesFormated.getParent(), "games_linux.csv");

        try (
                Reader reader = new FileReader(gamesFormated);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
                Writer writer = new FileWriter(linuxGames);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(csvParser.getHeaderMap().keySet().toArray(new String[0])))

        ) {
            for (CSVRecord record : csvParser) {
                if ("True".equals(record.get("Linux"))){
                    csvPrinter.printRecord(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro: "+e.getMessage());

        }

    }

    public static void getPortugueseGames(Path filePath) throws IOException {
        File gamesFormated = filePath.toFile();
        File portuguesesGames = new File(gamesFormated.getParent(), "portuguese_supported_games.csv");

        try (
                Reader reader = new FileReader(gamesFormated);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
                Writer writer = new FileWriter(portuguesesGames);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(csvParser.getHeaderMap().keySet().toArray(new String[0])))

        ) {
            for (CSVRecord record : csvParser) {
                String content = record.get(10).trim().replaceAll("[\\[\\]']", "");
                String[] languages = content.split(",\\s*");
                for (String lang : languages) {
                    if ("Portuguese - Brazil".equalsIgnoreCase(lang.trim())) {
                        csvPrinter.printRecord(record);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro: "+e.getMessage());
        }

    }

    public static void mainTransformations() throws IOException {
        Path pathToGames = Paths.get("src\\main\\java\\database\\games.csv");
        Path pathToGamesFormated = Paths.get("src\\main\\java\\database\\games_formated_release_data.csv");
        toReleaseDate(pathToGames);
        getLinuxGames(pathToGamesFormated);
        getPortugueseGames(pathToGamesFormated);
    }


}

