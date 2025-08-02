/*
* package GetToken;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

public class ZSmc {
    public static void A(String code) {
        try {
            // 第一个请求 - 获取access token
            // 使用x-www-form-urlencoded格式
            String postData = "client_id=00000000402b5328" +
                    "&code=" + URLEncoder.encode(code, "UTF-8") +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=https://login.live.com/oauth20_desktop.srf";

            String[] R0 = d.post("https://login.live.com/oauth20_token.srf",
                    postData,
                    new String[]{"Content-Type", "application/x-www-form-urlencoded"});

            System.out.print("1 / 8\r");

            String access_token = d.GetJson(R0[1], "access_token");
            String refresh_token = d.GetJson(R0[1], "refresh_token");

            String writeResult = (String) f.fileOperation(".\\MCL\\token.MCL", "write", refresh_token);

            // 第二个请求 - Xbox认证
            String xboxAuthJson = String.format(
                    "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"c.GetToken.d=%s\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}",
                    access_token);

            String[] R1 = d.post("https://user.auth.xboxlive.com/user/authenticate",
                    xboxAuthJson,
                    new String[]{"Content-Type", "application/json", "Accept", "application/json"});

            System.out.print("2 / 8\r");
            String Xbox_token = d.GetJson(R1[1], "Token");
            System.out.print("3 / 8\r");
            String Uhash = d.GetJson(d.GetJson(d.GetJson(R1[1], "DisplayClaims"), "xui"), "uhs");
            System.out.print("4 / 8\r");


            String XTST_data = String.format("{\"Properties\": {\"SandboxId\": \"RETAIL\", \"UserTokens\": [\"%s\"]},\"RelyingParty\": \"rp://api.minecraftservices.com/\",\"TokenType\": \"JWT\"}",Xbox_token);
            String[] R2 = d.post("https://xsts.auth.xboxlive.com/xsts/authorize",
                    XTST_data,
                    new String[]{"Content-Type", "application/json"});
            System.out.print("5 / 8\r");
            String XSTS_token = d.GetJson(R2[1], "Token");
            System.out.print("6 / 8\r");


            String M_data = String.format(
                    "{\"identityToken\":\"XBL3.0 x=%s;%s\"}",
                    Uhash, XSTS_token);
            String[] R3 = d.post("https://api.minecraftservices.com/authentication/login_with_xbox",
                    M_data,
                    new String[]{"Content-Type", "application/json"});
            System.out.print("7 / 8\r");
            String Minecraft_access_token = d.GetJson(R3[1], "access_token");
            System.out.println("8 / 8");
            System.out.println("Minecraft Token: " + Minecraft_access_token);


        } catch (UnsupportedEncodingException e) {
            System.err.print("URL编码错误: " + e.getMessage());
        }
    }

    public static void B(String ReToken) {
        try {
            // 第一个请求 - 获取access token
            // 使用x-www-form-urlencoded格式
            String postData = "client_id=00000000402b5328" +
                    "&refresh_token=" + URLEncoder.encode(ReToken, "UTF-8") +
                    "&grant_type=refresh_token" +
                    "&scope=XboxLive.signin%20offline_access";

            String[] R0 = d.post("https://login.live.com/oauth20_token.srf",
                    postData,
                    new String[]{"Content-Type", "application/x-www-form-urlencoded"});

            System.out.print("1 / 8\r");

            String access_token = d.GetJson(R0[1], "access_token");
            String refresh_token = d.GetJson(R0[1], "refresh_token");

            String writeResult = (String) f.fileOperation(".\\MCL\\token.MCL", "write", refresh_token);

            // 第二个请求 - Xbox认证
            String xboxAuthJson = String.format(
                    "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"c.GetToken.d=%s\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}",
                    access_token);

            String[] R1 = d.post("https://user.auth.xboxlive.com/user/authenticate",
                    xboxAuthJson,
                    new String[]{"Content-Type", "application/json", "Accept", "application/json"});

            System.out.print("2 / 8\r");
            String Xbox_token = d.GetJson(R1[1], "Token");
            System.out.print("3 / 8\r");
            String Uhash = d.GetJson(d.GetJson(d.GetJson(R1[1], "DisplayClaims"), "xui"), "uhs");
            System.out.print("4 / 8\r");


            String XTST_data = String.format("{\"Properties\": {\"SandboxId\": \"RETAIL\", \"UserTokens\": [\"%s\"]},\"RelyingParty\": \"rp://api.minecraftservices.com/\",\"TokenType\": \"JWT\"}",Xbox_token);
            String[] R2 = d.post("https://xsts.auth.xboxlive.com/xsts/authorize",
                    XTST_data,
                    new String[]{"Content-Type", "application/json"});
            System.out.print("5 / 8\r");
            String XSTS_token = d.GetJson(R2[1], "Token");
            System.out.print("6 / 8\r");


            String M_data = String.format(
                    "{\"identityToken\":\"XBL3.0 x=%s;%s\"}",
                    Uhash, XSTS_token);
            String[] R3 = d.post("https://api.minecraftservices.com/authentication/login_with_xbox",
                    M_data,
                    new String[]{"Content-Type", "application/json"});
            System.out.print("7 / 8\r");
            String Minecraft_access_token = d.GetJson(R3[1], "access_token");
            System.out.println("8 / 8");
            System.out.println("Minecraft Token: " + Minecraft_access_token);


        } catch (UnsupportedEncodingException e) {
            System.err.print("URL编码错误: " + e.getMessage());
        }
    }

    public static void C() {
        try {
            Object readResult = f.fileOperation(".\\MCL\\token.MCL", "read");
            if (readResult.equals("N")) {
                System.out.println("请访问以下 URL 获取 code：\n" +
                        "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=00000000402b5328&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=XboxLive.Signin%20offline_access&prompt=select_account");
                String code = c.input("Code: ");
                System.out.println("Starting Minecraft...");
                A(code);
            }else{
                B((String) readResult);
            }
        } catch (Exception e) {
            System.out.println("请访问以下 URL 获取 code：\n" +
                    "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=00000000402b5328&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=XboxLive.Signin%20offline_access&prompt=select_account");
            String code = c.input("Code: ");
            System.out.println("Starting Minecraft...");
            A(code);
        }


    }

    public static void main(String[] args) {
        C();
    }
}
* */

