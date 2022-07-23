/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfslineupgenerator;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.File;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author luke0
 */
public class dkTeamBuilder {

    ArrayList<String> positionfileList = new ArrayList<String>();
    ArrayList<String> teamFileList = new ArrayList<String>();
    ArrayList<ArrayList<Integer>> qb1List = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> rb2List = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> rb3List = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> wr3List = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> wr4List = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> te1List = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> te2List = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> def1List = new ArrayList<ArrayList<Integer>>();
    ArrayList<dkPlayer> qbList = new ArrayList();
    ArrayList<dkPlayer> rbList = new ArrayList();
    ArrayList<dkPlayer> wrList = new ArrayList();
    ArrayList<dkPlayer> teList = new ArrayList();
    ArrayList<dkPlayer> defList = new ArrayList();
    double maxSalary = 50000.0;
    int countOfTeamsInSalary;
    

    int qbCount;
    int rbCount;
    int wrCount;
    int teCount;
    int defCount;
    long qb1;
    long rb2;
    long rb3;
    long wr3;
    long wr4;
    long te1;
    long te2;
    long def1;

    public int getQbCount() {
        return qbCount;
    }

    public void setQbCount(int qbCount) {
        this.qbCount = qbCount;
    }

    public int getRbCount() {
        return rbCount;
    }

    public void setRbCount(int rbCount) {
        this.rbCount = rbCount;
    }

    public int getWrCount() {
        return wrCount;
    }

    public void setWrCount(int wrCount) {
        this.wrCount = wrCount;
    }

    public int getTeCount() {
        return teCount;
    }

    public void setTeCount(int teCount) {
        this.teCount = teCount;
    }

    public int getDefCount() {
        return defCount;
    }

    public void setDefCount(int defCount) {
        this.defCount = defCount;
    }

    public ArrayList<String> getFileList() {
        return positionfileList;
    }

    public void setFileList(ArrayList<String> fileList) {
        this.positionfileList = fileList;
    }

    public long getQb1() {
        return qb1;
    }

    public void setQb1(long qb1) {
        this.qb1 = qb1;
    }

    public long getRb2() {
        return rb2;
    }

    public void setRb2(long rb2) {
        this.rb2 = rb2;
    }

    public long getRb3() {
        return rb3;
    }

    public void setRb3(long rb3) {
        this.rb3 = rb3;
    }

    public long getWr3() {
        return wr3;
    }

    public void setWr3(long wr3) {
        this.wr3 = wr3;
    }

    public long getWr4() {
        return wr4;
    }

    public void setWr4(long wr4) {
        this.wr4 = wr4;
    }

    public long getTe1() {
        return te1;
    }

    public void setTe1(long te1) {
        this.te1 = te1;
    }

    public long getTe2() {
        return te2;
    }

    public void setTe2(long te2) {
        this.te2 = te2;
    }

    public long getDef1() {
        return def1;
    }

    public void setDef1(long def1) {
        this.def1 = def1;
    }

    public ArrayList<String> getTeamFileList() {
        return teamFileList;
    }

    public void setTeamFileList(ArrayList<String> teamFileList) {
        this.teamFileList = teamFileList;
    }

    public ArrayList<String> getPositionfileList() {
        return positionfileList;
    }

    public void setPositionfileList(ArrayList<String> positionfileList) {
        this.positionfileList = positionfileList;
    }

    public ArrayList<ArrayList<Integer>> getQb1List() {
        return qb1List;
    }

    public void setQb1List(ArrayList<ArrayList<Integer>> qb1List) {
        this.qb1List = qb1List;
    }

    public ArrayList<ArrayList<Integer>> getRb2List() {
        return rb2List;
    }

    public void setRb2List(ArrayList<ArrayList<Integer>> rb2List) {
        this.rb2List = rb2List;
    }

    public ArrayList<ArrayList<Integer>> getRb3List() {
        return rb3List;
    }

    public void setRb3List(ArrayList<ArrayList<Integer>> rb3List) {
        this.rb3List = rb3List;
    }

    public ArrayList<ArrayList<Integer>> getWr3List() {
        return wr3List;
    }

    public void setWr3List(ArrayList<ArrayList<Integer>> wr3List) {
        this.wr3List = wr3List;
    }

    public ArrayList<ArrayList<Integer>> getWr4List() {
        return wr4List;
    }

    public void setWr4List(ArrayList<ArrayList<Integer>> wr4List) {
        this.wr4List = wr4List;
    }

