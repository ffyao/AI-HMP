package util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * 提供文件读写和操作功能
 */
public class FileUtils {
    
    private static final String DATA_DIR = "data";
    
    /**
     * 确保数据目录存在
     */
    public static void ensureDataDirectory() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            System.err.println("创建数据目录失败: " + e.getMessage());
        }
    }
    
    /**
     * 写入字符串到文件
     * @param fileName 文件名
     * @param content 文件内容
     * @return 是否写入成功
     */
    public static boolean writeToFile(String fileName, String content) {
        ensureDataDirectory();
        try {
            Path filePath = Paths.get(DATA_DIR, fileName);
            Files.write(filePath, content.getBytes("UTF-8"));
            return true;
        } catch (IOException e) {
            System.err.println("写入文件失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 从文件读取字符串
     * @param fileName 文件名
     * @return 文件内容，读取失败返回空字符串
     */
    public static String readFromFile(String fileName) {
        try {
            Path filePath = Paths.get(DATA_DIR, fileName);
            if (!Files.exists(filePath)) {
                return "";
            }
            byte[] bytes = Files.readAllBytes(filePath);
            return new String(bytes, "UTF-8");
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * 写入对象到文件（序列化）
     * @param fileName 文件名
     * @param object 要写入的对象
     * @return 是否写入成功
     */
    public static boolean writeObjectToFile(String fileName, Serializable object) {
        ensureDataDirectory();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Paths.get(DATA_DIR, fileName).toFile()))) {
            oos.writeObject(object);
            return true;
        } catch (IOException e) {
            System.err.println("写入对象失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 从文件读取对象（反序列化）
     * @param fileName 文件名
     * @return 读取的对象，读取失败返回null
     */
    public static Object readObjectFromFile(String fileName) {
        try {
            Path filePath = Paths.get(DATA_DIR, fileName);
            if (!Files.exists(filePath)) {
                return null;
            }
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(filePath.toFile()))) {
                return ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("读取对象失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 检查文件是否存在
     * @param fileName 文件名
     * @return 文件是否存在
     */
    public static boolean fileExists(String fileName) {
        Path filePath = Paths.get(DATA_DIR, fileName);
        return Files.exists(filePath);
    }
    
    /**
     * 删除文件
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public static boolean deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(DATA_DIR, fileName);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("删除文件失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取数据目录下的所有文件名
     * @return 文件名列表
     */
    public static List<String> listFiles() {
        List<String> fileList = new ArrayList<>();
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (Files.exists(dataPath)) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(dataPath);
                for (Path path : stream) {
                    if (Files.isRegularFile(path)) {
                        fileList.add(path.getFileName().toString());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("列出文件失败: " + e.getMessage());
        }
        return fileList;
    }
    
    /**
     * 备份文件
     * @param fileName 原文件名
     * @return 是否备份成功
     */
    public static boolean backupFile(String fileName) {
        try {
            Path source = Paths.get(DATA_DIR, fileName);
            if (!Files.exists(source)) {
                return false;
            }
            String backupName = fileName + ".backup." + System.currentTimeMillis();
            Path target = Paths.get(DATA_DIR, backupName);
            Files.copy(source, target);
            return true;
        } catch (IOException e) {
            System.err.println("备份文件失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取文件大小（字节）
     * @param fileName 文件名
     * @return 文件大小，文件不存在返回-1
     */
    public static long getFileSize(String fileName) {
        try {
            Path filePath = Paths.get(DATA_DIR, fileName);
            if (Files.exists(filePath)) {
                return Files.size(filePath);
            }
        } catch (IOException e) {
            System.err.println("获取文件大小失败: " + e.getMessage());
        }
        return -1;
    }
}