package GetToken;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

public class ZSmc {
    public static void A(String code) {
        try {
            // 第一个请求 - 获取access token
            String postData = "client_id=00000000402b5328" +
                    "&code=" + URLEncoder.encode(code, "UTF-8") +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=https://login.live.com/oauth20_desktop.srf";

            System.out.println("正在获取Microsoft访问令牌...");
            String[] R0 = d.post("https://login.live.com/oauth20_token.srf",
                    postData,
                    new String[]{"Content-Type", "application/x-www-form-urlencoded"});

            // 检查响应是否有效
            if (R0 == null || !"1".equals(R0[0]) || R0[1] == null) {
                System.err.println("获取Microsoft访问令牌失败!");
                System.err.println("HTTP状态码: " + (R0 != null ? R0[2] : "无响应"));
                System.err.println("错误信息: " + (R0 != null ? R0[1] : "请求失败"));
                return;
            }

            String access_token = d.GetJson(R0[1], "access_token");
            String refresh_token = d.GetJson(R0[1], "refresh_token");

            if (access_token == null || refresh_token == null) {
                System.err.println("解析Microsoft令牌失败! 原始响应:");
                System.err.println(R0[1]);
                return;
            }

            System.out.println("1/8 - 成功获取Microsoft访问令牌");

            // 保存refresh token
            String writeResult = (String) f.fileOperation(".\\MCL\\token.MCL", "write", refresh_token);
            if (writeResult.startsWith("Error")) {
                System.err.println("保存refresh token失败: " + writeResult);
            }

            // 第二个请求 - Xbox认证
            System.out.println("2/8 - 正在获取Xbox令牌...");
            String xboxAuthJson = String.format(
                    "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d=%s\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}",
                    access_token);

            String[] R1 = d.post("https://user.auth.xboxlive.com/user/authenticate",
                    xboxAuthJson,
                    new String[]{"Content-Type", "application/json", "Accept", "application/json"});

            if (R1 == null || !"1".equals(R1[0])) {
                System.err.println("获取Xbox令牌失败! 状态码: " + (R1 != null ? R1[2] : "无响应"));
                return;
            }

            String Xbox_token = d.GetJson(R1[1], "Token");
            String Uhash = d.GetJson(d.GetJson(d.GetJson(R1[1], "DisplayClaims"), "xui"), "uhs");

            if (Xbox_token == null || Uhash == null) {
                System.err.println("解析Xbox令牌失败! 原始响应:");
                System.err.println(R1[1]);
                return;
            }

            System.out.println("3/8 - 成功获取Xbox令牌");
            System.out.println("4/8 - 成功获取用户哈希");

            // 获取XSTS令牌
            System.out.println("5/8 - 正在获取XSTS令牌...");
            String XTST_data = String.format("{\"Properties\": {\"SandboxId\": \"RETAIL\", \"UserTokens\": [\"%s\"]},\"RelyingParty\": \"rp://api.minecraftservices.com/\",\"TokenType\": \"JWT\"}", Xbox_token);
            String[] R2 = d.post("https://xsts.auth.xboxlive.com/xsts/authorize",
                    XTST_data,
                    new String[]{"Content-Type", "application/json"});

            if (R2 == null || !"1".equals(R2[0])) {
                System.err.println("获取XSTS令牌失败! 状态码: " + (R2 != null ? R2[2] : "无响应"));
                System.err.println("错误信息: " + (R2 != null ? R2[1] : "请求失败"));
                return;
            }

            String XSTS_token = d.GetJson(R2[1], "Token");
            if (XSTS_token == null) {
                System.err.println("解析XSTS令牌失败! 原始响应:");
                System.err.println(R2[1]);
                return;
            }

            System.out.println("6/8 - 成功获取XSTS令牌");

            // 获取Minecraft令牌
            System.out.println("7/8 - 正在获取Minecraft令牌...");
            String M_data = String.format(
                    "{\"identityToken\":\"XBL3.0 x=%s;%s\"}",
                    Uhash, XSTS_token);
            String[] R3 = d.post("https://api.minecraftservices.com/authentication/login_with_xbox",
                    M_data,
                    new String[]{"Content-Type", "application/json"});

            if (R3 == null || !"1".equals(R3[0])) {
                System.err.println("获取Minecraft令牌失败! 状态码: " + (R3 != null ? R3[2] : "无响应"));
                return;
            }

            String Minecraft_access_token = d.GetJson(R3[1], "access_token");
            if (Minecraft_access_token == null) {
                System.err.println("解析Minecraft令牌失败! 原始响应:");
                System.err.println(R3[1]);
                return;
            }

            System.out.println("8/8 - 成功获取Minecraft令牌");
            System.out.println("\nMinecraft Token: " + Minecraft_access_token);

        } catch (UnsupportedEncodingException e) {
            System.err.println("URL编码错误: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("发生未预期错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // B()方法也需要类似的修改，这里省略...
    public static void B(String ReToken) {
        try {
            // 第一个请求 - 获取access token
            // 使用x-www-form-urlencoded格式
            String postData = "client_id=00000000402b5328" +
                    "&refresh_token=" + URLEncoder.encode(ReToken, "UTF-8") +
                    "&grant_type=refresh_token" +
                    "&scope=XboxLive.signin%20offline_access";

            String[] R0 = d.post("https://login.live.com/oauth20_token.srf",
                    postData,
                    new String[]{"Content-Type", "application/x-www-form-urlencoded"});

            System.out.print("1 / 8\r");

            String access_token = d.GetJson(R0[1], "access_token");
            String refresh_token = d.GetJson(R0[1], "refresh_token");

            String writeResult = (String) f.fileOperation(".\\MCL\\token.MCL", "write", refresh_token);

            // 第二个请求 - Xbox认证
            String xboxAuthJson = String.format(
                    "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"c.GetToken.d=%s\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}",
                    access_token);

            String[] R1 = d.post("https://user.auth.xboxlive.com/user/authenticate",
                    xboxAuthJson,
                    new String[]{"Content-Type", "application/json", "Accept", "application/json"});

            System.out.print("2 / 8\r");
            String Xbox_token = d.GetJson(R1[1], "Token");
            System.out.print("3 / 8\r");
            String Uhash = d.GetJson(d.GetJson(d.GetJson(R1[1], "DisplayClaims"), "xui"), "uhs");
            System.out.print("4 / 8\r");


            String XTST_data = String.format("{\"Properties\": {\"SandboxId\": \"RETAIL\", \"UserTokens\": [\"%s\"]},\"RelyingParty\": \"rp://api.minecraftservices.com/\",\"TokenType\": \"JWT\"}",Xbox_token);
            String[] R2 = d.post("https://xsts.auth.xboxlive.com/xsts/authorize",
                    XTST_data,
                    new String[]{"Content-Type", "application/json"});
            System.out.print("5 / 8\r");
            String XSTS_token = d.GetJson(R2[1], "Token");
            System.out.print("6 / 8\r");


            String M_data = String.format(
                    "{\"identityToken\":\"XBL3.0 x=%s;%s\"}",
                    Uhash, XSTS_token);
            String[] R3 = d.post("https://api.minecraftservices.com/authentication/login_with_xbox",
                    M_data,
                    new String[]{"Content-Type", "application/json"});
            System.out.print("7 / 8\r");
            String Minecraft_access_token = d.GetJson(R3[1], "access_token");
            System.out.println("8 / 8");
            System.out.println("Minecraft Token: " + Minecraft_access_token);


        } catch (UnsupportedEncodingException e) {
            System.err.print("URL编码错误: " + e.getMessage());
        }
    }
    // C()方法可以保持不变
    public static void C() {
        try {
            Object readResult = f.fileOperation(".\\MCL\\token.MCL", "read");
            if (readResult.equals("N")) {
                System.out.println("请访问以下 URL 获取 code：\n" +
                        "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=00000000402b5328&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=XboxLive.Signin%20offline_access&prompt=select_account");
                String code = c.input("Code: ");
                System.out.println("Starting Minecraft...");
                A(code);
            }else{
                B((String) readResult);
            }
        } catch (Exception e) {
            System.out.println("请访问以下 URL 获取 code：\n" +
                    "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=00000000402b5328&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=XboxLive.Signin%20offline_access&prompt=select_account");
            String code = c.input("Code: ");
            System.out.println("Starting Minecraft...");
            A(code);
        }


    }
}