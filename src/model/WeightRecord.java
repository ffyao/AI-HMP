package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 体重记录实体类
 * 记录每日体重数据
 */
public class WeightRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LocalDate date;
    private double weight; // 体重，单位：公斤
    private String note; // 备注
    
    public WeightRecord() {
    }
    
    public WeightRecord(LocalDate date, double weight, String note) {
        this.date = date;
        this.weight = weight;
        this.note = note;
    }
    
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    @Override
    public String toString() {
        return "WeightRecord{" +
                "date=" + date +
                ", weight=" + weight +
                ", note='" + note + '\'' +
                '}';
    }
}