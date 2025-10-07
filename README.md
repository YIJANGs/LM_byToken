# LM_byToken (Launch Minecraft by Token / 通过Token启动正版Minecraft)
此工具可以获取您的Minecraft正版登录Token并启动Minecraft。<br>
## 什么是 Minecraft 访问令牌？
Minecraft 正版验证Token用于验证您是否为正版验证。

例如，除了官方的 Minecraft 启动器启动 Minecraft Java 版本外，您还可以通过命令行启动游戏。

在命令行启动参数中，minecraft 验证令牌就是其中之一。

也就是说，您可以在**没有**启动器的情况下启动**正版**Minecraft。

## 如何使用这个工具？
- 下载仓库的zip压缩包并解压。
### 配置`tokens.json`
在根目录下的`tokens.json`文件中输入基本内容，以下是格式参考：
```json
{
    "user": {
        "name": "<您的Minecraft档案名>",
        "uuid": "<您的Minecraft UUID>"
    },
    "acc_token": "<您的登录token>",
    "ref_token": "<您的刷新token>",
    "gameVersion": "<您要启动的游戏版本，如1.20.4>",
    "Dirs": {
        "gameDir": "<您的Minecraft .jar文件所在的目录>",
        "assetsDir": "<您的assets文件夹所在的目录>",
        "javaDir": "<您用于启动Minecraft的java.exe文件所在的目录>"
    }
}
```
- 其中，`acc_token`参数为您的Minecraft登录token而非Microsoft登录token；`ref_token`参数为您的Microsoft登录token，若您不知道您的登录token，可以直接留空。
- `gameVersion`参数为您要启动的Minecraft版本（名称），这一参数为您的Minecraft`.jar`文件的名称，不包含".jar"。
- `Dirs`参数中为此工具需要（或启动Minecraft需要）的文件路径，以下是获取方法及示例。

**gameDir**
> 在您的Minecraft游戏目录下（或第三方启动器如PCL、HMCL根目录下），找到`.minecraft`文件夹，并打开`.minecraft/versions`文件夹，
> 即您现在所在的目录中存在字符串".minecraft/versions"，在`versions`文件夹下，若您下载过任意版本的Minecraft，您会在这个文件夹下
> 看到一些文件夹，这些文件夹的名称就是您所下的游戏版本。
> 接下来，请打开您希望启动的Minecraft版本对应的文件夹，复制文件夹路径，并粘贴至`gameDir`的参数中。

**gameVersion**
> **在您打开的版本文件夹下，有一个`.jar`扩展名的文件，复制文件名（不包含".jar"），粘贴至上一步描述的`gameVersion`参数中。**

**assetsDir**
> 返回至`.minecraft`文件夹，不出意外的话，您会看到一个名为`assets`文件夹（其与`versions`在同一目录下），请直接复制您当前所在的目录路径，
>并粘贴至`tokens.json`的`assetsDir`参数中。

**javaDir**
> 本参数为启动Minecraft所使用的`java.exe`文件所在目录（路径不包含"java.exe"），若您未手动下载过java，可以在第三方启动器（如PCL、HMCL）
> 的设置中找到`java.exe`文件所在的目录，复制它至`tokens.json`的`javaDir`参数中。

不出意外的话，您现在的`tokens.json`内容应该大致为以下内容：
```json
{
    "user": {
        "name": "YIJANG",
        "uuid": "a86efb5f12654efdb5fc94a6ce98d62d"
    },
    "acc_token": "",
    "ref_token": "",
    "gameVersion": "1.21.1",
    "Dirs": {
        "gameDir": "E:/Minecraft/.minecraft/versions/1.21.1",
        "assetsDir": "E:/Minecraft/.minecraft",
        "javaDir": "E:/java22/bin"
    }
}
```

### 启动工具
使用python启动根目录下的`main.py`文件，如果您之前的token留空，工具会请求登录，打开网址并登录您的Microsoft账号，之后页面会跳转至一个**空白页面**（注意，这不是网络问题，而是正常情况），查看现在浏览器显示的网址，它应该是以下格式：
```
https://login.live.com/oauth20_desktop.srf?code=......&lc=2052
或
https://login.live.com/oauth20_desktop.srf?code=...
```
请将"code="后的内容（或"code="后、"&lc="前的内容）复制到命令行，不出意外的话，工具会启动Minecraft，并使用您刚刚登录的Microsoft账号登录Minecraft。






