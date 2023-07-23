from os import walk, path
from subprocess import run, PIPE

import fnmatch

def find(pattern, dirpath):
    result = []
    for root, dirs, files in walk(dirpath):
        for name in files:
            if fnmatch.fnmatch(name, pattern):
                result.append(path.join(root, name))
    return result

def cmd(command):
    return run(command, shell=True, stdout=PIPE, stderr=PIPE, universal_newlines=True)
