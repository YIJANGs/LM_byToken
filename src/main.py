import Gtoken
import json
import launch


def savetokens(tokens_data, token_file="./tokens.json"):
    '''
    tokens_w = open(token_file, "w")
    json.dump(tokens_data, tokens_w)
    tokens_w.close()
    '''
    with open(token_file, 'w', encoding='utf-8') as f:
        json.dump(tokens_data, f, indent=4, ensure_ascii=False)

def gettoken(options):
    from urllib.parse import urlparse, parse_qs
    print("请访问以下 URL 获取 code：")
    print("https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=00000000402b5328&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=XboxLive.Signin%20offline_access&prompt=select_account")
    url = input("请输入跳转后的网址: ")
    p_url = urlparse(url)
    qp = parse_qs(p_url.query)
    code = qp["code"][0]
    A, B = Gtoken.get_microsoft_token(code)
    A = Gtoken.start(A)
    if options['gettoken_thenEchotoken']:
        print("Access Token: " + A)
        print("Refresh Token: " + B)
    return A, B

def launchMinecraft(dir, acc_token, username, uuid, options):
    launcher = launch.Launch(dir, acc_token, username, uuid, options)
    launcher.launchMinecraft()

def checkDir(t):
    if t["Dirs"]["versionDir"] == '' or t["Dirs"]["assetsDir"] == '' or t["Dirs"]["javaDir"] == '':
        return False
    return True

def refreshToken(t, options):
    acc_token, ref_token = Gtoken.refreshtoken(t["ref_token"])
    acc_token = Gtoken.start(acc_token)
    t['acc_token'] = acc_token
    t['ref_token'] = ref_token
    if options['gettoken_thenEchotoken']:
        print("Access Token: " + t['acc_token'])
        print("Refresh Token: " + t['ref_token'])
    if options['gettoken_thenSaveToken']:
        savetokens(t)
    return t

def checkTokens(t, options):
    if t['ref_token'] != "":
        if t['acc_token'] != '':
            if options['always_refreshToken']:
                t = refreshToken(t, options)
            return t
        else:
            t = refreshToken(t, options)
            return t
    else:
        if t["acc_token"] != '':
            return t
        else:
            acc_token, ref_token = gettoken(options)
            t['acc_token'] = acc_token
            t['ref_token'] = ref_token
            
            if options['gettoken_thenSaveToken']:
                savetokens(t)
            return t

def getTokensJson():
    tokens = open("./tokens.json", "r")
    tokens_data = json.load(tokens)
    tokens.close()
    return tokens_data

def getOptionsJson():
    options = open("./options.json", "r")
    options_data = json.load(options)
    options.close()
    return options_data

def checkUser(t):
    if not (t["user"]["name"] and t["user"]["uuid"]):
        return False
    return True


if __name__ == "__main__":
    
    tokens_data = getTokensJson()
    options_data = getOptionsJson()

    if not checkUser(tokens_data):
        print("No Username and Useruuid found.")
        exit(2)

    if not checkDir(tokens_data):
        print("Game Directory not set. Please set gameDir in tokens.json")
        exit(2)

    tokens_data = checkTokens(tokens_data, options_data)

    #opthions

    if options_data['gettoken_thenLaunch']:
        
        launchMinecraft(tokens_data["Dirs"],
                        tokens_data["gameVersion"],
                        tokens_data['acc_token'],
                        tokens_data["user"]["name"],
                        tokens_data["user"]["uuid"])
        
    if options_data['launch_thenCleanbat']:
        bat_clean = open("launch.bat", "w")
        bat_clean.write("")
        bat_clean.close()
            
    if options_data['launch_thenRefreshToken']:
        tokens_data["acc_token"], tokens_data["ref_token"] = gettoken(options_data)
        savetokens(tokens_data)


        

        