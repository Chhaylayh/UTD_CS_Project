import sys

# Global variable to store the encryption passkey
passkey = None

def encrypt(text, key):

    # Array list to store the encrypted characters
    encrypted = []

    # Encrypt each character in the text
    for i, char in enumerate(text):
        shift = ord(key[i % len(key)]) - ord("A")
        encryptedChar = chr((ord(char) - ord("A") + shift) % 26 + ord("A"))
        encrypted.append(encryptedChar)
    
    # Join list into a single string and return
    return "".join(encrypted)

def decrypt(text, key):

    # Array list to store decrypted characters
    decrypted = []

    # Decrypt each character in the text
    for i, char in enumerate(text):
        shift = ord(key[i % len(key)]) - ord("A")
        decryptedChar = chr((ord(char) - ord("A") - shift + 26) % 26 + ord("A"))
        decrypted.append(decryptedChar)

    # Join list into a single string and return
    return "".join(decrypted)

def main():
    
    # Declare the global passkey variable for use within this function
    global passkey
    
    # Loop to continuously read and process user commands
    while True:

        # Read input from standard input and strip whitespace
        line = sys.stdin.readline().strip()
        
        # Skip empty lines
        if not line:
            continue

        # Split input into command and argument
        command, *args = line.split(" ", 1)

        # If argument exists, assign it; otherwise, assign an empty string
        argument = args[0] if args else ""
        
        # Command to set the passkey for encryption and decryption
        if command == "PASSKEY":
            passkey = argument.upper()
            print("RESULT")
        
         # Command to encrypt text
        elif command == "ENCRYPT":
            if passkey is None:
                print("ERROR Password not set")
            else:
                encryptedText = encrypt(argument.upper(), passkey)
                print(f"RESULT {encryptedText}")
        
         # Command to decrypt text
        elif command == "DECRYPT":
            if passkey is None:
                print("ERROR Password not set")
            else:
                decryptedText = decrypt(argument.upper(), passkey)
                print(f"RESULT {decryptedText}")
        
         # Command to quit the program
        elif command == "QUIT":
            break

        # Handle any unrecognized commands
        else:
            print("ERROR Invalid command")

        # Ensure output by flushing the standard output buffer
        sys.stdout.flush()

if __name__ == "__main__":
    main()
