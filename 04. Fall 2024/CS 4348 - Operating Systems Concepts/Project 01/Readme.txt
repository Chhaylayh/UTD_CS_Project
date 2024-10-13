List the files included in the archive and their purpose:
    - logger.py: Logs all actions with timestamps to a file and runs continuously until recieving a "QUIT" command. 
    - encryption.py: Encrypts and decrypts text using a Vigenère cypher. A passkey must be set before performing these operations.
    - driver.py: The main driver program that intereacts with the user, manages the logger and encryption programs, displays a command menu, and logs all the actions.
    - Readme.txt: Provide the overview of the project, instructions for compiling and running, and additional informations.
    - Write-up.docx: Detailed project report outlining the apporach, challeneges ecountered, and solutions.

Explain how to compile and run your project
	- Save logger.py, encryption.py and driver.py in the same directory
    - Run driver.py with the log file name as an argument:
        python driver.py log.txt

Include any other notes that the TA may need
    - N/A