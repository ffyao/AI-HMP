package ui;

import service.AIService;
import service.HealthService;

import javax.swing.*;
import java.awt.*;

/**
 * AI健康分析面板
 * 提供AI智能健康分析功能
 */
public class AIPanel extends JPanel {
    private HealthService healthService;
    private AIService aiService;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton analyzeBtn;
    private JButton askBtn;
    private JButton apiKeyBtn;
    private JButton clearBtn;
    private JLabel statusLabel;
    
    public AIPanel(HealthService healthService) {
        this.healthService = healthService;
        this.aiService = new AIService();
        initComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        
        inputField = new JTextField(30);
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        analyzeBtn = new JButton("生成健康报告");
        analyzeBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        askBtn = new JButton("提问");
        askBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        apiKeyBtn = new JButton("设置API Key");
        apiKeyBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        
        clearBtn = new JButton("清空对话");
        clearBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        
        statusLabel = new JLabel(aiService.hasApiKey() ? "API Key已加载" : "请先设置API Key");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusLabel.setForeground(aiService.hasApiKey() ? new Color(76, 175, 80) : Color.GRAY);
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 顶部标题和API设置
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("AI智能健康分析");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel apiPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        apiPanel.add(clearBtn);
        apiPanel.add(apiKeyBtn);
        apiPanel.add(statusLabel);
        topPanel.add(apiPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 中间聊天区域
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        add(scrollPane, BorderLayout.CENTER);
        
        // 底部输入区域
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(analyzeBtn);
        buttonPanel.add(askBtn);
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        
        // 添加提示信息
        JLabel tipLabel = new JLabel("提示：点击\"生成健康报告\"获取AI健康分析，或输入健康问题后点击\"提问\"");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tipLabel.setForeground(Color.GRAY);
        bottomPanel.add(tipLabel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // 添加欢迎信息
        appendMessage("系统", "欢迎使用AI智能健康分析功能！\n\n" +
                "功能说明：\n" +
                "1. 点击\"设置API Key\"配置智谱AI密钥\n" +
                "2. 点击\"生成健康报告\"获取AI健康分析\n" +
                "3. 输入健康问题后点击\"提问\"获取个性化建议\n\n" +
                "请先设置API Key以使用AI功能。\n");
    }
    
    /**
     * 设置事件处理器
     */
    private void setupEventHandlers() {
        // 清空对话按钮
        clearBtn.addActionListener(e -> {
            aiService.clearHistory();
            chatArea.setText("");
            appendMessage("系统", "对话已清空，AI将忘记之前的对话内容。\n\n" +
                "功能说明：\n" +
                "1. 点击\"生成健康报告\"获取AI健康分析\n" +
                "2. 输入健康问题后点击\"提问\"获取个性化建议\n" +
                "3. AI会记住对话内容，支持追问和深入讨论\n\n");
        });
        
        // API Key设置按钮
        apiKeyBtn.addActionListener(e -> showApiKeyDialog());
        
        // 生成健康报告按钮
        analyzeBtn.addActionListener(e -> generateHealthReport());
        
        // 提问按钮
        askBtn.addActionListener(e -> askQuestion());
        
        // 回车发送
        inputField.addActionListener(e -> askQuestion());
    }
    
    /**
     * 显示API Key设置对话框
     */
    private void showApiKeyDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "设置API Key", true);
        dialog.setSize(520, 260);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // 顶部提示
        JPanel tipPanel = new JPanel(new BorderLayout(5, 5));
        JLabel tipLabel = new JLabel("请输入智谱AI的API Key");
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        JLabel linkLabel = new JLabel("获取地址：https://bigmodel.cn/");
        linkLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tipPanel.add(tipLabel, BorderLayout.NORTH);
        tipPanel.add(linkLabel, BorderLayout.SOUTH);
        
        // 中间输入框
        JTextField apiKeyField = new JTextField();
        apiKeyField.setFont(new Font("Consolas", Font.PLAIN, 14));
        apiKeyField.setPreferredSize(new Dimension(450, 35));
        if (aiService.hasApiKey()) {
            apiKeyField.setText(aiService.getApiKey());
        }
        apiKeyField.selectAll();
        
        // 底部按钮
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton cancelBtn = new JButton("取消");
        cancelBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        JButton confirmBtn = new JButton("保存");
        confirmBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        confirmBtn.setPreferredSize(new Dimension(80, 32));
        cancelBtn.setPreferredSize(new Dimension(80, 32));
        btnPanel.add(cancelBtn);
        btnPanel.add(confirmBtn);
        
        mainPanel.add(tipPanel, BorderLayout.NORTH);
        mainPanel.add(apiKeyField, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        confirmBtn.addActionListener(e -> {
            String apiKey = apiKeyField.getText().trim();
            if (!apiKey.isEmpty()) {
                aiService.setApiKey(apiKey);
                statusLabel.setText("API Key已设置");
                statusLabel.setForeground(new Color(76, 175, 80));
                appendMessage("系统", "API Key设置成功！现在可以使用AI分析功能了。\n");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "请输入有效的API Key！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 回车确认
        apiKeyField.addActionListener(e -> confirmBtn.doClick());
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * 生成健康报告
     */
    private void generateHealthReport() {
        if (!checkApiKey()) return;
        
        String healthData = healthService.getHealthDataSummary();
        if (healthData.isEmpty()) {
            appendMessage("系统", "暂无健康数据，请先在\"数据记录\"页面添加健康数据。\n");
            return;
        }
        
        appendMessage("用户", "请帮我分析健康数据并生成报告。\n");
        appendMessage("系统", "正在生成AI健康报告，请稍候...\n");
        
        analyzeBtn.setEnabled(false);
        
        aiService.analyzeHealthData(healthData,
            response -> {
                SwingUtilities.invokeLater(() -> {
                    String aiResponse = AIService.parseResponse(response);
                    appendMessage("AI助手", aiResponse + "\n");
                    analyzeBtn.setEnabled(true);
                });
            },
            error -> {
                SwingUtilities.invokeLater(() -> {
                    appendMessage("系统", "AI分析失败：" + error + "\n");
                    analyzeBtn.setEnabled(true);
                });
            }
        );
    }
    
    /**
     * 提问
     */
    private void askQuestion() {
        if (!checkApiKey()) return;
        
        String question = inputField.getText().trim();
        if (question.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入您的健康问题！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String healthData = healthService.getHealthDataSummary();
        
        appendMessage("用户", question + "\n");
        appendMessage("系统", "AI正在思考，请稍候...\n");
        
        inputField.setText("");
        inputField.setEnabled(false);
        askBtn.setEnabled(false);
        
        aiService.askHealthQuestion(question, healthData,
            response -> {
                SwingUtilities.invokeLater(() -> {
                    String aiResponse = AIService.parseResponse(response);
                    appendMessage("AI助手", aiResponse + "\n");
                    inputField.setEnabled(true);
                    askBtn.setEnabled(true);
                    inputField.requestFocus();
                });
            },
            error -> {
                SwingUtilities.invokeLater(() -> {
                    appendMessage("系统", "AI回答失败：" + error + "\n");
                    inputField.setEnabled(true);
                    askBtn.setEnabled(true);
                    inputField.requestFocus();
                });
            }
        );
    }
    
    /**
     * 检查API Key是否已设置
     */
    private boolean checkApiKey() {
        if (!aiService.hasApiKey()) {
            JOptionPane.showMessageDialog(this, "请先设置API Key！", "提示", JOptionPane.WARNING_MESSAGE);
            showApiKeyDialog();
            return false;
        }
        return true;
    }
    
    /**
     * 添加消息到聊天区域
     */
    private void appendMessage(String sender, String message) {
        chatArea.append("【" + sender + "】" + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}