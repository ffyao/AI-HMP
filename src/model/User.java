package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * 用户实体类
 * 存储用户基本信息和身体参数
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private LocalDate birthDate;
    private String gender; // "男" 或 "女"
    private double height; // 身高，单位：厘米
    private double targetWeight; // 目标体重，单位：公斤
    
    public User() {
    }
    
    public User(String name, LocalDate birthDate, String gender, double height, double targetWeight) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.height = height;
        this.targetWeight = targetWeight;
    }
    
    // 计算年龄
    public int getAge() {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public double getTargetWeight() {
        return targetWeight;
    }
    
    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", targetWeight=" + targetWeight +
                '}';
    }
}