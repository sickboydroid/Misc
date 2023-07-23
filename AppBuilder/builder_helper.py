import utils
import constant
from shutil import copyfile

class BuilderHelper(object):
    def __init__(self):
        pass

    def get_android_manifests(self):
        return utils.find("AndroidManifest.xml", ".")

    def get_java_files(self):
        return utils.find("*.java", ".")

    def get_class_files(self):
        return utils.find("*.class", constant.DIR_BUILD_OBJ)
    
    def prompt_install(self):
        copyfile(constant.FILE_SIGNED_APK, constant.FILE_EXTERNAL_SIGNED_APK)
        command = f'am start --activity-clear-top --es path {constant.FILE_EXTERNAL_SIGNED_APK} -n com.gameofcoding.devcolleague/.activities.AppInstallerActivity > /dev/null'
        return utils.cmd(command)
