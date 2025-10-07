from os import system
import json

class Launch:
    def __init__(self, dir, version, acc_token, username, uuid):

        self.gamedir = dir["gameDir"]
        self.assetsdir = dir["assetsDir"]
        self.javadir = dir["javaDir"]
        self.version = version
        self.acc_token = acc_token
        self.username = username
        self.uuid = uuid
        
        self.bat = f"\"{self.javadir}/java.exe\" "
    def launchMinecraft(self):
        self.libraries = open(f"{self.gamedir}/{self.version}.json", "r")
        self.libraries_data = json.load(self.libraries)
        self.libraries.close()

        self.libraries_list = self.libraries_data["libraries"]
        self.libraries_java_cp = ""
        for i in self.libraries_list:
            self.libraries_java_cp += f"{self.assetsdir}/libraries/{i['downloads']['artifact']['path']};"
            #print(i["downloads"]["artifact"]["path"])
        self.libraries_java_cp += f"{self.gamedir}/{self.version}.jar"
        self.bat += f"-cp \"{self.libraries_java_cp}\" "

        self.bat += "net.minecraft.client.main.Main "
        self.bat += f"--gameDir \"{self.gamedir}\" "
        self.bat += f"--assetsDir \"{self.assetsdir}/assets\" "
        
        self.assetindex = self.libraries_data["assetIndex"]["id"]
        self.bat += f"--assetIndex {self.assetindex} "

        self.bat += f"--version {self.version} "

        self.bat += f"--username {self.username} "
        self.bat += f"--uuid {self.uuid} "
        self.bat += f"--accessToken {self.acc_token} "
        self.bat += "--userType msa "

        #print(self.bat)

        self.bat_file = open("launch.bat", "w")
        self.bat_file.write(self.bat)
        self.bat_file.close()

        system("launch.bat")
        