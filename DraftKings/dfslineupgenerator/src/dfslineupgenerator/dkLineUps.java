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
public class dkLineUps {
    
    private int maxSalary;
    private dkPlayer QB = new dkPlayer();
    private dkPlayer RB1 = new dkPlayer();
    private dkPlayer RB2 = new dkPlayer();
    private dkPlayer WR1 = new dkPlayer();
    private dkPlayer WR2 = new dkPlayer();
    private dkPlayer WR3 = new dkPlayer();
    private dkPlayer TE = new dkPlayer();
    private dkPlayer FLEX = new dkPlayer();
    private dkPlayer DST = new dkPlayer();

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public dkPlayer getQB() {
        return QB;
    }

    public void setQB(dkPlayer QB) {
        this.QB = QB;
    }

    public dkPlayer getRB1() {
        return RB1;
    }

    public void setRB1(dkPlayer RB1) {
        this.RB1 = RB1;
    }

    public dkPlayer getRB2() {
        return RB2;
    }

    public void setRB2(dkPlayer RB2) {
        this.RB2 = RB2;
    }

    public dkPlayer getWR1() {
        return WR1;
    }

    public void setWR1(dkPlayer WR1) {
        this.WR1 = WR1;
    }

    public dkPlayer getWR2() {
        return WR2;
    }

    public void setWR2(dkPlayer WR2) {
        this.WR2 = WR2;
    }

    public dkPlayer getWR3() {
        return WR3;
    }

    public void setWR3(dkPlayer WR3) {
        this.WR3 = WR3;
    }

    public dkPlayer getTE() {
        return TE;
    }

    public void setTE(dkPlayer TE) {
        this.TE = TE;
    }

    public dkPlayer getFLEX() {
        return FLEX;
    }

    public void setFLEX(dkPlayer FLEX) {
        this.FLEX = FLEX;
    }

    public dkPlayer getDST() {
        return DST;
    }

    public void setDST(dkPlayer DST) {
        this.DST = DST;
    }
    
}
