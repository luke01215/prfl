/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfslineupgenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 *
 * @author luke0
 */
public class Combinatorics {

    /**
     * permutation function
     *
     * @param str string to calculate permutation for
     * @param l starting index
     * @param r end index
     */
    private void permute(String str, int l, int r) {
        if (l == r) {
            System.out.println(str);
        } else {
            for (int i = l; i <= r; i++) {
                str = swap(str, l, i);
                permute(str, l + 1, r);
                str = swap(str, l, i);
            }
        }
    }

    /**
     * Swap Characters at position
     *
     * @param a string value
     * @param i position 1
     * @param j position 2
     * @return swapped string
     */
    public String swap(String a, int i, int j) {
        char temp;
        char[] charArray = a.toCharArray();
        temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    public static Iterator<int[]> generateCombinations(int poolSize, int numberFromPool) {
        Iterator<int[]> iteration;
        iteration = CombinatoricsUtils.combinationsIterator(poolSize, numberFromPool);
        return iteration;
    }

    public static PermutationIterator<Integer> generatePermutations(int sizeOfCollection) {
        ArrayList<Integer> myList = new ArrayList();
        if (sizeOfCollection < 0) {
            return null;
        } else {
            for (int i = 0; i < sizeOfCollection; i++) {
                myList.add(i);
            }
        }
        PermutationIterator<Integer> myIterator = new PermutationIterator(myList);
        return myIterator;
    }

    public static void generatePermutationFile(String fileName, int sizeOfCollection, int numberPicked, int dumpOnRecordCount) {

        try {
            int i = 0;
            long totalSize = 0;
            ArrayList bigArray = new ArrayList();
            Iterator<int[]> myIterator;
            myIterator = CombinatoricsUtils.combinationsIterator(sizeOfCollection, numberPicked);
            File fileToWrite = new File(fileName);
            totalSize = CombinatoricsUtils.binomialCoefficient(sizeOfCollection, numberPicked);
            if (fileToWrite.exists()) {
                if (Utility.countLinesInFile(fileName) == totalSize) {
                    System.out.println("File already exists.");
                } else {
                    fileToWrite.delete();
                    while (myIterator.hasNext()) {
                        int[] iArray = myIterator.next();
                        List<Integer> myList = Arrays.stream(iArray).boxed().collect(Collectors.toList());
                        bigArray.add(myList);
                        i++;
                        if (bigArray.size() % dumpOnRecordCount == 0) {
                            Utility.writeToFile(fileName, true, bigArray);
                            bigArray.clear();
                            System.out.println(i + " printed out of " + totalSize);
                        }
                    }
                    Utility.writeToFile(fileName, true, bigArray);
                    System.out.println(Utility.countLinesInFile(fileName));
                }
            }
            else {
                fileToWrite.createNewFile();
                    while (myIterator.hasNext()) {
                        int[] iArray = myIterator.next();
                        List<Integer> myList = Arrays.stream(iArray).boxed().collect(Collectors.toList());
                        bigArray.add(myList);
                        i++;
                        if (bigArray.size() % dumpOnRecordCount == 0) {
                            Utility.writeToFile(fileName, true, bigArray);
                            bigArray.clear();
                            System.out.println(i + " printed out of " + totalSize);
                        }
                    }
                    Utility.writeToFile(fileName, true, bigArray);
                    System.out.println(Utility.countLinesInFile(fileName));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long calculatePermutations(int n, int k) {
        if (n <= 0 || k < 1 || k > n) {
            System.out.println("Bad inputs");
            return -1;
        } else {
            return CombinatoricsUtils.binomialCoefficient(n, k);
        }
    }

}
