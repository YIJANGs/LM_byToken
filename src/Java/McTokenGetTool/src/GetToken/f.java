package GetToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class f {

    /**
     * 文件读写操作
     * @param filePath 文件路径
     * @param mode 模式："read" 读取文件内容，"write" 覆写文件内容
     * @param content 写入内容（mode为"write"时使用）
     * @return 读取模式下返回文件内容，写入模式下返回操作结果
     */
    public static Object fileOperation(String filePath, String mode, String... content) {
        try {
            if ("read".equalsIgnoreCase(mode)) {
                // 读取文件内容
                return readFile(filePath);
            } else if ("write".equalsIgnoreCase(mode)) {
                // 覆写文件内容
                if (content.length == 0) {
                    return "Error: No content provided for writing";
                }
                return writeFile(filePath, content[0]);
            } else {
                return "Error: Invalid mode. Use 'read' or 'write'";
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    // 读取文件内容
    private static String readFile(String filePath) throws IOException {
        // 使用Java 8+的Files类读取所有行
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return String.join(System.lineSeparator(), lines);

        /* 或者使用传统方式：
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
        */
    }

    // 覆写文件内容
    private static String writeFile(String filePath, String content) throws IOException {
        // 使用Java 8+的Files类写入
        Files.write(Paths.get(filePath), content.getBytes());
        return "File written successfully";

        /* 或者使用传统方式：
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
        }
        return "File written successfully";
        */
    }

    // 使用示例
    public static void main(String[] args) {
        // 读取文件示例
        String filePath = "test.txt";
        Object readResult = fileOperation(filePath, "read");
        System.out.println("File content:\n" + readResult);

        // 写入文件示例
        String writeResult = (String) fileOperation(filePath, "write", "This is new content");
        System.out.println(writeResult);

        // 验证写入结果
        System.out.println("Updated file content:");
        System.out.println(fileOperation(filePath, "read"));
    }
}
