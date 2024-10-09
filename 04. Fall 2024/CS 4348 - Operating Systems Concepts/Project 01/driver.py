# driver.py
import sys
import os
from subprocess import Popen, PIPE

def main(log_file):
    history = []
    logger = Popen(['python', 'logger.py', log_file], stdin=PIPE, encoding='utf8')
    encryption = Popen(['python', 'encryption.py'], stdin=PIPE, stdout=PIPE, encoding='utf8')

    def log(action, message):
        logger.stdin.write(f"{action} {message}\n")
        logger.stdin.flush()
    
    log("START", "Driver started.")
    while True:
        print("Menu:")
        print("1. Set Password")
        print("2. Encrypt")
        print("3. Decrypt")
        print("4. Show History")
        print("5. Quit")
        choice = input("Enter choice: ").strip()

        if choice == "1":  # Set Password
            password = input("Enter password: ").strip()
            encryption.stdin.write(f"PASSKEY {password}\n")
            encryption.stdin.flush()
            result = encryption.stdout.readline().strip()
            log("PASSKEY", result)

        elif choice == "2":  # Encrypt
            text = input("Enter text to encrypt: ").strip()
            history.append(text)
            encryption.stdin.write(f"ENCRYPT {text}\n")
            encryption.stdin.flush()
            result = encryption.stdout.readline().strip()
            print(result)
            log("ENCRYPT", result)
            if result.startswith("RESULT"):
                history.append(result.split(" ", 1)[1])

        elif choice == "3":  # Decrypt
            text = input("Enter text to decrypt: ").strip()
            history.append(text)
            encryption.stdin.write(f"DECRYPT {text}\n")
            encryption.stdin.flush()
            result = encryption.stdout.readline().strip()
            print(result)
            log("DECRYPT", result)
            if result.startswith("RESULT"):
                history.append(result.split(" ", 1)[1])

        elif choice == "4":  # Show History
            print("History:")
            for index, entry in enumerate(history, 1):
                print(f"{index}: {entry}")
            log("HISTORY", "Displayed history")

        elif choice == "5":  # Quit
            encryption.stdin.write("QUIT\n")
            encryption.stdin.flush()
            logger.stdin.write("QUIT\n")
            logger.stdin.flush()
            log("EXIT", "Driver exited.")
            break

if __name__ == "__main__":
    log_file = sys.argv[1]
    main(log_file)
