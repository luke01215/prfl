/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfslineupgenerator;
import com.google.common.math.Stats;
import java.util.*;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author luke0
 */
public class dkPosition {
    
    private ArrayList<dkPlayer> positionList = new ArrayList();
    private double averagePoints;
    private double stdPoints;
    private double stdOnePoints;
    private double stdTwoPoints;
    private double avgPPD;
    private double stdPPD;
    private double stdOnePPD;
    private double stdTwoPPD;
    private ArrayList<dkPlayer> aboveAverageList = new ArrayList();
    private ArrayList<dkPlayer> oneSTDaboveAverageList = new ArrayList();
    private ArrayList<dkPlayer> twoSTDaboveAverageList = new ArrayList();
    private ArrayList<Integer> possibleTeamCount = new ArrayList();

    public ArrayList<dkPlayer> getPositionList() {
        return positionList;
    }

    public void setPositionList(ArrayList<dkPlayer> positionList) {
        dkPlayer player = new dkPlayer();
        ArrayList<Double> pointList = new ArrayList();
        ArrayList<Double> ppdList = new ArrayList();
        this.positionList = positionList;
        
        for(int i = 0; i < this.positionList.size(); i++) {
            player = this.positionList.get(i);
            pointList.add(player.getDkPoints());
            ppdList.add(player.getPpd());
        }
        
        setAveragePoints(Stats.meanOf(pointList));
        StandardDeviation sd = new StandardDeviation(false);
        double[] pointsArray = new double[pointList.size()];
        for (int i = 0; i < pointsArray.length; i++) {
            pointsArray[i] = pointList.get(i);
        }
        setStdPoints(sd.evaluate(pointsArray));
        setStdOnePoints(getAveragePoints() + getStdPoints());
        setStdTwoPoints(getAveragePoints() + getStdPoints() + getStdPoints());
        
        setAvgPPD(Stats.meanOf(ppdList));
        sd = new StandardDeviation(false);
        double[] ppdArray = new double[ppdList.size()];
        for (int i = 0; i < ppdArray.length; i++) {
            ppdArray[i] = ppdList.get(i);
        }
        setStdPPD(sd.evaluate(ppdArray));
        setStdOnePPD(getAvgPPD() + getStdPPD());
        setStdTwoPP(getAvgPPD() + getStdPPD() + getStdPPD());
        
        setEnabled(0);
        addEnabledToList(getAboveAverageList());
        setEnabled(1);
        addEnabledToList(getOneSTDaboveAverageList());
        setEnabled(2);
        addEnabledToList(getTwoSTDaboveAverageList());
    }

    public double getAveragePoints() {
        return averagePoints;
    }

    public void setAveragePoints(double averagePoints) {
        this.averagePoints = averagePoints;
    }

    public double getStdOnePoints() {
        return stdOnePoints;
    }

    public void setStdOnePoints(double stdOnePoints) {
        this.stdOnePoints = stdOnePoints;
    }

    public double getStdTwoPoints() {
        return stdTwoPoints;
    }

    public void setStdTwoPoints(double stdTwoPoints) {
        this.stdTwoPoints = stdTwoPoints;
    }

    public double getAvgPPD() {
        return avgPPD;
    }

    public void setAvgPPD(double avgPPD) {
        this.avgPPD = avgPPD;
    }

    public double getStdOnePPD() {
        return stdOnePPD;
    }

    public void setStdOnePPD(double stdOnePPD) {
        this.stdOnePPD = stdOnePPD;
    }

    public double getStdTwoPPD() {
        return stdTwoPPD;
    }

    public void setStdTwoPP(double stdTwoPPD) {
        this.stdTwoPPD = stdTwoPPD;
    }

    public double getStdPoints() {
        return stdPoints;
    }

    public void setStdPoints(double stdPoints) {
        this.stdPoints = stdPoints;
    }

    public double getStdPPD() {
        return stdPPD;
    }

    public void setStdPPD(double stdPPD) {
        this.stdPPD = stdPPD;
    }
    
    public void disableAllPlayers() {
        for(int i = 0; i < getPositionList().size(); i++) {
            getPositionList().get(i).setEnabled(false);
        }
    }
    
    public void enableAllPlayers() {
        for(int i = 0; i < getPositionList().size(); i++) {
            getPositionList().get(i).setEnabled(true);
        }
    }
    
    public void setEnabled(int stdDev) {
        disableAllPlayers();
        switch (stdDev) {
            case 0:
                for (int i = 0; i < getPositionList().size(); i++) {
                    if ((getPositionList().get(i).getDkPoints() >= getAveragePoints()) || (getPositionList().get(i).getPpd() >= getAvgPPD())) {
                        getPositionList().get(i).setEnabled(true);
                    }
                }
                break;
            case 1:
                for (int i = 0; i < getPositionList().size(); i++) {
                    if ((getPositionList().get(i).getDkPoints() >= getStdOnePoints()) || (getPositionList().get(i).getPpd() >= getStdOnePPD())) {
                        getPositionList().get(i).setEnabled(true);
                    }
                }
                break;
            case 2:
                for (int i = 0; i < getPositionList().size(); i++) {
                        if ((getPositionList().get(i).getDkPoints() >= getStdTwoPoints()) || (getPositionList().get(i).getPpd() >= getStdTwoPPD())) {
                        getPositionList().get(i).setEnabled(true);
                    }
                }
                break;
            default:
                System.out.println("Please select an appropriate measure [0,1,2]");
        }
    }
    
    public int enabledCount() {
        int result = 0;
        for(int i = 0; i < getPositionList().size(); i++) {
            if (getPositionList().get(i).isEnabled()) {
                result++;
            }
        }
        return result;
    }
    
    public int positionCount() {
        return getPositionList().size();
    }
    
    public double getPercentageOfPopulation(int count) {
        return ((double)count / (double) positionCount()) * 100;
    }
    
    public void addEnabledToList(ArrayList<dkPlayer> playerList) {
        for (int i = 0; i < getPositionList().size(); i++) {
            if(getPositionList().get(i).isEnabled()) {
                playerList.add(getPositionList().get(i));
            }
        }
    }

    public ArrayList<dkPlayer> getAboveAverageList() {
        return aboveAverageList;
    }

    public void setAboveAverageList(ArrayList<dkPlayer> aboveAverageList) {
        this.aboveAverageList = aboveAverageList;
    }

    public ArrayList<dkPlayer> getOneSTDaboveAverageList() {
        return oneSTDaboveAverageList;
    }

    public void setOneSTDaboveAverageList(ArrayList<dkPlayer> oneSTDaboveAverageList) {
        this.oneSTDaboveAverageList = oneSTDaboveAverageList;
    }

    public ArrayList<dkPlayer> getTwoSTDaboveAverageList() {
        return twoSTDaboveAverageList;
    }

    public void setTwoSTDaboveAverageList(ArrayList<dkPlayer> twoSTDaboveAverageList) {
        this.twoSTDaboveAverageList = twoSTDaboveAverageList;
    }

    public ArrayList<Integer> getPossibleTeamCount() {
        return possibleTeamCount;
    }

    public void setPossibleTeamCount(ArrayList<Integer> possibleTeamCount) {
        this.possibleTeamCount = possibleTeamCount;
    }
}
