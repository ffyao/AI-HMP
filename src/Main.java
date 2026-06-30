import ui.MainFrame;
import javax.swing.*;

/**
 * AI健康管理平台 - 主程序入口
 * 
 * 功能特点：
 * 1. 健康数据记录与管理（体重、运动、饮食、睡眠、血压心率）
 * 2. BMI、体脂率、基础代谢率等指标计算
 * 3. 数据可视化图表展示（趋势图、柱状图、饼图）
 * 4. 智能健康建议生成
 * 5. 数据统计与分析
 * 
 * 技术栈：Java Swing + 文件存储
 * 
 * @author AI Health Management Platform
 * @version 1.0
 */
public class Main {
    
    public static void main(String[] args) {
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 设置中文字体
        setUIFont(new javax.swing.plaf.FontUIResource("微软雅黑", javax.swing.plaf.FontUIResource.PLAIN, 14));
        
        // 在事件调度线程中创建和显示GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.checkAndInitUser();
        });
    }
    
    /**
     * 设置全局UI字体
     */
    private static void setUIFont(javax.swing.plaf.FontUIResource font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}