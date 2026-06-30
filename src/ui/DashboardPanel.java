package ui;

import model.User;
import service.HealthService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * 仪表盘面板（精简版）
 * 显示健康数据概览和关键指标
 */
public class DashboardPanel extends JPanel {
    private HealthService healthService;
    private JLabel bmiLabel, bmiStatusLabel;
    private JLabel weightLabel;
    private JLabel calorieIntakeLabel, calorieBurnedLabel;
    private JLabel weeklyExerciseLabel;
    
    public DashboardPanel(HealthService healthService) {
        this.healthService = healthService;
        initComponents();
        setupLayout();
        refreshData();
    }
    
    private void initComponents() {
        Font labelFont = new Font("微软雅黑", Font.PLAIN, 14);
        Font valueFont = new Font("微软雅黑", Font.BOLD, 18);
        
        bmiLabel = createValueLabel("--", valueFont);
        bmiStatusLabel = createLabel("请先完善个人信息", labelFont);
        weightLabel = createValueLabel("--", valueFont);
        calorieIntakeLabel = createValueLabel("--", valueFont);
        calorieBurnedLabel = createValueLabel("--", valueFont);
        weeklyExerciseLabel = createValueLabel("--", valueFont);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("健康仪表盘");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        contentPanel.add(createBMIPanel());
        contentPanel.add(createCaloriePanel());
        contentPanel.add(createWeeklyPanel());
        contentPanel.add(createUserInfoPanel());
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createBMIPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("身体指标"));
        
        panel.add(createLabel("当前体重:", new Font("微软雅黑", Font.PLAIN, 14)));
        panel.add(weightLabel);
        panel.add(createLabel("BMI指数:", new Font("微软雅黑", Font.PLAIN, 14)));
        panel.add(bmiLabel);
        panel.add(createLabel("BMI状态:", new Font("微软雅黑", Font.PLAIN, 14)));
        panel.add(bmiStatusLabel);
        
        return panel;
    }
    
    private JPanel createCaloriePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("今日能量"));
        
        panel.add(createLabel("摄入热量:", new Font("微软雅黑", Font.PLAIN, 14)));
        panel.add(calorieIntakeLabel);
        panel.add(createLabel("消耗热量:", new Font("微软雅黑", Font.PLAIN, 14)));
        panel.add(calorieBurnedLabel);
        
        return panel;
    }
    
    private JPanel createWeeklyPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("本周统计"));
        
        panel.add(createLabel("运动时长:", new Font("微软雅黑", Font.PLAIN, 14)));
        panel.add(weeklyExerciseLabel);
        
        return panel;
    }
    
    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("个人信息"));
        
        User user = healthService.getDataManager().getUser();
        Font labelFont = new Font("微软雅黑", Font.PLAIN, 14);
        Font valueFont = new Font("微软雅黑", Font.BOLD, 14);
        
        panel.add(createLabel("姓名:", labelFont));
        panel.add(createValueLabel(user != null ? user.getName() : "--", valueFont));
        panel.add(createLabel("年龄:", labelFont));
        panel.add(createValueLabel(user != null ? user.getAge() + "岁" : "--", valueFont));
        panel.add(createLabel("性别:", labelFont));
        panel.add(createValueLabel(user != null ? user.getGender() : "--", valueFont));
        panel.add(createLabel("身高:", labelFont));
        panel.add(createValueLabel(user != null ? String.format("%.0f cm", user.getHeight()) : "--", valueFont));
        
        return panel;
    }
    
    public void refreshData() {
        double weight = healthService.getDataManager().getLatestWeight();
        weightLabel.setText(weight > 0 ? String.format("%.1f kg", weight) : "--");
        
        double bmi = healthService.calculateBMI(weight);
        bmiLabel.setText(bmi > 0 ? String.format("%.1f", bmi) : "--");
        bmiStatusLabel.setText(bmi > 0 ? healthService.getBMICategory(bmi) : "请先完善个人信息");
        
        double intake = healthService.getTodayCaloriesIntake();
        calorieIntakeLabel.setText(String.format("%.0f kcal", intake));
        
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        double burned = healthService.getDataManager().getTotalCaloriesBurned(weekAgo, LocalDate.now());
        calorieBurnedLabel.setText(String.format("%.0f kcal", burned));
        
        int weeklyExercise = healthService.getWeekExerciseMinutes();
        weeklyExerciseLabel.setText(String.format("%d 分钟", weeklyExercise));
    }
    
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }
    
    private JLabel createValueLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(0, 102, 204));
        return label;
    }
}