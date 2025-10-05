# LM_byToken (Launch Minecraft by Token / 通过Token启动正版Minecraft)
此工具可以获取您的Minecraft正版登录Token并启动Minecraft。
🟥Tip: 本工具暂未转型，即仍停留在只能获取您的我的世界正版登录Token的版本，不能启动Minecraft。
## 什么是 Minecraft 访问令牌？
Minecraft 正版验证Token用于验证您是否为正版验证。
<br>
例如，除了官方的 Minecraft 启动器启动 Minecraft Java 版本外，您还可以通过 `java -jar` 启动游戏。
<br>
在 `java -jar` 启动参数中，minecraft 验证令牌就是其中之一。
<br>
也就是说，您可以在**没有**启动器的情况下启动**正版**Minecraft。
## 我的世界是怎样进行正版验证的？
在2020年后，mojang账号被迁移至微软，您以此用微软账号进行正版登录。
### **1. 微软 OAuth2 认证**
#### **(1) 获取授权码**
首先，需要用户访问一个微软的URL，进行微软账号登录：
```
https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=00000000402b5328&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=XboxLive.Signin%20offline_access&prompt=select_account
```
**请求参数**：
| 参数名 | 说明 | 示例值 |
|--------|------|--------|
| `client_id` | OAuth 客户端 ID | 无法提供示例值 |
| `response_type` | 固定为 `code` | `code` |
| `redirect_uri` | 回调地址 | `https://login.live.com/oauth20_desktop.srf` |
| `scope` | 请求的权限范围 | `XboxLive.signin offline_access` |
| `prompt` | 登录行为（如 `select_account` 或 `login`） | `select_account` |

接下来，用户会跳转至一个新的URL，并会在URL中包含code参数，有效期10分钟。启动器会获取code参数，并完成后续操作。
在本工具中，用户输入的code即是此参数。

---

### **(2) 换取 Access Token**

**请求 URL**：
```
POST https://login.microsoftonline.com/consumers/oauth2/v2.0/token
```
**请求参数（HTTP Body, x-www-form-urlencoded）**：
| 参数名 | 说明 | 示例值 |
|--------|------|--------|
| `client_id` | OAuth 客户端 ID | 无法提供示例值 |
| `code` | 上一步获取的 `code` | `M.R3_BAY.123456...` |
| `grant_type` | 固定为 `authorization_code` | `authorization_code` |
| `redirect_uri` | 必须与上一步一致 | `https://login.live.com/oauth20_desktop.srf` |

**返回字段（JSON）**：
```json
{
  "token_type": "Bearer",
  "expires_in": 3600,
  "access_token": "eyJ0eXAiOi...",
  "refresh_token": "OAQABAAAAA..."
}
```
其中，"access_token"参数为微软登录token，有效期1小时；
<br>
"refresh_token"参数为刷新token，用于刷新"access_token"，有效期90天。

---

### **(1) 获取 Xbox Live Token**
**请求 URL**：
```
POST https://user.auth.xboxlive.com/user/authenticate
```
**请求头（Headers）**：
- `Content-Type: application/json`
- `Accept: application/json`

**请求体（JSON）**：
```json
{
  "Properties": {
    "AuthMethod": "RPS",
    "SiteName": "user.auth.xboxlive.com",
    "RpsTicket": "d=<access_token>"  
  },
  "RelyingParty": "http://auth.xboxlive.com",
  "TokenType": "JWT"
}
```
其中，"<access_token>"为上一步获取的微软登录token。

**返回字段（JSON）**：
```json
{
  "Token": "xbox_live_token",  
  "DisplayClaims": { "xui": [{ "uhs": "userhash" }] }
}
```
其中，"token"参数为xbox登录token，有效期24小时；
"uhs"参数为用户哈希值。

---

### **(2) 获取 XSTS Token（用于 Minecraft 登录）**
**请求 URL**：
```
POST https://xsts.auth.xboxlive.com/xsts/authorize
```
**请求体（JSON）**：
```json
{
  "Properties": {
    "SandboxId": "RETAIL",
    "UserTokens": ["xbox_live_token"] 
  },
  "RelyingParty": "rp://api.minecraftservices.com/",
  "TokenType": "JWT"
}
```
**返回字段（JSON）**：
```json
{
  "Token": "xsts_token", 
  "DisplayClaims": { "xui": [{ "uhs": "userhash" }] }
}
```
参数格式与上一步相同，token有效期24小时。