    public ArrayList<ArrayList<Integer>> getTe1List() {
        return te1List;
    }

    public void setTe1List(ArrayList<ArrayList<Integer>> te1List) {
        this.te1List = te1List;
    }

    public ArrayList<ArrayList<Integer>> getTe2List() {
        return te2List;
    }

    public void setTe2List(ArrayList<ArrayList<Integer>> te2List) {
        this.te2List = te2List;
    }

    public ArrayList<ArrayList<Integer>> getDef1List() {
        return def1List;
    }

    public void setDef1List(ArrayList<ArrayList<Integer>> def1List) {
        this.def1List = def1List;
    }

    public void readComboFilesIntoArray() {
        String fileName = new String();
        for (int i = 0; i < getPositionfileList().size(); i++) {
            fileName = getPositionfileList().get(i);
            switch (i) {
                case 0:
                    setQb1List(Utility.readCombinationFile(fileName, 1));
                    break;
                case 1:
                    setRb2List(Utility.readCombinationFile(fileName, 2));
                    break;
                case 2:
                    setRb3List(Utility.readCombinationFile(fileName, 3));
                    break;
                case 3:
                    setWr3List(Utility.readCombinationFile(fileName, 3));
                    break;
                case 4:
                    setWr4List(Utility.readCombinationFile(fileName, 4));
                    break;
                case 5:
                    setTe1List(Utility.readCombinationFile(fileName, 1));
                    break;
                case 6:
                    setTe2List(Utility.readCombinationFile(fileName, 2));
                    break;
                case 7:
                    setDef1List(Utility.readCombinationFile(fileName, 1));
                    break;
            }
        }
    }

    public ArrayList<dkPlayer> getQbList() {
        return qbList;
    }

    public void setQbList(ArrayList<dkPlayer> qbList) {
        this.qbList = qbList;
    }

    public ArrayList<dkPlayer> getRbList() {
        return rbList;
    }

    public void setRbList(ArrayList<dkPlayer> rbList) {
        this.rbList = rbList;
    }

    public ArrayList<dkPlayer> getWrList() {
        return wrList;
    }

    public void setWrList(ArrayList<dkPlayer> wrList) {
        this.wrList = wrList;
    }

    public ArrayList<dkPlayer> getTeList() {
        return teList;
    }

    public void setTeList(ArrayList<dkPlayer> teList) {
        this.teList = teList;
    }

    public ArrayList<dkPlayer> getDefList() {
        return defList;
    }

    public void setDefList(ArrayList<dkPlayer> defList) {
        this.defList = defList;
    }

