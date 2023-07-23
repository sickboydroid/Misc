#!/usr/bin/python3
import os
import constant
from builder import Builder
from print_utils import Print
from builder_helper import BuilderHelper

class Main:
    def __init__(self):
        global Print
        Print = Print()
        os.system('clear')
        self.do_initial_setup()
        self.show_option_list()

    def do_initial_setup(self):
        if not os.path.exists(constant.DIR_ANDROID_BUILDER):
            os.mkdir(constant.DIR_ANDROID_BUILDER)
        if not os.path.exists(constant.FILE_CONFIG):
            with open(constant.FILE_CONFIG, 'w') as config_file:
                config_file.write(constant.DEFAULT_CONFIG)
        if not os.path.exists(constant.DIR_BUILD):
            os.mkdir(constant.DIR_BUILD)
        if not os.path.exists(constant.DIR_BUILD_BIN):
            os.mkdir(constant.DIR_BUILD_BIN)
        if not os.path.exists(constant.DIR_BUILD_OBJ):
            os.mkdir(constant.DIR_BUILD_OBJ)
        if not os.path.exists(constant.DIR_BUILD_GEN):
            os.mkdir(constant.DIR_BUILD_GEN)
            
    def show_option_list(self):
        print('*'*30)
        print(':: Android Builder ::')
        print('*'*30)
        print('\nSelect an option:')
        print('\t1: Build application')
        print('\t2: Custom build')
        print('\t3: Run application')
        print('\t4: Refresh build')
        print('\t5: Exit')
        choice = None
        while choice != 5:
            choice = input('Enter a choice [1-5]: ')
            try:
                choice = int(choice)
            except ValueError:
                Print.printRed(f"INVALID CHOICE: '{choice}'")
                continue
           
            # Manipulate user choice
            if choice == 1: # Build application
                os.system('clear')
                self.build_application()
                break
        
            elif choice == 2:
                pass
            elif choice == 3:
                pass
            elif choice == 4:
                pass
            elif choice == 5: # Exit
                continue
            else:
                print(f"INVALID CHOICE: '{choice}'")

    def build_application(self):
        builder = Builder()
        
        # Compile android raw resources
        Print.printBold('> Compiling <resources> using [aapt2]...', end='')
        process = builder.compile_resources()
        if not self.print_task_end("aapt2", process):
            return

        # Link AndroidManifest.xml and generate R.java
        Print.printBold('\n> Linking <resources> using [aapt2]...', end='')
        process = builder.link_resources()
        if not self.print_task_end("aapt2", process):
            return

        # Compile java source code
        Print.printBold("\n> Compiling <java> using [ecj]...", end='')
        process = builder.compile_source_code()
        if not self.print_task_end("ecj", process):
            return

        # Dex source code
        Print.printBold("\n> Dexing <class> using [d8]...", end='')
        process = builder.dex_source_code()
        if not self.print_task_end('d8', process):
            return

        # Create apk from resources.arsc and classes.dex
        Print.printBold("\n> Adding <dex> to <apk> using [zip]...", end='')
        process = builder.add_dex_to_apk()
        if not self.print_task_end('zip', process):
            return

        # Align and sign in apk
        Print.printBold("\n> Aligning and signing <apk> using [jarsigner & zipalign]...", end='')
        process = builder.sign_and_align_apk()
        if not self.print_task_end('jarsigner & zipalign', process):
            return

        # Prompt for installation of apk
        Print.printBold("\n> Installing <apk> using [am & DevColleague]...", end='')
        process = BuilderHelper().prompt_install()
        if not self.print_task_end('am & DevColleague', process):
            return
        
    def print_task_end(self, task, process):
        if process.returncode != 0: 
            # Command was unsuccessful
            print("\n" + process.stdout + process.stderr)
            Print.printRed(f'  > [{task}]... FAIL')
            print('*'*30)
            return False
        elif not (process.stdout + process.stderr).strip():
            # Command was successful but did not produced any output
             Print.printGreen(' OK')
        else:
            # Command was successful but produced some output
            print("\n\n" + process.stdout + process.stderr)
            Print.printGreen(f'  > [{task}]... OK')
        return True


def main():
    main_obj = Main()

if __name__ == '__main__':
    main()
