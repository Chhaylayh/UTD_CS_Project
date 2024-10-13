import sys
from subprocess import Popen, PIPE


def main():
    
    # Check for the required command-line argument
    if len(sys.argv) != 2:
        print("Usuage: python logger.py <logFile>")
        sys.exit(1)

    # Assign the log file name from command-line argument
    logFile = sys.argv[1]

    # Array list to store history of actions for the session
    history = []

    # Start logger and encryption subprocesses, connecting to their stdin and stdout as needed
    logger = Popen(["python", "logger.py", logFile], stdin=PIPE, encoding="utf8")
    encryption = Popen(
        ["python", "encryption.py"], stdin=PIPE, stdout=PIPE, encoding="utf8"
    )

    # Sends a log message to the logger subprocess.
    def log(action, message):
        # Write action and message to logger
        logger.stdin.write(f"{action} {message}\n")

        # Ensure the message is sent immediately
        logger.stdin.flush()

    # Log the start of the driver
    log("START", "Driver started.")

    # Main interaction loop
    while True:
        # Display menu options
        print("\nMenu:")
        print("1. Set Password")
        print("2. Encrypt")
        print("3. Decrypt")
        print("4. Show History")
        print("5. Quit")

        # Prompt for choice and strip any extra whitespace
        choice = input("Enter choice: ").strip()

        # Set Password
        if choice == "1":
            password = input("Enter password: ").strip()
            encryption.stdin.write(f"PASSKEY {password}\n")
            encryption.stdin.flush()
            result = encryption.stdout.readline().strip()
            log("PASSKEY", result)

        # Encrypt
        elif choice == "2":
            text = input("Enter text to encrypt: ").strip()
            history.append(text)
            encryption.stdin.write(f"ENCRYPT {text}\n")
            encryption.stdin.flush()
            result = encryption.stdout.readline().strip()
            print(result)
            log("ENCRYPT", result)
            if result.startswith("RESULT"):
                history.append(result.split(" ", 1)[1])

        # Decrypt
        elif choice == "3":
            text = input("Enter text to decrypt: ").strip()
            history.append(text)
            encryption.stdin.write(f"DECRYPT {text}\n")
            encryption.stdin.flush()
            result = encryption.stdout.readline().strip()
            print(result)
            log("DECRYPT", result)
            if result.startswith("RESULT"):
                history.append(result.split(" ", 1)[1])

        # Show History
        elif choice == "4":
            print("History:")
            for index, entry in enumerate(history, 1):
                print(f"{index}: {entry}")
            log("HISTORY", "Displayed history")

        # Quit
        elif choice == "5":
            encryption.stdin.write("QUIT\n")
            encryption.stdin.flush()
            logger.stdin.write("QUIT\n")
            logger.stdin.flush()
            log("EXIT", "Driver exited.")
            break

        # Handle invalid input
        else:
            print("Invalid choice. Please select a number from 1 to 5.")


if __name__ == "__main__":
    main()