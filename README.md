# AI健康管理平台

基于 Java Swing 的智能个人健康管理应用，集成智谱AI（GLM-4-Flash）实现健康数据分析与智能问答

## 功能概览

| 模块 | 功能 |
|------|------|
| 健康仪表盘 | 展示体重、BMI、热量摄入/消耗、本周运动时长等关键指标 |
| 数据记录 | 记录体重、运动、饮食数据 |
| 数据图表 | 体重历史表格、运动记录表、饮食记录表、体重趋势折线图 |
| AI健康分析 | 一键生成健康报告、智能问答 |

## 技术栈

- **GUI**：Java Swing + Java2D 绘图
- **AI**：智谱AI API（GLM-4-Flash 模型），通过 HTTP 调用
- **存储**：Java 对象序列化（`.dat` 文件）
- **架构**：MVC 分层架构

## 项目结构

```
src/
├── Main.java                    # 程序入口
├── model/                       # 数据实体
│   ├── User.java                #   用户信息
│   ├── WeightRecord.java        #   体重记录
│   ├── ExerciseRecord.java      #   运动记录
│   └── DietRecord.java          #   饮食记录
├── util/                        # 工具类
│   ├── HealthCalculator.java    #   健康指标计算（BMI/BMR/体脂率等）
│   ├── DateUtils.java           #   日期工具
│   └── FileUtils.java           #   文件读写工具
├── dao/                         # 数据访问层
│   └── DataManager.java         #   数据管理器（单例模式）
├── service/                     # 业务逻辑层
│   ├── HealthService.java       #   健康数据服务
│   └── AIService.java           #   AI分析服务（调用智谱API）
└── ui/                          # 用户界面
    ├── MainFrame.java           #   主窗口
    ├── DashboardPanel.java      #   健康仪表盘
    ├── RecordPanel.java         #   数据记录面板
    ├── AnalysisPanel.java       #   数据图表面板
    └── AIPanel.java             #   AI健康分析面板
```

## 运行方式

### 环境要求

- JDK 8 及以上版本
- Windows 操作系统
- 网络连接

### 方式一：使用脚本（推荐）

```bash
# 首次运行：编译 + 启动
compile_and_run.bat

# 之后运行：直接启动
启动程序.bat
```

### 方式二：命令行

```bash
# 编译
javac -encoding UTF-8 -d bin src/model/*.java src/util/*.java src/dao/*.java src/service/*.java src/ui/*.java src/Main.java

# 运行
java -cp bin -Dfile.encoding=UTF-8 Main
```

## AI功能使用

1. 注册智谱AI账号：https://bigmodel.cn/
2. 获取 API Key
3. 程序中切换到"AI健康分析"标签页
4. 点击"设置API Key"输入密钥
5. 点击"生成健康报告"获取AI分析
6. 在输入框输入健康问题，获取个性化建议

## 架构设计

```
──────────────────────────────────────────
              UI 层 (Swing)               
  MainFrame ─┬─ DashboardPanel            
             ├─ RecordPanel               
             ├─ AnalysisPanel             
             └─ AIPanel                   
──────────────────────────────────────────
           Service 层 (业务逻辑)          
  HealthService        AIService          
──────────────────────────────────────────
           DAO 层 (数据持久化)             
  DataManager (单例模式)                  
──────────────────────────────────────────
           Model 层 (数据实体)             
  User / WeightRecord / ExerciseRecord    
  / DietRecord                            
──────────────────────────────────────────
           Util 层 (工具类)               
  HealthCalculator / DateUtils            
  / FileUtils                             
──────────────────────────────────────────
```

## 代码统计

- 源文件：16 个 Java 文件
- 代码量：约 2800 行
