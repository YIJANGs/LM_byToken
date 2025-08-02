package GetToken;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class d {

    public static String[] get(String url) {
        HttpURLConnection connection = null;
        try {
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    responseCode < HttpURLConnection.HTTP_BAD_REQUEST
                            ? connection.getInputStream()
                            : connection.getErrorStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 返回数组格式: [状态码, 响应内容, HTTP状态码]
            return new String[]{
                    "1",
                    response.toString(),
                    String.valueOf(responseCode)
            };

        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"2", e.getMessage(), "0"};
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String[] post(String url, String postData, String[] headers) {
        HttpURLConnection connection = null;
        try {
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 设置请求头
            if (headers != null && headers.length >= 2) {
                for (int i = 0; i < headers.length - 1; i += 2) {
                    if (headers[i] != null && headers[i+1] != null) {
                        connection.setRequestProperty(headers[i], headers[i+1]);
                    }
                }
            }

            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    responseCode < HttpURLConnection.HTTP_BAD_REQUEST
                            ? connection.getInputStream()
                            : connection.getErrorStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 返回数组格式: [状态码, 响应内容, HTTP状态码]
            return new String[]{
                    responseCode == HttpURLConnection.HTTP_OK ? "1" : "0",
                    response.toString(),
                    String.valueOf(responseCode)
            };

        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"2", e.getMessage(), "0"};
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }



    /*
    public static void main(String[] args) {
        // GET请求测试
        String[] getResponse = get("https://jsonplaceholder.typicode.com/posts/1");
        System.out.println("GET Response: " + Arrays.toString(getResponse));

        // POST请求测试
        String[] postResponse = post(
                "https://login.live.com/oauth20_token.srf",
                "{\n" +
                        "            \"client_id\": \"00000000402b5328\",\n" +
                        "            \"code\": code,\n" +
                        "            \"grant_type\": \"authorization_code\",\n" +
                        "            \"redirect_uri\": \"https://login.live.com/oauth20_desktop.srf\"\n" +
                        "        }",
                new String[]{"Content-Type", "application/json"}
        );
        System.out.println("POST Response: " + Arrays.toString(postResponse));
        String accessToken = GetJson(postResponse[1], "error");
        System.out.println(accessToken);
    }
    */
    public static String GetJson(String json, String key) {
        try {
            // 处理可能的键名格式："key":、"key" :、"KEY":、"KEY" : 等
            String[] possiblePatterns = {
                    "\"" + key + "\":",     // "key":
                    "\"" + key + "\" :",    // "key" :
                    "\"" + key.toLowerCase() + "\":",
                    "\"" + key.toLowerCase() + "\" :",
                    "\"" + key.toUpperCase() + "\":",
                    "\"" + key.toUpperCase() + "\" :"
            };

            int keyIndex = -1;
            String foundPattern = null;

            // 检查所有可能的键名格式
            for (String pattern : possiblePatterns) {
                keyIndex = json.indexOf(pattern);
                if (keyIndex != -1) {
                    foundPattern = pattern;
                    break;
                }
            }

            if (keyIndex == -1) return null;

            int valueStart = keyIndex + foundPattern.length();
            // 跳过可能的值前面的空格
            while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                valueStart++;
            }

            if (valueStart >= json.length()) return null;

            char firstChar = json.charAt(valueStart);

            // 处理嵌套对象或数组
            if (firstChar == '{' || firstChar == '[') {
                int braceCount = 1;
                int end = valueStart + 1;
                char openingChar = firstChar;
                char closingChar = (openingChar == '{') ? '}' : ']';

                while (end < json.length() && braceCount > 0) {
                    if (json.charAt(end) == openingChar) braceCount++;
                    if (json.charAt(end) == closingChar) braceCount--;
                    end++;
                }
                return json.substring(valueStart, end);
            }

            if (firstChar == '"') { // 字符串值
                int start = valueStart + 1;
                int end = json.indexOf('"', start);
                return json.substring(start, end);
            } else { // 数字或布尔值
                int end = valueStart;
                while (end < json.length() &&
                        (Character.isLetterOrDigit(json.charAt(end)) ||
                                json.charAt(end) == '.' ||
                                json.charAt(end) == '-' ||
                                json.charAt(end) == '_')) {
                    end++;
                }
                return json.substring(valueStart, end);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}