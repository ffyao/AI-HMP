package ui;

import service.HealthService;

import javax.swing.*;
import java.awt.*;

/**
 * 数据记录面板（精简版）
 * 用于记录体重、运动、饮食数据
 */
public class RecordPanel extends JPanel {
    private HealthService healthService;
    private JTabbedPane recordTabs;
    
    public RecordPanel(HealthService healthService) {
        this.healthService = healthService;
        initComponents();
        setupLayout();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        recordTabs = new JTabbedPane();
        recordTabs.addTab("体重记录", createWeightPanel());
        recordTabs.addTab("运动记录", createExercisePanel());
        recordTabs.addTab("饮食记录", createDietPanel());
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("数据记录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        add(recordTabs, BorderLayout.CENTER);
    }
    
    /**
     * 创建体重记录面板
     */
    private JPanel createWeightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField weightField = new JTextField(20);
        
        int row = 0;
        addField(panel, gbc, "体重(kg):", weightField, row++);
        
        JButton saveBtn = new JButton("保存记录");
        saveBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        saveBtn.addActionListener(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                if (healthService.addWeightRecord(weight)) {
                    JOptionPane.showMessageDialog(this, "体重记录保存成功！");
                    weightField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "请输入有效的体重数值（0-300kg）！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的体重数值！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveBtn, gbc);
        
        return panel;
    }
    
    /**
     * 创建运动记录面板
     */
    private JPanel createExercisePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        String[] exerciseTypes = {"跑步", "游泳", "骑行", "步行", "瑜伽", "健身", "跳绳", "篮球", "羽毛球"};
        JComboBox<String> exerciseCombo = new JComboBox<>(exerciseTypes);
        JTextField durationField = new JTextField(20);
        JLabel caloriesLabel = new JLabel("预计消耗: 0 kcal");
        caloriesLabel.setFont(new Font("微软雅黑", Font.ITALIC, 12));
        caloriesLabel.setForeground(Color.GRAY);
        
        // 自动计算卡路里
        exerciseCombo.addActionListener(e -> updateCaloriesPreview(exerciseCombo, durationField, caloriesLabel));
        durationField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                updateCaloriesPreview(exerciseCombo, durationField, caloriesLabel);
            }
        });
        
        int row = 0;
        addField(panel, gbc, "运动类型:", exerciseCombo, row++);
        addField(panel, gbc, "运动时长(分钟):", durationField, row++);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(caloriesLabel, gbc);
        row++;
        
        JButton saveBtn = new JButton("保存记录");
        saveBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        saveBtn.addActionListener(e -> {
            try {
                String exerciseType = (String) exerciseCombo.getSelectedItem();
                int duration = Integer.parseInt(durationField.getText().trim());
                double calories = healthService.estimateCaloriesBurned(exerciseType, duration);
                
                if (healthService.addExerciseRecord(exerciseType, duration, calories)) {
                    JOptionPane.showMessageDialog(this, 
                        String.format("运动记录保存成功！\n预计消耗: %.0f kcal", calories));
                    durationField.setText("");
                    caloriesLabel.setText("预计消耗: 0 kcal");
                } else {
                    JOptionPane.showMessageDialog(this, "请输入有效的运动时长！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的运动时长！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveBtn, gbc);
        
        return panel;
    }
    
    /**
     * 更新卡路里预览
     */
    private void updateCaloriesPreview(JComboBox<String> exerciseCombo, 
                                       JTextField durationField, JLabel caloriesLabel) {
        try {
            String exerciseType = (String) exerciseCombo.getSelectedItem();
            int duration = Integer.parseInt(durationField.getText().trim());
            double calories = healthService.estimateCaloriesBurned(exerciseType, duration);
            caloriesLabel.setText(String.format("预计消耗: %.0f kcal", calories));
        } catch (NumberFormatException e) {
            caloriesLabel.setText("预计消耗: 0 kcal");
        }
    }
    
    /**
     * 创建饮食记录面板
     */
    private JPanel createDietPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        String[] mealTypes = {"早餐", "午餐", "晚餐", "加餐"};
        JComboBox<String> mealCombo = new JComboBox<>(mealTypes);
        JTextField foodField = new JTextField(20);
        JTextField caloriesField = new JTextField(20);
        
        // 常见食物热量参考
        JLabel tipLabel = new JLabel("<html><i>常见食物热量参考：米饭116kcal/100g、苹果52kcal/100g、鸡蛋144kcal/100g</i></html>");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        tipLabel.setForeground(Color.GRAY);
        
        int row = 0;
        addField(panel, gbc, "餐次:", mealCombo, row++);
        addField(panel, gbc, "食物名称:", foodField, row++);
        addField(panel, gbc, "热量(kcal):", caloriesField, row++);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(tipLabel, gbc);
        row++;
        
        JButton saveBtn = new JButton("保存记录");
        saveBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        saveBtn.addActionListener(e -> {
            try {
                String mealType = (String) mealCombo.getSelectedItem();
                String foodName = foodField.getText().trim();
                double calories = Double.parseDouble(caloriesField.getText().trim());
                
                if (healthService.addDietRecord(mealType, foodName, calories)) {
                    JOptionPane.showMessageDialog(this, "饮食记录保存成功！");
                    foodField.setText("");
                    caloriesField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "请输入有效的食物名称和热量！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的热量数值！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveBtn, gbc);
        
        return panel;
    }
    
    /**
     * 添加表单字段
     */
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(field, gbc);
    }
}