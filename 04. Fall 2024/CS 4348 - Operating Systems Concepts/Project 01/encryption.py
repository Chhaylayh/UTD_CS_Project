# encryption.py
import sys

passkey = None

def vigenere_encrypt(text, key):
    encrypted = []
    for i, char in enumerate(text):
        shift = ord(key[i % len(key)]) - ord('A')
        encrypted_char = chr((ord(char) - ord('A') + shift) % 26 + ord('A'))
        encrypted.append(encrypted_char)
    return ''.join(encrypted)

def vigenere_decrypt(text, key):
    decrypted = []
    for i, char in enumerate(text):
        shift = ord(key[i % len(key)]) - ord('A')
        decrypted_char = chr((ord(char) - ord('A') - shift + 26) % 26 + ord('A'))
        decrypted.append(decrypted_char)
    return ''.join(decrypted)

def main():
    global passkey
    while True:
        line = sys.stdin.readline().strip()
        if not line:
            continue
        command, *args = line.split(" ", 1)
        argument = args[0] if args else ""
        
        if command == "PASSKEY":
            passkey = argument.upper()
            print("RESULT")
        elif command == "ENCRYPT":
            if passkey is None:
                print("ERROR Password not set")
            else:
                encrypted_text = vigenere_encrypt(argument.upper(), passkey)
                print(f"RESULT {encrypted_text}")
        elif command == "DECRYPT":
            if passkey is None:
                print("ERROR Password not set")
            else:
                decrypted_text = vigenere_decrypt(argument.upper(), passkey)
                print(f"RESULT {decrypted_text}")
        elif command == "QUIT":
            break
        sys.stdout.flush()

if __name__ == "__main__":
    main()