    public void iterateThroughTeamPossibilities(boolean output) {
        String fileName;
        ArrayList<ArrayList<Integer>> teamList = new ArrayList();
        for (int i = 0; i < getTeamFileList().size(); i++) {
            fileName = getTeamFileList().get(i);
            switch (i) {
                case 0:
                    try {
                        Reader in = new FileReader(fileName);
                        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
                        ArrayList<Integer> recordList = new ArrayList();
                        for (CSVRecord record : records) {
                            for (int j = 0; j < 5; j++) {
                                int index = NumberUtils.toInt(record.get(j));
                                recordList.add(index);
                                switch (j) {
                                    case 0:
                                        teamList.add(new ArrayList(getQb1List().get(index)));
                                        break;
                                    case 1:
                                        teamList.add(new ArrayList(getRb3List().get(index)));
                                        break;
                                    case 2:
                                        teamList.add(new ArrayList(getWr3List().get(index)));
                                        break;
                                    case 3:
                                        teamList.add(new ArrayList(getTe1List().get(index)));
                                        break;
                                    case 4:
                                        teamList.add(new ArrayList(getDef1List().get(index)));
                                        break;
                                }
                            }
                            printTeam(teamList, output);
                            teamList.clear();
                            recordList.clear();
                        }

                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        Reader in = new FileReader(fileName);
                        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
                        ArrayList<Integer> recordList = new ArrayList();
                        for (CSVRecord record : records) {
                            for (int j = 0; j < 5; j++) {
                                int index = NumberUtils.toInt(record.get(j));
                                recordList.add(index);
                                switch (j) {
                                    case 0:
                                        teamList.add(new ArrayList(getQb1List().get(index)));
                                        break;
                                    case 1:
                                        teamList.add(new ArrayList(getRb2List().get(index)));
                                        break;
                                    case 2:
                                        teamList.add(new ArrayList(getWr4List().get(index)));
                                        break;
                                    case 3:
                                        teamList.add(new ArrayList(getTe1List().get(index)));
                                        break;
                                    case 4:
                                        teamList.add(new ArrayList(getDef1List().get(index)));
                                        break;
                                }
                            }
                            printTeam(teamList, output);
                            teamList.clear();
                            recordList.clear();
                        }

                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Reader in = new FileReader(fileName);
                        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
                        ArrayList<Integer> recordList = new ArrayList();
                        for (CSVRecord record : records) {
                            for (int j = 0; j < 5; j++) {
                                int index = NumberUtils.toInt(record.get(j));
                                recordList.add(index);
                                switch (j) {
                                    case 0:
                                        teamList.add(new ArrayList(getQb1List().get(index)));
                                        break;
                                    case 1:
                                        teamList.add(new ArrayList(getRb2List().get(index)));
                                        break;
                                    case 2:
                                        teamList.add(new ArrayList(getWr3List().get(index)));
                                        break;
                                    case 3:
                                        teamList.add(new ArrayList(getTe2List().get(index)));
                                        break;
                                    case 4:
                                        teamList.add(new ArrayList(getDef1List().get(index)));
                                        break;
                                }
                            }
                            printTeam(teamList, output);
                            teamList.clear();
                            recordList.clear();
                        }

                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
        }
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
    
    public void printTeam(ArrayList<ArrayList<Integer>> teamList, boolean output) {
        StringBuilder sb = new StringBuilder();
        StringBuilder teamOut = new StringBuilder();
        StringBuilder teamName = new StringBuilder();
        ArrayList<Integer> position = new ArrayList();
        ArrayList<dkPlayer> team = new ArrayList();
        for(int i = 0; i < teamList.size(); i++) {
            switch (i) {
                case 0: 
                    position = teamList.get(i);
                    sb.append("---------------------------------------------\n");
                    sb.append("Team\n");
                    for(int j = 0; j < position.size(); j++) {
                        if ((j == 0) && (j + 1 == position.size())) {
                            teamOut.append("[" + position.get(j) + "],");
                            teamName.append("\"[" + qbList.get(position.get(j)).getName() + "]\",");
                        }
                        else if (j == 0) {
                            teamOut.append("[" + position.get(j) + ",");
                            teamName.append("\"[" + qbList.get(position.get(j)).getName() + ",");
                        }
                        else if (j + 1 == position.size()) {
                            teamOut.append(position.get(j) + "],");
                            teamName.append(qbList.get(position.get(j)).getName() + "]\",");
                        }
                        else {
                            teamOut.append(position.get(j) + ",");
                            teamName.append(qbList.get(position.get(j)).getName() + ",");
                        }
                        sb.append("QB: " + qbList.get(position.get(j)).getName() + " | " + qbList.get(position.get(j)).getTeam() + "\n");
                        team.add(qbList.get(position.get(j)));
                    }
                    break;
                case 1:
                    position = teamList.get(i);
                    for(int j = 0; j < position.size(); j++) {
                        if ((j == 0) && (j + 1 == position.size())) {
                            teamOut.append("[" + position.get(j) + "],");
                            teamName.append("\"[" + rbList.get(position.get(j)).getName() + "]\",");
                        }
                        else if (j == 0) {
                            teamOut.append("[" + position.get(j) + ",");
                            teamName.append("\"[" + rbList.get(position.get(j)).getName() + ",");
                        }
                        else if (j + 1 == position.size()) {
                            teamOut.append(position.get(j) + "],");
                            teamName.append(rbList.get(position.get(j)).getName() + "]\",");
                        }
                        else {
                            teamOut.append(position.get(j) + ",");
                            teamName.append(rbList.get(position.get(j)).getName() + ",");
                        }
                        sb.append("RB: " + rbList.get(position.get(j)).getName() + " | " + rbList.get(position.get(j)).getTeam() + "\n");
                        team.add(rbList.get(position.get(j)));
                    }
                    break;
                case 2:
                    position = teamList.get(i);
                    for(int j = 0; j < position.size(); j++) {
                        if ((j == 0) && (j + 1 == position.size())) {
                            teamOut.append("[" + position.get(j) + "],");
                            teamName.append("\"[" + wrList.get(position.get(j)).getName() + "]\",");
                        }
                        else if (j == 0) {
                            teamOut.append("[" + position.get(j) + ",");
                            teamName.append("\"[" + wrList.get(position.get(j)).getName() + ",");
                        }
                        else if (j + 1 == position.size()) {
                            teamOut.append(position.get(j) + "],");
                            teamName.append(wrList.get(position.get(j)).getName() + "]\",");
                        }
                        else {
                            teamOut.append(position.get(j) + ",");
                            teamName.append(wrList.get(position.get(j)).getName() + ",");
                        }
                        sb.append("WR: " + wrList.get(position.get(j)).getName() + " | " + wrList.get(position.get(j)).getTeam() + "\n");
                        team.add(wrList.get(position.get(j)));
                    }
                    break;
                case 3:
                    position = teamList.get(i);
                    for(int j = 0; j < position.size(); j++) {
                        if ((j == 0) && (j + 1 == position.size())) {
                            teamOut.append("[" + position.get(j) + "],");
                            teamName.append("\"[" + teList.get(position.get(j)).getName() + "]\",");
                        }
                        else if (j == 0) {
                            teamOut.append("[" + position.get(j) + ",");
                            teamName.append("\"[" + teList.get(position.get(j)).getName() + ",");
                        }
                        else if (j + 1 == position.size()) {
                            teamOut.append(position.get(j) + "],");
                            teamName.append(teList.get(position.get(j)).getName() + "]\",");
                        }
                        else {
                            teamOut.append(position.get(j) + ",");
                            teamName.append(teList.get(position.get(j)).getName() + ",");
                        }
                        sb.append("TE: " + teList.get(position.get(j)).getName() + " | " + teList.get(position.get(j)).getTeam() + "\n");
                        team.add(teList.get(position.get(j)));
                    }
                    break;
                case 4:
                    position = teamList.get(i);
                    for(int j = 0; j < position.size(); j++) {
                        if ((j == 0) && (j + 1 == position.size())) {
                            teamOut.append("[" + position.get(j) + "],");
                            teamName.append("\"[" + defList.get(position.get(j)).getName() + "]\",");
                        }
                        else if (j == 0) {
                            teamOut.append("[" + position.get(j) + ",");
                            teamName.append("\"[" + defList.get(position.get(j)).getName() + ",");
                        }
                        else if (j + 1 == position.size()) {
                            teamOut.append(position.get(j) + "],");
                            teamName.append(defList.get(position.get(j)).getName() + "]\",");
                        }
                        else {
                            teamOut.append(position.get(j) + ",");
                            teamName.append(defList.get(position.get(j)).getName() + ",");
                        }
                        sb.append("DE: " + defList.get(position.get(j)).getName() + " | " + defList.get(position.get(j)).getTeam() + "\n");
                        team.add(defList.get(position.get(j)));
                    }
                    sb.append("---------------------------------------------\n");
                    break;
            }
        }
        StringBuilder sb1 = new StringBuilder();
        double totalPoints = 0.0;
        double totalSalary = 0.0;
        for(int i = 0; i < team.size(); i++) {
            if (i + 1 == team.size()) {
                sb1.append(team.get(i).getPosition());
            }
            else {
                sb1.append(team.get(i).getPosition() + ",");
            }
            totalPoints = totalPoints + team.get(i).getDkPoints();
            totalSalary = totalSalary + team.get(i).getSalary();
        }
        teamOut.append(totalPoints + ",");
        teamOut.append(totalSalary + "\n");
        teamName.append(totalPoints + ",");
        teamName.append(totalSalary + "\n");
        sb.append("TeamComp: " + sb1.toString() + "\n");
        sb.append("Team Total Points: " + totalPoints + "\n");
        sb.append("Team Total Salary: " + totalSalary);
        double maxSalary = getMaxSalary();
        if (totalSalary <= getMaxSalary()) {
            int temp = getCountOfTeamsInSalary();
            temp++;
            setCountOfTeamsInSalary(temp);
        }
        if (output) {
            System.out.println(sb.toString());
        }
        sb1.setLength(0);
        sb.setLength(0);
        File file = new File("H:\\temp\\weeoutput.csv");
        try {
            FileUtils.writeStringToFile(file, teamName.toString(), (String)null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        //System.out.println(teamOut.toString());
        teamOut.setLength(0);
        position.clear();
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getCountOfTeamsInSalary() {
        return countOfTeamsInSalary;
    }

    public void setCountOfTeamsInSalary(int countOfTeamsInSalary) {
        this.countOfTeamsInSalary = countOfTeamsInSalary;
    }
    
}
