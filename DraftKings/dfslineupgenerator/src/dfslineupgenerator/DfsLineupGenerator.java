/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfslineupgenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.CombinatoricsUtils;
import java.nio.file.Files;

/**
 *
 * @author luke0
 */
public class DfsLineupGenerator {

    /**
     * @param args the command line arguments
     */
    private static ArrayList<dkPlayer> playerList = new ArrayList();
    private static ArrayList<dkLineUps> validLineUps = new ArrayList();
    private static HashMap<String, Integer> positionHash = new HashMap();
    private static ArrayList<dkPlayer> qbList = new ArrayList();
    private static ArrayList<dkPlayer> rbList = new ArrayList();
    private static ArrayList<dkPlayer> wrList = new ArrayList();
    private static ArrayList<dkPlayer> teList = new ArrayList();
    private static ArrayList<dkPlayer> defList = new ArrayList();
    private static dkPosition qbPosition = new dkPosition();
    private static dkPosition rbPosition = new dkPosition();
    private static dkPosition wrPosition = new dkPosition();
    private static dkPosition tePosition = new dkPosition();
    private static dkPosition defPosition = new dkPosition();
    private static ArrayList<Long> avgPossibilitiesList = new ArrayList();
    private static ArrayList<Long> oneSTDaboveAvgPossibilitiesList = new ArrayList();
    private static ArrayList<Long> twoSTDaboveAvgPossibilitiesList = new ArrayList();
    private static dkTeamBuilder avgBuilder = new dkTeamBuilder();
    private static dkTeamBuilder oneAboveAvgBuilder = new dkTeamBuilder();
    private static dkTeamBuilder twoAboveAvgBuilder = new dkTeamBuilder();

    public static void main(String[] args) {
        String fileName = "G:\\GIT\\self\\DraftKings\\dfslineupgenerator\\src\\dfslineupgenerator\\2020_Week10.csv";
        playerList = Utility.parsePlayerFile(fileName);
        buildPositionArrays();

        generateCombinationFiles("H:\\temp\\", 0);
        generateCombinationFiles("H:\\temp\\", 1);
        generateCombinationFiles("H:\\temp\\", 2);
        showAboveAverageResults();
        showOneSTDAboveAverageResults();
        showTwoSTDAboveAverageResults();
        //generateTeamCombinationFiles("G:\\temp\\week10_", 1, false);
        //oneAboveAvgBuilder.readComboFilesIntoArray();
        //oneAboveAvgBuilder.iterateThroughTeamPossibilities(false);

        generateTeamCombinationFiles("H:\\temp\\week10_", 1, false);
        oneAboveAvgBuilder.readComboFilesIntoArray();
        oneAboveAvgBuilder.iterateThroughTeamPossibilities(false);
        //twoAboveAvgBuilder.readComboFilesIntoArray();
        //twoAboveAvgBuilder.iterateThroughTeamPossibilities(false);
        System.out.println("Count of Teams in Salary Boundary: " + oneAboveAvgBuilder.countOfTeamsInSalary);
        System.out.println("Test");
    }

    public static void excludedIndexes() {
        ArrayList<Integer> excludedIndexes = new ArrayList();
        excludedIndexes.add(160);
        excludedIndexes.add(161);

    }

    public static ArrayList<dkPlayer> getPlayerList() {
        return playerList;
    }

    public static void setPlayerList(ArrayList<dkPlayer> playerList) {
        DfsLineupGenerator.playerList = playerList;
    }

    public static ArrayList<dkLineUps> getValidLineUps() {
        return validLineUps;
    }

    public static void setValidLineUps(ArrayList<dkLineUps> validLineUps) {
        DfsLineupGenerator.validLineUps = validLineUps;
    }

