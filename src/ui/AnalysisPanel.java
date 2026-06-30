package ui;

import model.*;
import service.HealthService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * 数据图表面板（精简版）
 * 显示历史数据表格和体重趋势图
 */
public class AnalysisPanel extends JPanel {
    private HealthService healthService;
    private JTabbedPane analysisTabs;
    private JTable weightTable, exerciseTable, dietTable;
    private DefaultTableModel weightModel, exerciseModel, dietModel;
    private JPanel chartPanel;
    
    public AnalysisPanel(HealthService healthService) {
        this.healthService = healthService;
        initComponents();
        setupLayout();
        refreshData();
    }
    
    private void initComponents() {
        analysisTabs = new JTabbedPane();
        
        weightModel = new DefaultTableModel(
            new String[]{"日期", "体重(kg)", "备注"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        weightTable = new JTable(weightModel);
        
        exerciseModel = new DefaultTableModel(
            new String[]{"日期", "运动类型", "时长(分钟)", "消耗热量(kcal)", "强度"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        exerciseTable = new JTable(exerciseModel);
        
        dietModel = new DefaultTableModel(
            new String[]{"日期", "餐次", "食物", "热量(kcal)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        dietTable = new JTable(dietModel);
        
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWeightChart(g);
            }
        };
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("数据图表");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // 左侧数据表格
        analysisTabs.addTab("体重记录", new JScrollPane(weightTable));
        analysisTabs.addTab("运动记录", new JScrollPane(exerciseTable));
        analysisTabs.addTab("饮食记录", new JScrollPane(dietTable));
        
        // 右侧图表
        chartPanel.setBorder(BorderFactory.createTitledBorder("体重趋势图"));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            analysisTabs, chartPanel);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.5);
        
        add(splitPane, BorderLayout.CENTER);
        
        // 底部按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton refreshBtn = new JButton("刷新数据");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> refreshData());
        
        JButton deleteBtn = new JButton("删除选中记录");
        deleteBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        deleteBtn.addActionListener(e -> deleteSelectedRecord());
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 体重数据
        weightModel.setRowCount(0);
        List<WeightRecord> weightRecords = healthService.getDataManager().getAllWeightRecords();
        for (WeightRecord record : weightRecords) {
            weightModel.addRow(new Object[]{
                record.getDate().format(formatter),
                record.getWeight(),
                record.getNote()
            });
        }
        
        // 运动数据
        exerciseModel.setRowCount(0);
        List<ExerciseRecord> exerciseRecords = healthService.getDataManager().getAllExerciseRecords();
        for (ExerciseRecord record : exerciseRecords) {
            exerciseModel.addRow(new Object[]{
                record.getDate().format(formatter),
                record.getExerciseType(),
                record.getDuration(),
                String.format("%.0f", record.getCaloriesBurned()),
                record.getIntensity()
            });
        }
        
        // 饮食数据
        dietModel.setRowCount(0);
        List<DietRecord> dietRecords = healthService.getDataManager().getAllDietRecords();
        for (DietRecord record : dietRecords) {
            dietModel.addRow(new Object[]{
                record.getDate().format(formatter),
                record.getMealType(),
                record.getFoodName(),
                String.format("%.0f", record.getCalories())
            });
        }
        
        // 刷新图表
        chartPanel.repaint();
    }
    
    private void deleteSelectedRecord() {
        int selectedIndex = analysisTabs.getSelectedIndex();
        JTable selectedTable = null;
        String tableName = "";
        
        switch (selectedIndex) {
            case 0:
                selectedTable = weightTable;
                tableName = "体重记录";
                break;
            case 1:
                selectedTable = exerciseTable;
                tableName = "运动记录";
                break;
            case 2:
                selectedTable = dietTable;
                tableName = "饮食记录";
                break;
        }
        
        if (selectedTable != null && selectedTable.getSelectedRow() >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "确定要删除这条" + tableName + "吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                int row = selectedTable.getSelectedRow();
                switch (selectedIndex) {
                    case 0:
                        healthService.getDataManager().deleteWeightRecord(row);
                        break;
                    case 1:
                        healthService.getDataManager().deleteExerciseRecord(row);
                        break;
                    case 2:
                        healthService.getDataManager().deleteDietRecord(row);
                        break;
                }
                refreshData();
                JOptionPane.showMessageDialog(this, "记录删除成功！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "请先选择要删除的记录！", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * 绘制体重趋势图
     */
    private void drawWeightChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        List<WeightRecord> records = healthService.getDataManager().getAllWeightRecords();
        if (records.isEmpty()) {
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            g2d.setColor(Color.GRAY);
            String msg = "暂无体重数据";
            int x = (chartPanel.getWidth() - g2d.getFontMetrics().stringWidth(msg)) / 2;
            g2d.drawString(msg, x, chartPanel.getHeight() / 2);
            return;
        }
        
        List<WeightRecord> sortedRecords = new java.util.ArrayList<>(records);
        sortedRecords.sort(Comparator.comparing(WeightRecord::getDate));
        
        int width = chartPanel.getWidth() - 100;
        int height = chartPanel.getHeight() - 100;
        int startX = 70;
        int startY = 50;
        
        double minWeight = sortedRecords.stream().mapToDouble(WeightRecord::getWeight).min().orElse(0);
        double maxWeight = sortedRecords.stream().mapToDouble(WeightRecord::getWeight).max().orElse(100);
        double weightRange = maxWeight - minWeight;
        if (weightRange == 0) weightRange = 10;
        minWeight -= weightRange * 0.1;
        maxWeight += weightRange * 0.1;
        weightRange = maxWeight - minWeight;
        
        // 坐标轴
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(startX, startY, startX, startY + height);
        g2d.drawLine(startX, startY + height, startX + width, startY + height);
        
        // Y轴刻度
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        for (int i = 0; i <= 5; i++) {
            int y = startY + height - (i * height / 5);
            double value = minWeight + (i * weightRange / 5);
            g2d.setColor(Color.GRAY);
            g2d.drawLine(startX - 5, y, startX + width, y);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.format("%.1f", value), startX - 55, y + 5);
        }
        
        // 绘制数据
        if (sortedRecords.size() > 1) {
            g2d.setColor(new Color(0, 102, 204));
            g2d.setStroke(new BasicStroke(2));
            
            java.awt.geom.Point2D[] points = new java.awt.geom.Point2D[sortedRecords.size()];
            for (int i = 0; i < sortedRecords.size(); i++) {
                int x = startX + (i * width / (sortedRecords.size() - 1));
                int y = startY + height - (int) ((sortedRecords.get(i).getWeight() - minWeight) * height / weightRange);
                points[i] = new java.awt.geom.Point2D.Double(x, y);
            }
            
            for (int i = 0; i < points.length - 1; i++) {
                g2d.draw(new java.awt.geom.Line2D.Double(points[i], points[i + 1]));
            }
            
            g2d.setColor(new Color(255, 102, 0));
            for (java.awt.geom.Point2D point : points) {
                g2d.fill(new java.awt.geom.Ellipse2D.Double(point.getX() - 4, point.getY() - 4, 8, 8));
            }
            
            // X轴日期标签
            g2d.setColor(Color.BLACK);
            int labelInterval = Math.max(1, sortedRecords.size() / 6);
            for (int i = 0; i < sortedRecords.size(); i += labelInterval) {
                int x = startX + (i * width / (sortedRecords.size() - 1));
                String dateStr = sortedRecords.get(i).getDate().format(DateTimeFormatter.ofPattern("MM-dd"));
                g2d.drawString(dateStr, x - 15, startY + height + 20);
            }
        }
        
        // 标题
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 14));
        g2d.drawString("体重变化趋势图", startX + width / 2 - 50, 30);
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        g2d.drawString("体重(kg)", 10, startY + height / 2);
    }
}