---

### **(1) 用 XSTS Token 换取 Minecraft Token**
**请求 URL**：
```
POST https://api.minecraftservices.com/authentication/login_with_xbox
```
**请求体（JSON）**：
```json
{
  "identityToken": "XBL3.0 x=<userhash>;<xsts_token>"
}
```
**返回字段（JSON）**：
```json
{
  "username": "PlayerUUID",
  "access_token": "minecraft_access_token",
  "token_type": "Bearer",
  "expires_in": 86400
}
```
其中，"access_token"为我的世界正版验证token，有效期24小时。
<br>
因本工具仅获取我的世界登录token且下两步非启动器的工作，下面两步不作解释。

---

### **(2) 检查游戏所有权（可选，但正版必须）**
**请求 URL**：
```
GET https://api.minecraftservices.com/entitlements/mcstore
```
**请求头**：
- `Authorization: Bearer <minecraft_access_token>`

**返回字段（JSON）**：
```json
{
  "items": [{
    "name": "game_minecraft",
    "signature": "..."
  }]
}
```

---

### **(3) 获取玩家 Profile（UUID 和皮肤）**
**请求 URL**：
```
GET https://api.minecraftservices.com/minecraft/profile
```
**请求头**：
- `Authorization: Bearer <minecraft_access_token>`

**返回字段（JSON）**：
```json
{
  "id": "玩家UUID",
  "name": "游戏ID",
  "skins": [{
    "id": "皮肤ID",
    "url": "https://textures.minecraft.net/texture/..."
  }]
}
```


## 如何使用这个工具？
### 如何获取token
下载仓库中的McTokenGetTool.zip文件并解压，运行McTokenGetTool.jar文件或main.py文件。然后，运行文件会给你一个网址，打开网址，登录你的Microsoft，然后你会跳转到另一个网址：https://......?code=...&...
然后，在运行文件中输入URL中的code参数。
不出意外的话，等待约6秒，运行文件会输出以下格式：
```
Minecraft Token: ......
```
这个就是您的我的世界正版验证token。
### 如何使用token
以第三方启动器PCL为例，当你完成一次启动后，通常会在根目录下的PCL文件夹生成一个LatestLaunch.bat文件，通常情况下，它的内容应该是这样（以YIJANG的为例）：
```
chcp 65001>nul
@echo off
title 启动 - 1.21.1
echo 游戏正在启动，请稍候。
cd /D "E:\PCL\.minecraft\versions\1.21.1\"


java -Dfile.encoding=COMPAT ...(一堆因Minecraft Jar文件位置而不同的在本条说明中不重要的东西)... --username YIJANG --version 1.21.1 --gameDir "E:\PCL\.minecraft\versions\1.21.1" --assetsDir "E:\PCL\.minecraft\assets" --assetIndex 17 --uuid a86efb5f12654efdb5fc94a6ce98d62d --accessToken eyJraFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFnJQoA --clientId ${clientid} --xuid ${auth_xuid} --userType msa --width 1234 --height 687 
echo 游戏已退出。
pause
```
在java启动命令中，`--username`参数为用户名，`--uuid`为用户的uuid，--accessToken为用户的我的世界正版验证token。
<br>
正常情况下，用户无法直接查看正版验证token，因为其中间被"F"替换。因此，直接启动此文件不会进行正版登录，即便您可以打开游戏。
<br>
如果您将上一步获取的我的世界正版验证token替换掉，并启动此.bat文件，您可以发现，您成功正版登录了！

## 我是否可以怀疑这个工具存在后门？
当然可以！这是您的权利。
<br>
不过，我仍由衷地希望您检查仓库的`src\Java\McTokenGetTool\src`文件夹下的Java代码和`src\Python`文件夹下的python代码，
<br>
正常情况下，您不会发现您的任何token被传递到不明的URL。

