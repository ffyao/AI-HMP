package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 运动记录实体类
 * 记录运动类型、时长和消耗的卡路里
 */
public class ExerciseRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LocalDate date;
    private String exerciseType; // 运动类型
    private int duration; // 运动时长，单位：分钟
    private double caloriesBurned; // 消耗的卡路里
    private LocalTime startTime; // 开始时间
    private String intensity; // 运动强度：低、中、高
    
    public ExerciseRecord() {
    }
    
    public ExerciseRecord(LocalDate date, String exerciseType, int duration, 
                          double caloriesBurned, LocalTime startTime, String intensity) {
        this.date = date;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.startTime = startTime;
        this.intensity = intensity;
    }
    
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getExerciseType() {
        return exerciseType;
    }
    
    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public double getCaloriesBurned() {
        return caloriesBurned;
    }
    
    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public String getIntensity() {
        return intensity;
    }
    
    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }
    
    @Override
    public String toString() {
        return "ExerciseRecord{" +
                "date=" + date +
                ", exerciseType='" + exerciseType + '\'' +
                ", duration=" + duration +
                ", caloriesBurned=" + caloriesBurned +
                ", startTime=" + startTime +
                ", intensity='" + intensity + '\'' +
                '}';
    }
}