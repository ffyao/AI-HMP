package util;

import model.User;

/**
 * 健康计算器工具类
 * 提供各种健康指标的计算方法
 */
public class HealthCalculator {
    
    /**
     * 计算BMI（身体质量指数）
     * @param weight 体重，单位：公斤
     * @param height 身高，单位：厘米
     * @return BMI值
     */
    public static double calculateBMI(double weight, double height) {
        if (height <= 0 || weight <= 0) return 0;
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }
    
    /**
     * 获取BMI状态描述
     * @param bmi BMI值
     * @return 状态描述
     */
    public static String getBMIStatus(double bmi) {
        if (bmi < 18.5) {
            return "偏瘦";
        } else if (bmi < 24) {
            return "正常";
        } else if (bmi < 28) {
            return "偏胖";
        } else {
            return "肥胖";
        }
    }
    
    /**
     * 计算基础代谢率（BMR）
     * 使用Mifflin-St Jeor公式
     * @param user 用户信息
     * @param weight 当前体重
     * @return BMR值（卡路里/天）
     */
    public static double calculateBMR(User user, double weight) {
        if (user == null || weight <= 0) return 0;
        
        double height = user.getHeight();
        int age = user.getAge();
        
        if ("男".equals(user.getGender())) {
            return 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            return 10 * weight + 6.25 * height - 5 * age - 161;
        }
    }
    
    /**
     * 计算每日所需卡路里
     * @param bmr 基础代谢率
     * @param activityLevel 活动水平（1.2-1.9）
     * @return 每日所需卡路里
     */
    public static double calculateDailyCalories(double bmr, double activityLevel) {
        return bmr * activityLevel;
    }
    
    /**
     * 估算体脂率
     * 使用BMI估算方法
     * @param bmi BMI值
     * @param age 年龄
     * @param gender 性别
     * @return 体脂率（百分比）
     */
    public static double estimateBodyFat(double bmi, int age, String gender) {
        if (bmi <= 0 || age <= 0) return 0;
        
        if ("男".equals(gender)) {
            return 1.20 * bmi + 0.23 * age - 16.2;
        } else {
            return 1.20 * bmi + 0.23 * age - 5.4;
        }
    }
    
    /**
     * 计算运动消耗的卡路里
     * @param weight 体重（公斤）
     * @param metValue MET值（代谢当量）
     * @param duration 运动时长（分钟）
     * @return 消耗的卡路里
     */
    public static double calculateCaloriesBurned(double weight, double metValue, int duration) {
        // 卡路里 = MET × 体重(kg) × 时间(小时)
        return metValue * weight * (duration / 60.0);
    }
    
    /**
     * 获取常见运动的MET值
     * @param exerciseType 运动类型
     * @return MET值
     */
    public static double getMETValue(String exerciseType) {
        switch (exerciseType.toLowerCase()) {
            case "步行": return 3.5;
            case "慢跑": return 7.0;
            case "跑步": return 9.8;
            case "游泳": return 6.0;
            case "骑自行车": return 6.8;
            case "瑜伽": return 2.5;
            case "健身操": return 6.5;
            case "篮球": return 6.5;
            case "足球": return 7.0;
            case "羽毛球": return 5.5;
            case "乒乓球": return 4.0;
            case "跳绳": return 11.0;
            case "力量训练": return 5.0;
            default: return 4.0; // 默认中等强度
        }
    }
    
    /**
     * 计算健康评分（0-100分）
     * @param bmi BMI值
     * @param sleepHours 睡眠时长
     * @param exerciseMinutes 每周运动分钟数
     * @param heartRate 心率
     * @return 健康评分
     */
    public static int calculateHealthScore(double bmi, double sleepHours, 
                                           int exerciseMinutes, int heartRate) {
        int score = 0;
        
        // BMI评分（30分）
        if (bmi >= 18.5 && bmi < 24) {
            score += 30;
        } else if (bmi >= 24 && bmi < 28) {
            score += 20;
        } else if (bmi >= 16 && bmi < 18.5) {
            score += 15;
        } else {
            score += 5;
        }
        
        // 睡眠评分（25分）
        if (sleepHours >= 7 && sleepHours <= 9) {
            score += 25;
        } else if (sleepHours >= 6 && sleepHours < 7) {
            score += 15;
        } else if (sleepHours > 9 && sleepHours <= 10) {
            score += 15;
        } else {
            score += 5;
        }
        
        // 运动评分（25分）
        if (exerciseMinutes >= 150) { // 符合WHO推荐
            score += 25;
        } else if (exerciseMinutes >= 75) {
            score += 15;
        } else if (exerciseMinutes >= 30) {
            score += 10;
        } else {
            score += 5;
        }
        
        // 心率评分（20分）
        if (heartRate >= 60 && heartRate <= 100) {
            score += 20;
        } else if (heartRate >= 50 && heartRate < 60) {
            score += 15;
        } else if (heartRate > 100 && heartRate <= 120) {
            score += 10;
        } else {
            score += 5;
        }
        
        return score;
    }
    
    /**
     * 获取健康评分等级
     * @param score 健康评分
     * @return 等级描述
     */
    public static String getHealthScoreLevel(int score) {
        if (score >= 90) {
            return "优秀";
        } else if (score >= 80) {
            return "良好";
        } else if (score >= 70) {
            return "一般";
        } else if (score >= 60) {
            return "较差";
        } else {
            return "需要改善";
        }
    }
    
    /**
     * 计算理想体重范围
     * @param height 身高（厘米）
     * @return 理想体重范围数组 [最小值, 最大值]
     */
    public static double[] calculateIdealWeightRange(double height) {
        if (height <= 0) return new double[]{0, 0};
        double heightInMeters = height / 100.0;
        double minWeight = 18.5 * heightInMeters * heightInMeters;
        double maxWeight = 24.0 * heightInMeters * heightInMeters;
        return new double[]{minWeight, maxWeight};
    }
    
    /**
     * 计算腰臀比
     * @param waist 腰围（厘米）
     * @param hip 臀围（厘米）
     * @return 腰臀比
     */
    public static double calculateWHR(double waist, double hip) {
        if (hip <= 0) return 0;
        return waist / hip;
    }
    
    /**
     * 获取腰臀比健康状态
     * @param whr 腰臀比
     * @param gender 性别
     * @return 健康状态
     */
    public static String getWHRStatus(double whr, String gender) {
        if ("男".equals(gender)) {
            if (whr < 0.9) return "正常";
            else if (whr < 1.0) return "偏高";
            else return "过高";
        } else {
            if (whr < 0.8) return "正常";
            else if (whr < 0.85) return "偏高";
            else return "过高";
        }
    }
}