    public static HashMap<String, Integer> printNumberAtPosition() {
        HashMap<String, Integer> countMap = new HashMap();
        countMap = Utility.displayNumberAtPosition(getPlayerList());

        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String position = entry.getKey();
            Integer count = entry.getValue();
            System.out.println(position + " | " + count);
        }
        return countMap;
    }

    public static void createLineUps(int maxSalary) {
        if (getPlayerList() != null) {
        } else {
            System.out.println("Create player list first!");
        }
    }

    public static HashMap<String, Integer> getPositionHash() {
        return positionHash;
    }

    public static void setPositionHash(HashMap<String, Integer> positionHash) {
        DfsLineupGenerator.positionHash = positionHash;
    }

    public static void buildPlayerList(String fileName) {
        setPlayerList(Utility.parsePlayerFile(fileName));
        setPositionHash(printNumberAtPosition());
    }

    public static void buildPositionArrays() {
        dkPlayer player = new dkPlayer();
        String position;
        for (int i = 0; i < playerList.size(); i++) {
            player = playerList.get(i);
            position = player.getPosition();
            switch (position) {
                case "QB":
                    qbList.add(player);
                    break;
                case "RB":
                    rbList.add(player);
                    break;
                case "WR":
                    wrList.add(player);
                    break;
                case "TE":
                    teList.add(player);
                    break;
                case "Def":
                    defList.add(player);
                    break;
                default:
                    System.out.println("Invalid position");
                    break;
            }
        }

        ArrayList<Integer> possibleList = new ArrayList();
        possibleList.add(1);

        qbPosition.setPositionList(qbList);
        qbPosition.setPossibleTeamCount(possibleList);
        defPosition.setPositionList(defList);
        defPosition.setPossibleTeamCount(possibleList);
        possibleList = new ArrayList();
        possibleList.add(1);
        possibleList.add(2);
        tePosition.setPositionList(teList);
        tePosition.setPossibleTeamCount(possibleList);
        possibleList = new ArrayList();
        possibleList.add(2);
        possibleList.add(3);
        rbPosition.setPositionList(rbList);
        rbPosition.setPossibleTeamCount(possibleList);
        possibleList = new ArrayList();
        possibleList.add(3);
        possibleList.add(4);
        wrPosition.setPositionList(wrList);
        wrPosition.setPossibleTeamCount(possibleList);
    }

    public static void showAboveAverageResults() {
        long qb1;
        long rb2;
        long rb3;
        long wr3;
        long wr4;
        long te1;
        long te2;
        long def1;

        System.out.println("--------------------------------------------");
        System.out.println("Showing all teams that are above average.");
        System.out.println();

        System.out.println("QB Count: " + qbPosition.getAboveAverageList().size() + " | " + qbPosition.getPercentageOfPopulation(qbPosition.getAboveAverageList().size()));
        System.out.println("RB Count: " + rbPosition.getAboveAverageList().size() + " | " + rbPosition.getPercentageOfPopulation(rbPosition.getAboveAverageList().size()));
        System.out.println("WR Count: " + wrPosition.getAboveAverageList().size() + " | " + wrPosition.getPercentageOfPopulation(wrPosition.getAboveAverageList().size()));
        System.out.println("TE Count: " + tePosition.getAboveAverageList().size() + " | " + tePosition.getPercentageOfPopulation(tePosition.getAboveAverageList().size()));
        System.out.println("DEF Count: " + defPosition.getAboveAverageList().size() + " | " + defPosition.getPercentageOfPopulation(defPosition.getAboveAverageList().size()));
        avgBuilder.setQbCount(qbPosition.getAboveAverageList().size());
        avgBuilder.setRbCount(rbPosition.getAboveAverageList().size());
        avgBuilder.setWrCount(wrPosition.getAboveAverageList().size());
        avgBuilder.setTeCount(tePosition.getAboveAverageList().size());
        avgBuilder.setDefCount(defPosition.getAboveAverageList().size());

        System.out.println("---");
        qb1 = Combinatorics.calculatePermutations(qbPosition.getAboveAverageList().size(), 1);
        System.out.println("Permutation: QB(1) | " + qb1);
        rb2 = Combinatorics.calculatePermutations(rbPosition.getAboveAverageList().size(), 2);
        System.out.println("Permutation: RB(2) | " + rb2);
        rb3 = Combinatorics.calculatePermutations(rbPosition.getAboveAverageList().size(), 3);
        System.out.println("Permutation: RB(3) | " + rb3);
        wr3 = Combinatorics.calculatePermutations(wrPosition.getAboveAverageList().size(), 3);
        System.out.println("Permutation: WR(3) | " + wr3);
        wr4 = Combinatorics.calculatePermutations(wrPosition.getAboveAverageList().size(), 4);
        System.out.println("Permutation: WR(4) | " + wr4);
        te1 = Combinatorics.calculatePermutations(tePosition.getAboveAverageList().size(), 1);
        System.out.println("Permutation: TE(1) | " + te1);
        te2 = Combinatorics.calculatePermutations(tePosition.getAboveAverageList().size(), 2);
        System.out.println("Permutation: TE(2) | " + te2);
        def1 = Combinatorics.calculatePermutations(defPosition.getAboveAverageList().size(), 1);
        System.out.println("Permutation: DEF(1) | " + def1);

        avgBuilder.setQb1(qb1);
        avgBuilder.setRb2(rb2);
        avgBuilder.setRb3(rb3);
        avgBuilder.setWr3(wr3);
        avgBuilder.setWr4(wr4);
        avgBuilder.setTe1(te1);
        avgBuilder.setTe2(te2);
        avgBuilder.setDef1(def1);

        System.out.println("---");
        generateTotalPerms(qb1, rb2, rb3, wr3, wr4, te1, te2, def1);
        ArrayList<Long> possibilitiesList = new ArrayList();
        possibilitiesList.add(new Long(qbPosition.getAboveAverageList().size()));
        possibilitiesList.add(rb2);
        possibilitiesList.add(rb3);
        possibilitiesList.add(wr3);
        possibilitiesList.add(wr4);
        possibilitiesList.add(te1);
        possibilitiesList.add(te2);
        possibilitiesList.add(def1);
        setAvgPossibilitiesList(possibilitiesList);
        avgBuilder.setQbList(qbPosition.getAboveAverageList());
        avgBuilder.setRbList(rbPosition.getAboveAverageList());
        avgBuilder.setWrList(wrPosition.getAboveAverageList());
        avgBuilder.setTeList(tePosition.getAboveAverageList());
        avgBuilder.setDefList(defPosition.getAboveAverageList());
    }

    public static void showOneSTDAboveAverageResults() {
        long qb1;
        long rb2;
        long rb3;
        long wr3;
        long wr4;
        long te1;
        long te2;
        long def1;

        System.out.println("--------------------------------------------");
        System.out.println("Showing all teams that are one standard deviation above average.");
        System.out.println();

        System.out.println("QB Count: " + qbPosition.getOneSTDaboveAverageList().size() + " | " + qbPosition.getPercentageOfPopulation(qbPosition.getOneSTDaboveAverageList().size()));
        System.out.println("RB Count: " + rbPosition.getOneSTDaboveAverageList().size() + " | " + rbPosition.getPercentageOfPopulation(rbPosition.getOneSTDaboveAverageList().size()));
        System.out.println("WR Count: " + wrPosition.getOneSTDaboveAverageList().size() + " | " + wrPosition.getPercentageOfPopulation(wrPosition.getOneSTDaboveAverageList().size()));
        System.out.println("TE Count: " + tePosition.getOneSTDaboveAverageList().size() + " | " + tePosition.getPercentageOfPopulation(tePosition.getOneSTDaboveAverageList().size()));
        System.out.println("DEF Count: " + defPosition.getOneSTDaboveAverageList().size() + " | " + defPosition.getPercentageOfPopulation(defPosition.getOneSTDaboveAverageList().size()));
        oneAboveAvgBuilder.setQbCount(qbPosition.getOneSTDaboveAverageList().size());
        oneAboveAvgBuilder.setRbCount(rbPosition.getOneSTDaboveAverageList().size());
        oneAboveAvgBuilder.setWrCount(wrPosition.getOneSTDaboveAverageList().size());
        oneAboveAvgBuilder.setTeCount(tePosition.getOneSTDaboveAverageList().size());
        oneAboveAvgBuilder.setDefCount(defPosition.getOneSTDaboveAverageList().size());

        System.out.println("---");
        qb1 = Combinatorics.calculatePermutations(qbPosition.getOneSTDaboveAverageList().size(), 1);
        System.out.println("Permutation: QB(1) | " + qb1);
        rb2 = Combinatorics.calculatePermutations(rbPosition.getOneSTDaboveAverageList().size(), 2);
        System.out.println("Permutation: RB(2) | " + rb2);
        rb3 = Combinatorics.calculatePermutations(rbPosition.getOneSTDaboveAverageList().size(), 3);
        System.out.println("Permutation: RB(3) | " + rb3);
        wr3 = Combinatorics.calculatePermutations(wrPosition.getOneSTDaboveAverageList().size(), 3);
        System.out.println("Permutation: WR(3) | " + wr3);
        wr4 = Combinatorics.calculatePermutations(wrPosition.getOneSTDaboveAverageList().size(), 4);
        System.out.println("Permutation: WR(4) | " + wr4);
        te1 = Combinatorics.calculatePermutations(tePosition.getOneSTDaboveAverageList().size(), 1);
        System.out.println("Permutation: TE(1) | " + te1);
        te2 = Combinatorics.calculatePermutations(tePosition.getOneSTDaboveAverageList().size(), 2);
        System.out.println("Permutation: TE(2) | " + te2);
        def1 = Combinatorics.calculatePermutations(defPosition.getOneSTDaboveAverageList().size(), 1);
        System.out.println("Permutation: DEF(1) | " + def1);

        oneAboveAvgBuilder.setQb1(qb1);
        oneAboveAvgBuilder.setRb2(rb2);
        oneAboveAvgBuilder.setRb3(rb3);
        oneAboveAvgBuilder.setWr3(wr3);
        oneAboveAvgBuilder.setWr4(wr4);
        oneAboveAvgBuilder.setTe1(te1);
        oneAboveAvgBuilder.setTe2(te2);
        oneAboveAvgBuilder.setDef1(def1);

        System.out.println("---");
        generateTotalPerms(qb1, rb2, rb3, wr3, wr4, te1, te2, def1);
        ArrayList<Long> possibilitiesList = new ArrayList();
        possibilitiesList.add(qb1);
        possibilitiesList.add(rb2);
        possibilitiesList.add(rb3);
        possibilitiesList.add(wr3);
        possibilitiesList.add(wr4);
        possibilitiesList.add(te1);
        possibilitiesList.add(te2);
        possibilitiesList.add(def1);
        setOneSTDaboveAvgPossibilitiesList(possibilitiesList);
        oneAboveAvgBuilder.setQbList(qbPosition.getOneSTDaboveAverageList());
        oneAboveAvgBuilder.setRbList(rbPosition.getOneSTDaboveAverageList());
        oneAboveAvgBuilder.setWrList(wrPosition.getOneSTDaboveAverageList());
        oneAboveAvgBuilder.setTeList(tePosition.getOneSTDaboveAverageList());
        oneAboveAvgBuilder.setDefList(defPosition.getOneSTDaboveAverageList());
    }

    public static void showTwoSTDAboveAverageResults() {
        long qb1;
        long rb2;
        long rb3;
        long wr3;
        long wr4;
        long te1;
        long te2;
        long def1;

        System.out.println("--------------------------------------------");
        System.out.println("Showing all teams that are two standard deviations above average.");
        System.out.println();

        System.out.println("QB Count: " + qbPosition.getTwoSTDaboveAverageList().size() + " | " + qbPosition.getPercentageOfPopulation(qbPosition.getTwoSTDaboveAverageList().size()));
        System.out.println("RB Count: " + rbPosition.getTwoSTDaboveAverageList().size() + " | " + rbPosition.getPercentageOfPopulation(rbPosition.getTwoSTDaboveAverageList().size()));
        System.out.println("WR Count: " + wrPosition.getTwoSTDaboveAverageList().size() + " | " + wrPosition.getPercentageOfPopulation(wrPosition.getTwoSTDaboveAverageList().size()));
        System.out.println("TE Count: " + tePosition.getTwoSTDaboveAverageList().size() + " | " + tePosition.getPercentageOfPopulation(tePosition.getTwoSTDaboveAverageList().size()));
        System.out.println("DEF Count: " + defPosition.getTwoSTDaboveAverageList().size() + " | " + defPosition.getPercentageOfPopulation(defPosition.getTwoSTDaboveAverageList().size()));
        twoAboveAvgBuilder.setQbCount(qbPosition.getTwoSTDaboveAverageList().size());
        twoAboveAvgBuilder.setRbCount(rbPosition.getTwoSTDaboveAverageList().size());
        twoAboveAvgBuilder.setWrCount(wrPosition.getTwoSTDaboveAverageList().size());
        twoAboveAvgBuilder.setTeCount(tePosition.getTwoSTDaboveAverageList().size());
        twoAboveAvgBuilder.setDefCount(defPosition.getTwoSTDaboveAverageList().size());

        System.out.println("---");
        qb1 = Combinatorics.calculatePermutations(qbPosition.getTwoSTDaboveAverageList().size(), 1);
        System.out.println("Permutation: QB(1) | " + qb1);
        rb2 = Combinatorics.calculatePermutations(rbPosition.getTwoSTDaboveAverageList().size(), 2);
        System.out.println("Permutation: RB(2) | " + rb2);
        rb3 = Combinatorics.calculatePermutations(rbPosition.getTwoSTDaboveAverageList().size(), 3);
        System.out.println("Permutation: RB(3) | " + rb3);
        wr3 = Combinatorics.calculatePermutations(wrPosition.getTwoSTDaboveAverageList().size(), 3);
        System.out.println("Permutation: WR(3) | " + wr3);
        wr4 = Combinatorics.calculatePermutations(wrPosition.getTwoSTDaboveAverageList().size(), 4);
        System.out.println("Permutation: WR(4) | " + wr4);
        te1 = Combinatorics.calculatePermutations(tePosition.getTwoSTDaboveAverageList().size(), 1);
        System.out.println("Permutation: TE(1) | " + te1);
        te2 = Combinatorics.calculatePermutations(tePosition.getTwoSTDaboveAverageList().size(), 2);
        System.out.println("Permutation: TE(2) | " + te2);
        def1 = Combinatorics.calculatePermutations(defPosition.getTwoSTDaboveAverageList().size(), 1);
        System.out.println("Permutation: DEF(1) | " + def1);

        twoAboveAvgBuilder.setQb1(qb1);
        twoAboveAvgBuilder.setRb2(rb2);
        twoAboveAvgBuilder.setRb3(rb3);
        twoAboveAvgBuilder.setWr3(wr3);
        twoAboveAvgBuilder.setWr4(wr4);
        twoAboveAvgBuilder.setTe1(te1);
        twoAboveAvgBuilder.setTe2(te2);
        twoAboveAvgBuilder.setDef1(def1);

        System.out.println("---");
        generateTotalPerms(qb1, rb2, rb3, wr3, wr4, te1, te2, def1);
        ArrayList<Long> possibilitiesList = new ArrayList();
        possibilitiesList.add(qb1);
        possibilitiesList.add(rb2);
        possibilitiesList.add(rb3);
        possibilitiesList.add(wr3);
        possibilitiesList.add(wr4);
        possibilitiesList.add(te1);
        possibilitiesList.add(te2);
        possibilitiesList.add(def1);
        setTwoSTDaboveAvgPossibilitiesList(possibilitiesList);
        twoAboveAvgBuilder.setQbList(qbPosition.getTwoSTDaboveAverageList());
        twoAboveAvgBuilder.setRbList(rbPosition.getTwoSTDaboveAverageList());
        twoAboveAvgBuilder.setWrList(wrPosition.getTwoSTDaboveAverageList());
        twoAboveAvgBuilder.setTeList(tePosition.getTwoSTDaboveAverageList());
        twoAboveAvgBuilder.setDefList(defPosition.getTwoSTDaboveAverageList());
    }

    public static ArrayList<Long> generateTotalPerms(long qb1, long rb2, long rb3, long wr3, long wr4, long te1, long te2, long def1) {
        ArrayList<Long> possibilitiesList = new ArrayList();
        Long result;
        result = (qb1 * rb3 * wr3 * te1 * def1);
        System.out.println("Team 1 1QB,3RB,3WR,1TE,1DEF: " + qb1 + " x " + rb3 + " x " + wr3 + " x " + te1 + " x " + def1 + " = " + result);
        possibilitiesList.add(result);
        result = (qb1 * rb2 * wr4 * te1 * def1);
        System.out.println("Team 2 1QB,2RB,4WR,1TE,1DEF: " + qb1 + " x " + rb2 + " x " + wr4 + " x " + te1 + " x " + def1 + " = " + result);
        possibilitiesList.add(result);
        result = (qb1 * rb2 * wr3 * te2 * def1);
        System.out.println("Team 3 1QB,2RB,3WR,2TE,1DEF: " + qb1 + " x " + rb2 + " x " + wr3 + " x " + te2 + " x " + def1 + " = " + result);
        possibilitiesList.add(result);
        System.out.println("---");
        Long total = new Long(0);
        for (int i = 0; i < possibilitiesList.size(); i++) {
            total = total + possibilitiesList.get(i);
        }

        System.out.println("Total Number of Teams: " + total);
        System.out.println("---");
        return possibilitiesList;
    }

    public static void generateCombinationFiles(String dir, int std) {
        File directory = new File(dir);
        if (!(directory.exists())) {
            directory.mkdirs();
        }

        switch (std) {
            case 0:
                System.out.println("Generating all Average Combination Files in " + dir);
                for (int i = 0; i < 8; i++) {
                    String fileName = new String();
                    switch (i) {
                        case 0:
                            fileName = dir + qbPosition.getAboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, qbPosition.getAboveAverageList().size(), 1, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                        case 1:
                            fileName = dir + rbPosition.getAboveAverageList().size() + "-2.csv";
                            Combinatorics.generatePermutationFile(fileName, rbPosition.getAboveAverageList().size(), 2, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                        case 2:
                            fileName = dir + rbPosition.getAboveAverageList().size() + "-3.csv";
                            Combinatorics.generatePermutationFile(fileName, rbPosition.getAboveAverageList().size(), 3, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                        case 3:
                            fileName = dir + wrPosition.getAboveAverageList().size() + "-3.csv";
                            Combinatorics.generatePermutationFile(fileName, wrPosition.getAboveAverageList().size(), 3, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                        case 4:
                            fileName = dir + wrPosition.getAboveAverageList().size() + "-4.csv";
                            Combinatorics.generatePermutationFile(fileName, wrPosition.getAboveAverageList().size(), 4, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                        case 5:
                            fileName = dir + tePosition.getAboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, tePosition.getAboveAverageList().size(), 1, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                        case 6:
                            fileName = dir + tePosition.getAboveAverageList().size() + "-2.csv";
                            Combinatorics.generatePermutationFile(fileName, tePosition.getAboveAverageList().size(), 2, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                        case 7:
                            fileName = dir + defPosition.getAboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, defPosition.getAboveAverageList().size(), 1, 100000);
                            avgBuilder.positionfileList.add(fileName);
                            break;
                    }
                }
                break;
            case 1:
                System.out.println("Generating all Combination Files for One Standard Deviation Above Average in " + dir);
                for (int i = 0; i < 8; i++) {
                    String fileName = new String();
                    switch (i) {
                        case 0:
                            fileName = dir + qbPosition.getOneSTDaboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, qbPosition.getOneSTDaboveAverageList().size(), 1, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 1:
                            fileName = dir + rbPosition.getOneSTDaboveAverageList().size() + "-2.csv";
                            Combinatorics.generatePermutationFile(fileName, rbPosition.getOneSTDaboveAverageList().size(), 2, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 2:
                            fileName = dir + rbPosition.getOneSTDaboveAverageList().size() + "-3.csv";
                            Combinatorics.generatePermutationFile(fileName, rbPosition.getOneSTDaboveAverageList().size(), 3, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 3:
                            fileName = dir + wrPosition.getOneSTDaboveAverageList().size() + "-3.csv";
                            Combinatorics.generatePermutationFile(fileName, wrPosition.getOneSTDaboveAverageList().size(), 3, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 4:
                            fileName = dir + wrPosition.getOneSTDaboveAverageList().size() + "-4.csv";
                            Combinatorics.generatePermutationFile(fileName, wrPosition.getOneSTDaboveAverageList().size(), 4, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 5:
                            fileName = dir + tePosition.getOneSTDaboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, tePosition.getOneSTDaboveAverageList().size(), 1, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 6:
                            fileName = dir + tePosition.getOneSTDaboveAverageList().size() + "-2.csv";
                            Combinatorics.generatePermutationFile(fileName, tePosition.getOneSTDaboveAverageList().size(), 2, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 7:
                            fileName = dir + defPosition.getOneSTDaboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, defPosition.getOneSTDaboveAverageList().size(), 1, 100000);
                            oneAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                    }
                }
                break;
            case 2:
                System.out.println("Generating all Combination Files for Two Standard Deviations Above Average in " + dir);
                for (int i = 0; i < 8; i++) {
                    String fileName = new String();
                    switch (i) {
                        case 0:
                            fileName = dir + qbPosition.getTwoSTDaboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, qbPosition.getTwoSTDaboveAverageList().size(), 1, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 1:
                            fileName = dir + rbPosition.getTwoSTDaboveAverageList().size() + "-2.csv";
                            Combinatorics.generatePermutationFile(fileName, rbPosition.getTwoSTDaboveAverageList().size(), 2, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 2:
                            fileName = dir + rbPosition.getTwoSTDaboveAverageList().size() + "-3.csv";
                            Combinatorics.generatePermutationFile(fileName, rbPosition.getTwoSTDaboveAverageList().size(), 3, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 3:
                            fileName = dir + wrPosition.getTwoSTDaboveAverageList().size() + "-3.csv";
                            Combinatorics.generatePermutationFile(fileName, wrPosition.getTwoSTDaboveAverageList().size(), 3, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 4:
                            fileName = dir + wrPosition.getTwoSTDaboveAverageList().size() + "-4.csv";
                            Combinatorics.generatePermutationFile(fileName, wrPosition.getTwoSTDaboveAverageList().size(), 4, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 5:
                            fileName = dir + tePosition.getTwoSTDaboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, tePosition.getTwoSTDaboveAverageList().size(), 1, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 6:
                            fileName = dir + tePosition.getTwoSTDaboveAverageList().size() + "-2.csv";
                            Combinatorics.generatePermutationFile(fileName, tePosition.getTwoSTDaboveAverageList().size(), 2, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                        case 7:
                            fileName = dir + defPosition.getTwoSTDaboveAverageList().size() + "-1.csv";
                            Combinatorics.generatePermutationFile(fileName, defPosition.getTwoSTDaboveAverageList().size(), 1, 100000);
                            twoAboveAvgBuilder.positionfileList.add(fileName);
                            break;
                    }
                }
                break;
        }

    }

    public static ArrayList<Long> getAvgPossibilitiesList() {
        return avgPossibilitiesList;
    }

    public static void setAvgPossibilitiesList(ArrayList<Long> avgPossibilitiesList) {
        DfsLineupGenerator.avgPossibilitiesList = avgPossibilitiesList;
    }

    public static ArrayList<Long> getOneSTDaboveAvgPossibilitiesList() {
        return oneSTDaboveAvgPossibilitiesList;
    }

    public static void setOneSTDaboveAvgPossibilitiesList(ArrayList<Long> oneSTDaboveAvgPossibilitiesList) {
        DfsLineupGenerator.oneSTDaboveAvgPossibilitiesList = oneSTDaboveAvgPossibilitiesList;
    }

    public static ArrayList<Long> getTwoSTDaboveAvgPossibilitiesList() {
        return twoSTDaboveAvgPossibilitiesList;
    }

    public static void setTwoSTDaboveAvgPossibilitiesList(ArrayList<Long> twoSTDaboveAvgPossibilitiesList) {
        DfsLineupGenerator.twoSTDaboveAvgPossibilitiesList = twoSTDaboveAvgPossibilitiesList;
    }

    public static void generateTeamCombinationFiles(String dir, int std, boolean generateFiles) {
        File directory = new File(dir);
        if (!(directory.exists())) {
            directory.mkdirs();
        }

        switch (std) {
            case 0:
                System.out.println("Generating all Average Team Combination Files in " + dir);
                for (int i = 0; i < 3; i++) {
                    String fileName = new String();
                    switch (i) {
                        case 0:
                            fileName = dir + "_avg_team1_1qb_3rb_3wr_1te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) avgBuilder.getQb1(), (int) avgBuilder.getRb3(), (int) avgBuilder.getWr3(), (int) avgBuilder.getTe1(), (int) avgBuilder.getDef1(), fileName);
                            }
                            avgBuilder.getTeamFileList().add(fileName);
                            break;
                        case 1:
                            fileName = dir + "_avg_team1_1qb_2rb_4wr_1te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) avgBuilder.getQb1(), (int) avgBuilder.getRb2(), (int) avgBuilder.getWr4(), (int) avgBuilder.getTe1(), (int) avgBuilder.getDef1(), fileName);
                            }
                            avgBuilder.getTeamFileList().add(fileName);
                            break;
                        case 2:
                            fileName = dir + "_avg_team1_1qb_2rb_3wr_2te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) avgBuilder.getQb1(), (int) avgBuilder.getRb2(), (int) avgBuilder.getWr3(), (int) avgBuilder.getTe2(), (int) avgBuilder.getDef1(), fileName);
                            }
                            avgBuilder.getTeamFileList().add(fileName);
                            break;
                    }
                }
                break;
            case 1:
                System.out.println("Generating all Team Files for One Standard Deviation Above Average in " + dir);
                for (int i = 0; i < 3; i++) {
                    String fileName = new String();
                    switch (i) {
                        case 0:
                            fileName = dir + "_1avg_team1_1qb_3rb_3wr_1te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) oneAboveAvgBuilder.getQb1(), (int) oneAboveAvgBuilder.getRb3(), (int) oneAboveAvgBuilder.getWr3(), (int) oneAboveAvgBuilder.getTe1(), (int) oneAboveAvgBuilder.getDef1(), fileName);
                            }
                            oneAboveAvgBuilder.getTeamFileList().add(fileName);
                            break;
                        case 1:
                            fileName = dir + "_1avg_team1_1qb_2rb_4wr_1te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) oneAboveAvgBuilder.getQb1(), (int) oneAboveAvgBuilder.getRb2(), (int) oneAboveAvgBuilder.getWr4(), (int) oneAboveAvgBuilder.getTe1(), (int) oneAboveAvgBuilder.getDef1(), fileName);
                            }
                            oneAboveAvgBuilder.getTeamFileList().add(fileName);
                            break;
                        case 2:
                            fileName = dir + "_1avg_team1_1qb_2rb_3wr_2te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) oneAboveAvgBuilder.getQb1(), (int) oneAboveAvgBuilder.getRb2(), (int) oneAboveAvgBuilder.getWr3(), (int) oneAboveAvgBuilder.getTe2(), (int) oneAboveAvgBuilder.getDef1(), fileName);
                            }
                            oneAboveAvgBuilder.getTeamFileList().add(fileName);
                            break;
                    }
                }
                break;
            case 2:
                System.out.println("Generating all Team Files for Two Standard Deviations Above Average in " + dir);
                for (int i = 0; i < 3; i++) {
                    String fileName = new String();
                    switch (i) {
                        case 0:
                            fileName = dir + "_2avg_team1_1qb_3rb_3wr_1te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) twoAboveAvgBuilder.getQb1(), (int) twoAboveAvgBuilder.getRb3(), (int) twoAboveAvgBuilder.getWr3(), (int) twoAboveAvgBuilder.getTe1(), (int) twoAboveAvgBuilder.getDef1(), fileName);
                            }
                            twoAboveAvgBuilder.getTeamFileList().add(fileName);
                            break;
                        case 1:
                            fileName = dir + "_2avg_team1_1qb_2rb_4wr_1te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) twoAboveAvgBuilder.getQb1(), (int) twoAboveAvgBuilder.getRb2(), (int) twoAboveAvgBuilder.getWr4(), (int) twoAboveAvgBuilder.getTe1(), (int) twoAboveAvgBuilder.getDef1(), fileName);
                            }
                            twoAboveAvgBuilder.getTeamFileList().add(fileName);
                            break;
                        case 2:
                            fileName = dir + "_2avg_team1_1qb_2rb_3wr_2te_1def.csv";
                            if (generateFiles) {
                                GFG.buildVector((int) twoAboveAvgBuilder.getQb1(), (int) twoAboveAvgBuilder.getRb2(), (int) twoAboveAvgBuilder.getWr3(), (int) twoAboveAvgBuilder.getTe2(), (int) twoAboveAvgBuilder.getDef1(), fileName);
                            }
                            twoAboveAvgBuilder.getTeamFileList().add(fileName);
                            break;
                    }
                }
                break;
        }

    }

}
