package com.akshenkadakia.homeutility.medicine.data;

public class DayData {
    AlarmData alarmData;
    String member;
    String medicine;

    public DayData() {
    }

    public DayData(AlarmData alarmData, String member, String medicine) {
        this.alarmData = alarmData;
        this.member = member;
        this.medicine = medicine;
    }

    public AlarmData getAlarmData() {
        return alarmData;
    }

    public void setAlarmData(AlarmData alarmData) {
        this.alarmData = alarmData;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
}
