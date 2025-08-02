import requests

def retoken(re_token):
    Reurl = "https://login.live.com/oauth20_token.srf"
    headers = {
        "Content-Type": "application/x-www-form-urlencoded",
    }
    cid = "00000000402b5328"
    data1 = {
        "client_id": cid,
        "refresh_token": re_token,
        "grant_type": "refresh_token",
        "scope": "XboxLive.signin offline_access"
    }
    res = requests.post(Reurl, data=data1, headers=headers)
    return res.json()["access_token"], res.json()["refresh_token"]


# 1. 获取微软 OAuth2 Token
def get_microsoft_token(code):
    url = "https://login.live.com/oauth20_token.srf"
    headers = {
    "Content-Type": "application/x-www-form-urlencoded",
    }
    cid = "00000000402b5328"
    data = {
        "client_id": cid,
        "code": code,
        "grant_type": "authorization_code",
        "redirect_uri": "https://login.live.com/oauth20_desktop.srf"
    }
    response = requests.post(url, data=data, headers=headers)
    return response.json()["access_token"], response.json()["refresh_token"]


# 2. Xbox Live 认证
def get_xbox_token(ms_token):
    url = "https://user.auth.xboxlive.com/user/authenticate"
    headers = {"Content-Type": "application/json"}
    data = {
        "Properties": {"AuthMethod": "RPS", "SiteName": "user.auth.xboxlive.com", "RpsTicket": f"d={ms_token}"},
        "RelyingParty": "http://auth.xboxlive.com",
        "TokenType": "JWT"
    }
    response = requests.post(url, headers=headers, json=data)
    return response.json()["Token"], response.json()["DisplayClaims"]["xui"][0]["uhs"]

# 3. 获取 XSTS Token
def get_xsts_token(xbox_token):
    url = "https://xsts.auth.xboxlive.com/xsts/authorize"
    headers = {"Content-Type": "application/json"}
    data = {
        "Properties": {"SandboxId": "RETAIL", "UserTokens": [xbox_token]},
        "RelyingParty": "rp://api.minecraftservices.com/",
        "TokenType": "JWT"
    }
    response = requests.post(url, headers=headers, json=data)
    return response.json()["Token"]

# 4. Minecraft 认证
def get_minecraft_token(xsts_token, userhash):
    url = "https://api.minecraftservices.com/authentication/login_with_xbox"
    headers = {"Content-Type": "application/json"}
    data = {"identityToken": f"XBL3.0 x={userhash};{xsts_token}"}
    response = requests.post(url, headers=headers, json=data)
    return response.json()["access_token"]

# 5. 检查游戏所有权
def check_ownership(mc_token):
    url = "https://api.minecraftservices.com/entitlements/mcstore"
    headers = {"Authorization": f"Bearer {mc_token}"}
    response = requests.get(url, headers=headers)
    return response.json()

# 6. 获取玩家 Profile
def get_player_profile(mc_token):
    url = "https://api.minecraftservices.com/minecraft/profile"
    headers = {"Authorization": f"Bearer {mc_token}"}
    response = requests.get(url, headers=headers)
    return response.json()

def start(ms_token):

    # 3. Xbox Live 认证
    xbox_token, userhash = get_xbox_token(ms_token)
    xsts_token = get_xsts_token(xbox_token)

    # 4. Minecraft 认证
    mc_token = get_minecraft_token(xsts_token, userhash)

    print("Minecraft Token:", mc_token)