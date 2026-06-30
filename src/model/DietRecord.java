package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 饮食记录实体类
 * 简化版：只记录食物和热量
 */
public class DietRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LocalDate date;
    private String mealType; // 餐次：早餐、午餐、晚餐、加餐
    private String foodName; // 食物名称
    private double calories; // 卡路里
    private LocalTime mealTime; // 用餐时间
    
    public DietRecord() {
    }
    
    public DietRecord(LocalDate date, String mealType, String foodName, 
                      double calories, LocalTime mealTime) {
        this.date = date;
        this.mealType = mealType;
        this.foodName = foodName;
        this.calories = calories;
        this.mealTime = mealTime;
    }
    
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getMealType() {
        return mealType;
    }
    
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    
    public String getFoodName() {
        return foodName;
    }
    
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    
    public double getCalories() {
        return calories;
    }
    
    public void setCalories(double calories) {
        this.calories = calories;
    }
    
    public LocalTime getMealTime() {
        return mealTime;
    }
    
    public void setMealTime(LocalTime mealTime) {
        this.mealTime = mealTime;
    }
    
    @Override
    public String toString() {
        return "DietRecord{" +
                "date=" + date +
                ", mealType='" + mealType + '\'' +
                ", foodName='" + foodName + '\'' +
                ", calories=" + calories +
                ", mealTime=" + mealTime +
                '}';
    }
}