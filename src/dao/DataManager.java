package dao;

import model.*;
import util.FileUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据管理器（精简版）
 * 管理用户、体重、运动、饮食数据
 */
public class DataManager {
    private static DataManager instance;
    
    private User user;
    private List<WeightRecord> weightRecords;
    private List<ExerciseRecord> exerciseRecords;
    private List<DietRecord> dietRecords;
    
    private static final String USER_FILE = "user.dat";
    private static final String WEIGHT_FILE = "weight_records.dat";
    private static final String EXERCISE_FILE = "exercise_records.dat";
    private static final String DIET_FILE = "diet_records.dat";
    
    private DataManager() {
        weightRecords = new ArrayList<>();
        exerciseRecords = new ArrayList<>();
        dietRecords = new ArrayList<>();
        loadAllData();
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    @SuppressWarnings("unchecked")
    private void loadAllData() {
        Object userObj = FileUtils.readObjectFromFile(USER_FILE);
        if (userObj instanceof User) {
            user = (User) userObj;
        }
        
        Object weightObj = FileUtils.readObjectFromFile(WEIGHT_FILE);
        if (weightObj instanceof List) {
            weightRecords = (List<WeightRecord>) weightObj;
        }
        
        Object exerciseObj = FileUtils.readObjectFromFile(EXERCISE_FILE);
        if (exerciseObj instanceof List) {
            exerciseRecords = (List<ExerciseRecord>) exerciseObj;
        }
        
        Object dietObj = FileUtils.readObjectFromFile(DIET_FILE);
        if (dietObj instanceof List) {
            dietRecords = (List<DietRecord>) dietObj;
        }
    }
    
    public void saveAllData() {
        if (user != null) {
            FileUtils.writeObjectToFile(USER_FILE, user);
        }
        FileUtils.writeObjectToFile(WEIGHT_FILE, new ArrayList<>(weightRecords));
        FileUtils.writeObjectToFile(EXERCISE_FILE, new ArrayList<>(exerciseRecords));
        FileUtils.writeObjectToFile(DIET_FILE, new ArrayList<>(dietRecords));
    }
    
    // ==================== 用户相关 ====================
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
        FileUtils.writeObjectToFile(USER_FILE, user);
    }
    
    public boolean hasUser() {
        return user != null;
    }
    
    // ==================== 体重记录 ====================
    
    public void addWeightRecord(WeightRecord record) {
        weightRecords.add(record);
        FileUtils.writeObjectToFile(WEIGHT_FILE, new ArrayList<>(weightRecords));
    }
    
    public void deleteWeightRecord(int index) {
        if (index >= 0 && index < weightRecords.size()) {
            weightRecords.remove(index);
            FileUtils.writeObjectToFile(WEIGHT_FILE, new ArrayList<>(weightRecords));
        }
    }
    
    public List<WeightRecord> getAllWeightRecords() {
        return new ArrayList<>(weightRecords);
    }
    
    public List<WeightRecord> getWeightRecordsByDateRange(LocalDate start, LocalDate end) {
        return weightRecords.stream()
                .filter(r -> !r.getDate().isBefore(start) && !r.getDate().isAfter(end))
                .collect(Collectors.toList());
    }
    
