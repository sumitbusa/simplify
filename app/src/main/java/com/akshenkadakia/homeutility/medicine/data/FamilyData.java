package com.akshenkadakia.homeutility.medicine.data;

public class FamilyData {
    private String name;
    private boolean notifications;
    private int fid;
    private int  ai;

    public FamilyData(){

    }

    public FamilyData(String name, boolean notifications) {
        this.name = name;
        this.notifications = notifications;
    }

    public FamilyData(String name, boolean notifications, int fid, int ai) {
        this.name = name;
        this.notifications = notifications;
        this.fid = fid;
        this.ai = ai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getAi() {
        return ai;
    }

    public void setAi(int ai) {
        this.ai = ai;
    }

    public int incrementAi(){
        return ++ai;
    }

    @Override
    public String toString() {
        return "FamilyData{" +
                "name='" + name + '\'' +
                ", notifications=" + notifications +
                ", fid=" + fid +
                ", ai=" + ai +
                '}';
    }
}
