import sys

from datetime import datetime


# Logs a message with a timestamp and action label to the specified log file.
def logMessage(logFile, action, message):
    with open(logFile, "a") as f:
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M")
        f.write(f"{timestamp} [{action}] {message}\n")


def main():

    # Check for the required command-line argument
    if len(sys.argv) != 2:
        print("Usuage: python logger.py <logFile>")
        sys.exit(1)

    # Assign the log file name from command-line argument
    logFile = sys.argv[1]

    # Process input lines until receiving "QUIT"
    while True:
        inputLine = input().strip()
        if inputLine.upper() == "QUIT":
            break
           
        # Split input into action and message
        action, message = inputLine.split(" ", 1)
        
        # Log the message with specified action and message content
        logMessage(logFile, action, message)

if __name__ == "__main__":
    main()