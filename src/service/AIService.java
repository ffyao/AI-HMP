package service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * AI分析服务类（支持上下文记忆）
 * 使用智谱AI API进行健康数据分析
 */
public class AIService {
    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static final String MODEL = "glm-4-flash";
    private static final int MAX_HISTORY = 20; // 最多保留20条历史消息
    
    private static final String API_KEY_FILE = "data/api_key.dat";
    
    private String apiKey;
    private List<String[]> conversationHistory; // [role, content]
    private String systemPrompt; // 系统提示词
    
    public AIService() {
        this.apiKey = loadApiKey();
        this.conversationHistory = new ArrayList<>();
        this.systemPrompt = "你是一位专业的健康顾问AI助手。你的职责是：\n" +
            "1. 根据用户的健康数据提供专业的分析和建议\n" +
            "2. 回答用户的健康相关问题\n" +
            "3. 给出个性化的运动、饮食建议\n" +
            "4. 用友好、专业的语气回答\n" +
            "5. 记住之前的对话内容，保持连贯性";
    }
    
    public AIService(String apiKey) {
        this();
        this.apiKey = apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        saveApiKey(apiKey);
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public boolean hasApiKey() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }
    
    /**
     * 清空对话历史
     */
    public void clearHistory() {
        conversationHistory.clear();
    }
    
    /**
     * 获取对话历史条数
     */
    public int getHistorySize() {
        return conversationHistory.size();
    }
    
    /**
     * 生成健康分析报告（异步，带上下文）
     */
    public void analyzeHealthData(String healthData, Consumer<String> onSuccess, Consumer<String> onError) {
        // 清空历史，开始新的分析对话
        clearHistory();
        
        String userMessage = "请根据以下用户健康数据，给出详细的健康分析报告。\n\n" +
            "分析要求：\n" +
            "1. 总体健康状况评估\n" +
            "2. BMI分析及建议\n" +
            "3. 运动情况分析及建议\n" +
            "4. 饮食情况分析及建议\n" +
            "5. 需要改善的方面\n" +
            "6. 具体的行动建议\n\n" +
            "用户健康数据：\n" + healthData;
        
        // 添加用户消息到历史
        conversationHistory.add(new String[]{"user", userMessage});
        
        callAPIAsync(onSuccess, onError);
    }
    
    /**
     * 提问（带上下文记忆）
     */
    public void askHealthQuestion(String question, String healthData, 
                                  Consumer<String> onSuccess, Consumer<String> onError) {
        // 如果是第一条消息，先添加健康数据作为上下文
        if (conversationHistory.isEmpty() && healthData != null && !healthData.isEmpty()) {
            conversationHistory.add(new String[]{"user", "以下是我的健康数据，请记住：\n" + healthData});
            conversationHistory.add(new String[]{"assistant", "好的，我已经记住了您的健康数据。请问您有什么健康问题需要咨询？"});
        }
        
        // 添加用户问题到历史
        conversationHistory.add(new String[]{"user", question});
        
        callAPIAsync(onSuccess, onError);
    }
    
    /**
     * 异步调用API（带历史消息）
     */
    private void callAPIAsync(Consumer<String> onSuccess, Consumer<String> onError) {
        new Thread(() -> {
            try {
                String response = callAPI();
                // 提取AI回复并添加到历史
                String aiReply = parseResponse(response);
                conversationHistory.add(new String[]{"assistant", aiReply});
                
                // 限制历史长度
                trimHistory();
                
                onSuccess.accept(response);
            } catch (Exception e) {
                // 调用失败时移除最后一条用户消息
                if (!conversationHistory.isEmpty()) {
                    conversationHistory.remove(conversationHistory.size() - 1);
                }
                onError.accept(e.getMessage());
            }
        }).start();
    }
    
    /**
     * 裁剪历史记录，保持在最大长度内
     */
    private void trimHistory() {
        while (conversationHistory.size() > MAX_HISTORY) {
            // 成对删除（删除最早的用户消息和AI回复）
            conversationHistory.remove(0);
            if (!conversationHistory.isEmpty()) {
                conversationHistory.remove(0);
            }
        }
    }
    
    /**
     * 调用智谱AI API（带历史消息）
     */
    private String callAPI() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);
        
        String requestBody = buildRequestBody();
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            return readResponse(conn.getInputStream());
        } else {
            String errorResponse = readResponse(conn.getErrorStream());
            throw new Exception("API调用失败，状态码: " + responseCode + "\n" + errorResponse);
        }
    }
    
    /**
     * 构建请求体（包含完整对话历史）
     */
    private String buildRequestBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"model\": \"").append(MODEL).append("\",");
        sb.append("\"messages\": [");
        
        // 添加系统提示词
        sb.append("{\"role\": \"system\", \"content\": \"").append(escapeJson(systemPrompt)).append("\"}");
        
        // 添加历史消息
        for (String[] msg : conversationHistory) {
            sb.append(",{\"role\": \"").append(msg[0]).append("\", \"content\": \"").append(escapeJson(msg[1])).append("\"}");
        }
        
        sb.append("],");
        sb.append("\"temperature\": 0.7,");
        sb.append("\"max_tokens\": 2000");
        sb.append("}");
        
        return sb.toString();
    }
    
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    private String readResponse(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
    
    /**
     * 保存API Key到文件
     */
    private void saveApiKey(String key) {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();
            java.nio.file.Files.write(
                new File(API_KEY_FILE).toPath(), 
                key.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            // 静默失败，不影响使用
        }
    }
    
    /**
     * 从文件加载API Key
     */
    private String loadApiKey() {
        try {
            File file = new File(API_KEY_FILE);
            if (file.exists()) {
                String key = new String(
                    java.nio.file.Files.readAllBytes(file.toPath()), 
                    StandardCharsets.UTF_8).trim();
                if (!key.isEmpty()) return key;
            }
        } catch (IOException e) {
            // 静默失败
        }
        return "";
    }
    
    /**
     * 解析API响应
     */
    public static String parseResponse(String response) {
        try {
            int contentStart = response.indexOf("\"content\":");
            if (contentStart == -1) return "无法解析AI响应";
            
            contentStart = response.indexOf("\"", contentStart + 10);
            if (contentStart == -1) return "无法解析AI响应";
            
            int contentEnd = contentStart + 1;
            boolean escaped = false;
            while (contentEnd < response.length()) {
                char c = response.charAt(contentEnd);
                if (escaped) {
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    break;
                }
                contentEnd++;
            }
            
            String content = response.substring(contentStart + 1, contentEnd);
            content = content.replace("\\n", "\n")
                           .replace("\\r", "\r")
                           .replace("\\t", "\t")
                           .replace("\\\"", "\"")
                           .replace("\\\\", "\\");
            
            return content;
        } catch (Exception e) {
            return "解析AI响应失败: " + e.getMessage();
        }
    }
}