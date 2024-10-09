# logger.py
import sys
from datetime import datetime

def log_message(log_file, action, message):
    with open(log_file, 'a') as f:
        timestamp = datetime.now().strftime('%Y-%m-%d %H:%M')
        f.write(f"{timestamp} [{action}] {message}\n")

def main(log_file):
    while True:
        line = sys.stdin.readline().strip()
        if line == "QUIT":
            break
        action, message = line.split(" ", 1)
        log_message(log_file, action, message)

if __name__ == "__main__":
    log_file = sys.argv[1]
    main(log_file)