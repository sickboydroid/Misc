from constant import Colors

class Print:
    def __init__(self):
        pass

    def printRed(self, msg, end='\n'):
        self.printWithColor(Colors.FAIL, msg, end)

    def printGreen(self, msg, end='\n'):
        self.printWithColor(Colors.OKGREEN, msg, end)

    def printBlue(self, msg, end='\n'):
        self.printWithColor(Colors.OKBLUE, msg, end)
        
    def printBold(self, msg, end='\n'):
        self.printWithColor(Colors.BOLD, msg, end)

    def printWithColor(self, color, msg, end):
        print(f"{color}{msg}{Colors.ENDC}", end=end)
