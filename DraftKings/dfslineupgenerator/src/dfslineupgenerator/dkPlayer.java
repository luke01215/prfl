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
public class dkPlayer {
    
    private int week;
    private int year;
    private int GID;
    private String name;
    private String position;
    private String team;
    private double dkPoints;
    private double salary;
    private boolean enabled;
    private double ppd;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGID() {
        return GID;
    }

    public void setGID(int GID) {
        this.GID = GID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getDkPoints() {
        return dkPoints;
    }

    public void setDkPoints(double dkPoints) {
        this.dkPoints = dkPoints;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getPpd() {
        return ppd;
    }

    public void setPpd() {
        if (this.getSalary() == 0.0) {
            this.ppd = 0.0;
        }
        else {
            this.ppd = getDkPoints() / getSalary();
        }
    }
    
}