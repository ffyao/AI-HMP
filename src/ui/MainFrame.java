package ui;

import service.HealthService;

import javax.swing.*;
import java.awt.*;

/**
 * 主窗口类
 * 应用程序的主界面框架
 */
public class MainFrame extends JFrame {
    private HealthService healthService;
    private JTabbedPane tabbedPane;
    private DashboardPanel dashboardPanel;
    private RecordPanel recordPanel;
    private AnalysisPanel analysisPanel;
    private AIPanel aiPanel;
    
    public MainFrame() {
        healthService = new HealthService();
        initComponents();
        setupLayout();
        setupMenuBar();
        setFrameProperties();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        dashboardPanel = new DashboardPanel(healthService);
        recordPanel = new RecordPanel(healthService);
        analysisPanel = new AnalysisPanel(healthService);
        aiPanel = new AIPanel(healthService);
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 添加标签页
        tabbedPane.addTab("健康仪表盘", dashboardPanel);
        tabbedPane.addTab("数据记录", recordPanel);
        tabbedPane.addTab("数据图表", analysisPanel);
        tabbedPane.addTab("AI健康分析", aiPanel);
        
        // 设置标签页样式
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // 添加状态栏
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("AI健康管理平台 | 当前日期: " + 
            java.time.LocalDate.now().toString());
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 设置菜单栏
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        fileMenu.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        exitItem.addActionListener(e -> {
            healthService.getDataManager().saveAllData();
            System.exit(0);
        });
        fileMenu.add(exitItem);
        
        // 数据菜单
        JMenu dataMenu = new JMenu("数据");
        dataMenu.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JMenuItem refreshItem = new JMenuItem("刷新数据");
        refreshItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshItem.addActionListener(e -> refreshAllPanels());
        dataMenu.add(refreshItem);
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(dataMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * 设置窗口属性
     */
    private void setFrameProperties() {
        setTitle("AI健康管理平台");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 添加窗口关闭事件
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                healthService.getDataManager().saveAllData();
            }
        });
    }
    
    /**
     * 刷新所有面板
     */
    public void refreshAllPanels() {
        dashboardPanel.refreshData();
        analysisPanel.refreshData();
    }
    
    /**
     * 显示关于对话框
     */
    private void showAboutDialog() {
        String message = "AI健康管理平台\n\n" +
                        "功能特点：\n" +
                        "• 健康数据记录与管理\n" +
                        "• BMI等健康指标计算\n" +
                        "• 数据可视化图表展示\n" +
                        "• AI智能健康分析（基于智谱AI）\n\n" +
                        "技术栈：Java Swing + 智谱AI API";
        JOptionPane.showMessageDialog(this, message, "关于", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 检查是否需要初始化用户
     */
    public void checkAndInitUser() {
        if (!healthService.getDataManager().hasUser()) {
            showUserInitDialog();
        }
    }
    
    /**
     * 显示用户初始化对话框
     */
    private void showUserInitDialog() {
        JDialog dialog = new JDialog(this, "初始化用户信息", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        JTextField heightField = new JTextField(20);
        JTextField targetWeightField = new JTextField(20);
        
        int row = 0;
        addFormField(panel, gbc, "姓名:", nameField, row++);
        addFormField(panel, gbc, "年龄:", ageField, row++);
        addFormField(panel, gbc, "性别:", genderCombo, row++);
        addFormField(panel, gbc, "身高(cm):", heightField, row++);
        addFormField(panel, gbc, "目标体重(kg):", targetWeightField, row++);
        
        JButton confirmBtn = new JButton("确认");
        confirmBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = (String) genderCombo.getSelectedItem();
                double height = Double.parseDouble(heightField.getText().trim());
                double targetWeight = Double.parseDouble(targetWeightField.getText().trim());
                
                healthService.initUser(name, age, gender, height, targetWeight);
                refreshAllPanels();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "用户信息初始化成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "输入格式错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(confirmBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
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