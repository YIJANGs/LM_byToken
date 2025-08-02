import Gtoken



ReToken1 = open("./MCL/token.MCL", "r")
rr = ReToken1.read()
ReToken1.close()
ReToken2 = open("./MCL/token.MCL", "w")
if rr != "N":
    tR1, tR2 = Gtoken.retoken(rr)
    ReToken2.write(tR2)
    
    Gtoken.start(tR1)
else:
    print("请访问以下 URL 获取 code：")
    print("https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?client_id=00000000402b5328&response_type=code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=XboxLive.Signin%20offline_access&prompt=select_account")
    code = input("请输入返回的 code: ")
    A, B = Gtoken.get_microsoft_token(code)
    Gtoken.start(A)
    ReToken2.write(B)
ReToken2.close()
