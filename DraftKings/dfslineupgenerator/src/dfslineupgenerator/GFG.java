/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfslineupgenerator;

/**
 *
 * @author luke0
 */
// Java program to find combinations from n 
// arrays such that one element from each 
// array is present
import java.io.File;
import java.util.*;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.CombinatoricsUtils;

class GFG {

// Function to print combinations that contain
// one element from each of the given arrays
    static void print(Vector<Integer>[] arr, String fileName, int dumpOnRecordCount) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        StringBuilder sb = new StringBuilder();
        File fileToWrite = new File(fileName);
        long j = 0;
        // Number of arrays
        try {

            if (fileToWrite.exists()) {
                fileToWrite.delete();

            }

            int n = arr.length;

            // To keep track of next element in 
            // each of the n arrays
            int[] indices = new int[n];

            // Initialize with first element's index
            for (int i = 0; i < n; i++) {
                indices[i] = 0;
            }

            while (true) {

                // Print current combination
                for (int i = 0; i < n; i++) {
                    if (i + 1 == n) {
                        sb.append(arr[i].get(indices[i]));
                        list.add(sb.toString().split(","));
                        j++;
                        sb.setLength(0);
                        if (list.size() % dumpOnRecordCount == 0) {
                            Utility.writeToFile(fileName, true, list);
                            list.clear();
                            System.out.println(j + " printed.");
                        }

                    } else {
                        sb.append(arr[i].get(indices[i]));
                        sb.append(",");
                    }
                }

                //System.out.println();
                // Find the rightmost array that has more
                // elements left after the current element 
                // in that array
                int next = n - 1;
                while (next >= 0
                        && (indices[next] + 1
                        >= arr[next].size())) {
                    next--;
                }

                // No such array is found so no more 
                // combinations left
                if (next < 0) {
                    Utility.writeToFile(fileName, true, list);
                    list.clear();
                    System.out.println(j + " printed.");
                    return;
                }

                // If found move to next element in that 
                // array
                indices[next]++;

                // For all arrays to the right of this 
                // array current index again points to 
                // first element
                for (int i = next + 1; i < n; i++) {
                    indices[i] = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void buildVector(int qb, int rb, int wr, int te, int def, String fileName) {

        // Initializing a vector with 3 empty vectors
        Vector<Integer>[] arr = new Vector[5];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Vector<Integer>();
        }

        for (int i = 0; i < qb; i++) {
            arr[0].add(i);
        }

        for (int i = 0; i < rb; i++) {
            arr[1].add(i);
        }

        for (int i = 0; i < wr; i++) {
            arr[2].add(i);
        }

        for (int i = 0; i < te; i++) {
            arr[3].add(i);
        }

        for (int i = 0; i < def; i++) {
            arr[4].add(i);
        }

        print(arr, fileName, 1000000);
        return;
    }
}
