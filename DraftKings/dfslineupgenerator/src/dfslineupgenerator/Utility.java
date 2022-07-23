/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfslineupgenerator;

import static dfslineupgenerator.DfsLineupGenerator.getPlayerList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.csv.*;
import org.apache.commons.collections4.CollectionUtils;

/**
 *
 * @author luke0
 */
public class Utility {

    private static final String NEW_LINE_SEPARATOR = "\n";

    public static ArrayList<String> parseFile(String fileName) {
        File file = new File(fileName);
        ArrayList<String> listOfLines = new ArrayList();
        try {
            listOfLines = new ArrayList<String>(FileUtils.readLines(file, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return listOfLines;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static ArrayList<dkPlayer> parsePlayerFile(String fileName) {
        ArrayList<dkPlayer> playerList = new ArrayList();
        try {
            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                dkPlayer newPlayer = new dkPlayer();
                newPlayer.setWeek(NumberUtils.toInt(record.get(0), -1));
                newPlayer.setYear(NumberUtils.toInt(record.get(1), -1));
                newPlayer.setGID(NumberUtils.toInt(record.get(2), -1));
                newPlayer.setName(record.get(3));
                newPlayer.setPosition(record.get(4));
                newPlayer.setTeam(record.get(5));
                newPlayer.setDkPoints(NumberUtils.toDouble(record.get(8), 0.00));
                newPlayer.setSalary(NumberUtils.toDouble(record.get(9), 0.00));
                newPlayer.setEnabled(false);
                newPlayer.setPpd();
                playerList.add(newPlayer);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        return playerList;
    }

    public static HashMap<String, Integer> displayNumberAtPosition(ArrayList<dkPlayer> myPlayerList) {
        HashMap<String, Integer> myHashMap = new HashMap();

        for (dkPlayer player : getPlayerList()) {
            if (!(myHashMap.containsKey(player.getPosition()))) {
                myHashMap.put(player.getPosition(), 1);
            } else {
                myHashMap.put(player.getPosition(), myHashMap.get(player.getPosition()) + 1);
            }
        }

        return myHashMap;
    }

    public static void heaps_algorithm(int[] a, int n) {
        if (n == 1) {
            System.out.println(Arrays.toString(a));
            return;
        }
        for (int i = 0; i < (n - 1); i++) {
            heaps_algorithm(a, n - 1);
            if (n % 2 == 0) {
                swap(a, n - 1, i);
            } else {
                swap(a, n - 1, 0);
            }
        }
        heaps_algorithm(a, n - 1);
    }

    public static void swap(int[] a, int i1, int i2) {
        int tmp = a[i1];
        a[i1] = a[i2];
        a[i2] = tmp;
    }

    public static void printIterator(Iterator<int[]> i) {
        while (i.hasNext()) {
            System.out.println(Arrays.toString(i.next()));
        }
    }

    public static void printIterator(PermutationIterator<Integer> myPerm) {
        while (myPerm.hasNext()) {
            System.out.println(myPerm.next().toString());
        }
    }

    public static void writeToFile(String fileName, boolean appendToFile, ArrayList myArray) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        try {

            fileWriter = new FileWriter(fileName, appendToFile);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecords(myArray);

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (Exception e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }

    public static long countLinesInFile(String fileName) {
        long lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.readLine() != null) {
                lines++;
            }
            reader.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.getStackTrace();
        }
        return lines;
    }

    public static long countLinesInFile(String fileName, ArrayList<Integer> excludedIndexes) {
        long lines = 0;
        int numOfIndexes;
        numOfIndexes = numberOfIndexes(fileName) + 1;

        ArrayList<Integer> myArray = new ArrayList();
        try {
            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                for (int i = 0; i < numOfIndexes; i++) {
                    myArray.add(NumberUtils.toInt(record.get(i)));
                }
                if (!(CollectionUtils.containsAny(excludedIndexes, myArray))) {
                    lines++;
                }
                myArray.clear();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return lines;
    }

    public static ArrayList<ArrayList<Integer>> indexPermutationFile(String fileName) {
        int lines = 0;
        int numOfIndexes;
        int arraySize = getHighestIndexInFile(fileName);
        numOfIndexes = numberOfIndexes(fileName) + 1;

        ArrayList<ArrayList<Integer>> myArray = new ArrayList();
        
        for (int i = 0; i <= arraySize; i++) {
            ArrayList<Integer> myIntegerArray = new ArrayList();
            myArray.add(myIntegerArray);
        }
        try (Reader in = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                for (int i = 0; i < numOfIndexes; i++) {
                    if (NumberUtils.toInt(record.get(i)) >= arraySize)
                        System.out.println(record.get(i) + "|" + lines);
                      myArray.get(NumberUtils.toInt(record.get(i))).add(lines);
                }
                lines++;
                if( lines % 10000 == 0) {
                    System.out.println(lines);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return myArray;
    }

    public static int getHighestIndexInFile(String fileName) {
        int numOfIndexes;
        numOfIndexes = numberOfIndexes(fileName) + 1;
        int highestNumber = -1;

        try (Reader in = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                for (int i = 0; i < numOfIndexes; i++) {
                    if (NumberUtils.toInt(record.get(i)) > highestNumber) {
                        highestNumber = NumberUtils.toInt(record.get(i));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return highestNumber;
    }

    public static int numberOfIndexes(String fileName) {
        int indexCount = 0;

        try (BufferedReader brTest = readFile(fileName)){
            String text = brTest.readLine();
            if (text == null) {
                return -1;
            } else {
                indexCount = StringUtils.countMatches(text, ',');
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return indexCount;
    }

    public static BufferedReader readFile(String fileName) {
        BufferedReader brTest = null;
        try {
            brTest = new BufferedReader(new FileReader(fileName));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return brTest;
    }
    
        public static ArrayList<ArrayList<Integer>> readCombinationFile(String fileName, int numOfIndexes) {

        ArrayList<ArrayList<Integer>> bigList = new ArrayList();
        ArrayList<Integer> myArray = new ArrayList();
        try {
            Reader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                for (int i = 0; i < numOfIndexes; i++) {
                    myArray.add(NumberUtils.toInt(record.get(i)));
                }
                ArrayList<Integer> addList = new ArrayList(myArray);
                bigList.add(addList);
                myArray.clear();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return bigList;
    }

}
