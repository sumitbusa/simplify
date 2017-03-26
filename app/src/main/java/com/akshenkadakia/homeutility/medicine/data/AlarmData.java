package com.akshenkadakia.homeutility.medicine.data;

import java.sql.Time;

public class AlarmData{/*} implements Comparable<AlarmData> {
    @Override
    public int compareTo(AlarmData rhs) {
        return getTime().compareTo(rhs.getTime());
    }
    */
    Time time;

    //Calendar time;
    boolean sunday=true,monday=true,tuesday=true,
            wednesday=true,thursday=true,friday=true,saturday=true;
    int iid,mid,fid,aid;

    public AlarmData(){}

    public AlarmData(Time time, boolean sunday, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, int iid, int mid, int fid, int aid) {
        this.time = time;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.iid = iid;
        this.mid = mid;
        this.fid = fid;
        this.aid = aid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public boolean checkDay(int x){
        boolean b=false;
        switch(x){

                case 0:b=isSunday();
                    break;
                case 1:b=isMonday();
                    break;
                case 2:b=isTuesday();
                    break;
                case 3:b=isWednesday();
                    break;
                case 4:b=isThursday();
                    break;
                case 5:b=isFriday();
                    break;
                case 6:b=isSaturday();
                    break;
            }
            return b;
    }

    @Override
    public String toString() {
        return "AlarmData{" +
                "time=" + time +
                ", sunday=" + sunday +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", iid=" + iid +
                ", mid=" + mid +
                ", fid=" + fid +
                ", aid=" + aid +
                '}';
    }
}
