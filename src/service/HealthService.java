package service;

import dao.DataManager;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 健康服务类（精简版）
 * 提供核心健康数据管理功能
 */
public class HealthService {
    private DataManager dataManager;
    
    public HealthService() {
        this.dataManager = DataManager.getInstance();
    }
    
    public DataManager getDataManager() {
        return dataManager;
    }
    
    // ==================== 用户管理 ====================
    
    public boolean initUser(String name, int age, String gender, 
                           double height, double targetWeight) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // 用年龄估算出生日期
        LocalDate birthDate = LocalDate.now().minusYears(age);
        User user = new User(name.trim(), birthDate, gender, height, targetWeight);
        dataManager.setUser(user);
        return true;
    }
    
    // ==================== 体重记录 ====================
    
    public boolean addWeightRecord(double weight) {
        if (weight <= 0 || weight > 300) {
            return false;
        }
        WeightRecord record = new WeightRecord(LocalDate.now(), weight, "");
        dataManager.addWeightRecord(record);
        return true;
    }
    
    public boolean deleteWeightRecord(int index) {
        List<WeightRecord> records = dataManager.getAllWeightRecords();
        if (index >= 0 && index < records.size()) {
            dataManager.deleteWeightRecord(index);
            return true;
        }
        return false;
    }
    
    public List<WeightRecord> getAllWeightRecords() {
        return dataManager.getAllWeightRecords();
    }
    
    public double getCurrentWeight() {
        return dataManager.getLatestWeight();
    }
    
    public double calculateBMI(double weight) {
        User user = dataManager.getUser();
        if (user == null || user.getHeight() <= 0) {
            return 0;
        }
        double heightInM = user.getHeight() / 100.0;
        return weight / (heightInM * heightInM);
    }
    
    public String getBMICategory(double bmi) {
        if (bmi < 18.5) return "偏瘦";
        if (bmi < 24) return "正常";
        if (bmi < 28) return "偏胖";
        return "肥胖";
    }
    
    // ==================== 运动记录 ====================
    
    public boolean addExerciseRecord(String exerciseType, int duration, 
                                    double caloriesBurned) {
        if (exerciseType == null || exerciseType.trim().isEmpty() || duration <= 0) {
            return false;
        }
        ExerciseRecord record = new ExerciseRecord(
            LocalDate.now(), exerciseType.trim(), duration, caloriesBurned, 
            LocalTime.now(), "中");
        dataManager.addExerciseRecord(record);
        return true;
    }
    
    public double estimateCaloriesBurned(String exerciseType, int duration) {
        double weight = dataManager.getLatestWeight();
        if (weight <= 0) weight = 60;
        
        double caloriesPerMinute;
        switch (exerciseType) {
            case "跑步": caloriesPerMinute = 0.06 * weight; break;
            case "游泳": caloriesPerMinute = 0.07 * weight; break;
            case "骑行": caloriesPerMinute = 0.05 * weight; break;
            case "步行": caloriesPerMinute = 0.035 * weight; break;
            case "瑜伽": caloriesPerMinute = 0.03 * weight; break;
            case "健身": caloriesPerMinute = 0.05 * weight; break;
            case "跳绳": caloriesPerMinute = 0.08 * weight; break;
            case "篮球": caloriesPerMinute = 0.06 * weight; break;
            case "羽毛球": caloriesPerMinute = 0.045 * weight; break;
            default: caloriesPerMinute = 0.05 * weight;
        }
        return caloriesPerMinute * duration;
    }
    
    public boolean deleteExerciseRecord(int index) {
        List<ExerciseRecord> records = dataManager.getAllExerciseRecords();
        if (index >= 0 && index < records.size()) {
            dataManager.deleteExerciseRecord(index);
            return true;
        }
        return false;
    }
    
    public List<ExerciseRecord> getAllExerciseRecords() {
        return dataManager.getAllExerciseRecords();
    }
    
    // ==================== 饮食记录 ====================
    
    public boolean addDietRecord(String mealType, String foodName, double calories) {
        if (foodName == null || foodName.trim().isEmpty() || calories <= 0) {
            return false;
        }
        DietRecord record = new DietRecord(
            LocalDate.now(), mealType, foodName.trim(), calories, LocalTime.now());
        dataManager.addDietRecord(record);
        return true;
    }
    
    public boolean deleteDietRecord(int index) {
        List<DietRecord> records = dataManager.getAllDietRecords();
        if (index >= 0 && index < records.size()) {
            dataManager.deleteDietRecord(index);
            return true;
        }
        return false;
    }
    
    public List<DietRecord> getTodayDietRecords() {
        return dataManager.getDietRecordsByDate(LocalDate.now());
    }
    
    public double getTodayCaloriesIntake() {
        return dataManager.getTotalCaloriesIntake(LocalDate.now());
    }
    
    // ==================== 数据统计 ====================
    
    public int getWeekExerciseMinutes() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        return dataManager.getTotalExerciseMinutes(weekAgo, LocalDate.now());
    }
    
    public double getWeekCaloriesBurned() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        return dataManager.getTotalCaloriesBurned(weekAgo, LocalDate.now());
    }
    
    public String getHealthDataSummary() {
        return dataManager.getHealthDataSummary();
    }
}