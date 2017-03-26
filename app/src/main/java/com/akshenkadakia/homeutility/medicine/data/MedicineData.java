package com.akshenkadakia.homeutility.medicine.data;

public class MedicineData{/*} implements Comparable<MedicineData>{

    @Override
    public int compareTo(MedicineData another) {
        return this.getName().compareTo(another.getName());
    }*/

    private int fid;
    private int mid;
    private int ai=-1;
    private String name;
    private int packets,tablets,left,total;

    private String color,shape;

    public MedicineData() {
    }

    public MedicineData(String name, int packets, int tablets) {
        this.name = name;
        this.packets = packets;
        this.tablets = tablets;
    }

    public MedicineData(int fid, int mid, int ai, String name, int packets, int tablets, int left, int total, String color, String shape) {
        this.fid = fid;
        this.mid = mid;
        this.ai = ai;
        this.name = name;
        this.packets = packets;
        this.tablets = tablets;
        this.left = left;
        this.total = total;
        this.color = color;
        this.shape = shape;
    }
    public int incrementAi(){
        return ++ai;
    }


    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getAi() {
        return ai;
    }

    public void setAi(int ai) {
        this.ai = ai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPackets() {
        return packets;
    }

    public void setPackets(int packets) {
        this.packets = packets;
    }

    public int getTablets() {
        return tablets;
    }

    public void setTablets(int tablets) {
        this.tablets = tablets;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    @Override
    public String toString() {
        return "MedicineData{" +
                "fid=" + fid +
                ", mid=" + mid +
                ", ai=" + ai +
                ", name='" + name + '\'' +
                ", packets=" + packets +
                ", tablets=" + tablets +
                ", left=" + left +
                ", total=" + total +
                ", color='" + color + '\'' +
                ", shape='" + shape + '\'' +
                '}';
    }
}