    public WeightRecord getLatestWeightRecord() {
        if (weightRecords.isEmpty()) return null;
        return weightRecords.stream()
                .max((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
                .orElse(null);
    }
    
    public double getLatestWeight() {
        WeightRecord latest = getLatestWeightRecord();
        return latest != null ? latest.getWeight() : 0;
    }
    
    // ==================== 运动记录 ====================
    
    public void addExerciseRecord(ExerciseRecord record) {
        exerciseRecords.add(record);
        FileUtils.writeObjectToFile(EXERCISE_FILE, new ArrayList<>(exerciseRecords));
    }
    
    public void deleteExerciseRecord(int index) {
        if (index >= 0 && index < exerciseRecords.size()) {
            exerciseRecords.remove(index);
            FileUtils.writeObjectToFile(EXERCISE_FILE, new ArrayList<>(exerciseRecords));
        }
    }
    
    public List<ExerciseRecord> getAllExerciseRecords() {
        return new ArrayList<>(exerciseRecords);
    }
    
    public List<ExerciseRecord> getExerciseRecordsByDateRange(LocalDate start, LocalDate end) {
        return exerciseRecords.stream()
                .filter(r -> !r.getDate().isBefore(start) && !r.getDate().isAfter(end))
                .collect(Collectors.toList());
    }
    
    public int getTotalExerciseMinutes(LocalDate start, LocalDate end) {
        return getExerciseRecordsByDateRange(start, end).stream()
                .mapToInt(ExerciseRecord::getDuration)
                .sum();
    }
    
    public double getTotalCaloriesBurned(LocalDate start, LocalDate end) {
        return getExerciseRecordsByDateRange(start, end).stream()
                .mapToDouble(ExerciseRecord::getCaloriesBurned)
                .sum();
    }
    
    // ==================== 饮食记录 ====================
    
    public void addDietRecord(DietRecord record) {
        dietRecords.add(record);
        FileUtils.writeObjectToFile(DIET_FILE, new ArrayList<>(dietRecords));
    }
    
    public void deleteDietRecord(int index) {
        if (index >= 0 && index < dietRecords.size()) {
            dietRecords.remove(index);
            FileUtils.writeObjectToFile(DIET_FILE, new ArrayList<>(dietRecords));
        }
    }
    
    public List<DietRecord> getAllDietRecords() {
        return new ArrayList<>(dietRecords);
    }
    
    public List<DietRecord> getDietRecordsByDate(LocalDate date) {
        return dietRecords.stream()
                .filter(r -> r.getDate().equals(date))
                .collect(Collectors.toList());
    }
    
    public double getTotalCaloriesIntake(LocalDate date) {
        return getDietRecordsByDate(date).stream()
                .mapToDouble(DietRecord::getCalories)
                .sum();
    }
    
    // ==================== 数据汇总（供AI分析使用）====================
    
    /**
     * 获取用户健康数据摘要，用于AI分析
     */
    public String getHealthDataSummary() {
        StringBuilder sb = new StringBuilder();
        
        // 用户信息
        if (user != null) {
            sb.append("【用户信息】\n");
            sb.append("姓名: ").append(user.getName()).append("\n");
            sb.append("年龄: ").append(user.getAge()).append("岁\n");
            sb.append("性别: ").append(user.getGender()).append("\n");
            sb.append("身高: ").append(user.getHeight()).append("cm\n");
            sb.append("目标体重: ").append(user.getTargetWeight()).append("kg\n\n");
        }
        
        // 最近体重
        double latestWeight = getLatestWeight();
        if (latestWeight > 0) {
            sb.append("【当前体重】").append(String.format("%.1f", latestWeight)).append("kg\n");
            if (user != null) {
                double heightInM = user.getHeight() / 100.0;
                double bmi = latestWeight / (heightInM * heightInM);
                sb.append("【BMI指数】").append(String.format("%.1f", bmi)).append("\n");
            }
        }
        
        // 最近7天运动
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        int weekExercise = getTotalExerciseMinutes(weekAgo, LocalDate.now());
        double weekCaloriesBurned = getTotalCaloriesBurned(weekAgo, LocalDate.now());
        sb.append("\n【近7天运动】\n");
        sb.append("运动时长: ").append(weekExercise).append("分钟\n");
        sb.append("消耗热量: ").append(String.format("%.0f", weekCaloriesBurned)).append("kcal\n");
        
        // 今日饮食
        double todayCalories = getTotalCaloriesIntake(LocalDate.now());
        sb.append("\n【今日饮食】\n");
        sb.append("摄入热量: ").append(String.format("%.0f", todayCalories)).append("kcal\n");
        
        // 体重变化趋势
        List<WeightRecord> recentWeights = getWeightRecordsByDateRange(weekAgo, LocalDate.now());
        if (recentWeights.size() >= 2) {
            double firstWeight = recentWeights.get(0).getWeight();
            double lastWeight = recentWeights.get(recentWeights.size() - 1).getWeight();
            double change = lastWeight - firstWeight;
            sb.append("\n【近7天体重变化】");
            sb.append(String.format("%.1f", change)).append("kg");
            sb.append(change > 0 ? "(增加)" : change < 0 ? "(减少)" : "(持平)");
        }
        
        return sb.toString();
    }
}