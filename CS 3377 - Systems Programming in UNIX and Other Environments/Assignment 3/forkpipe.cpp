#include <stdlib.h>
#include <string.h>
#include <cstdlib>
#include <cstring>
#include <unistd.h>
#include <stdio.h>
#include <iostream>
#include <sys/types.h>
#include <time.h>
#include <fstream>

#include "LineInfo.h"

using namespace std;

static const char* const QUOTES_FILE_NAME = "quotes.txt";

int const READ   = 0;
int const WRITE  = 1;
int const PIPE_ERROR = -1;
int const FORK_ERROR = -1;
int const CHILD_PID  =  0;
int const MAX_PIPE_MESSAGE_SIZE = 1000;
int const MAX_QUOTE_LINE_SIZE   = 1000;


void getQuotesArray(char *lines[], unsigned &noLines){
	
    // Open the file for reading
    ifstream file(QUOTES_FILE_NAME);
    if (!file.is_open()) {
        throw runtime_error("Unable to open file " + string(QUOTES_FILE_NAME));
    }
    // Read the quotes from the file into the array
    string line;
    noLines = 0;
    while (getline(file, line)) {
        if (noLines >= 1000) {
            break; // Don't read more than 1000 quotes
        }
        if (line.length() > MAX_QUOTE_LINE_SIZE) {
            continue; // Skip lines that are too long
        }
        lines[noLines] = new char[line.length() + 1];
        strncpy(lines[noLines], line.c_str(), line.length() + 1);
        noLines++;
    }

    // Close the file
    file.close();

}

void executeParentProcess(int pipeParentWriteChildReadfds[], int pipeParentReadChildWritefds[], int noOfParentMessages2Send) {
	
    // Close unused read/write end of pipes
    close(pipeParentWriteChildReadfds[READ]);
    close(pipeParentReadChildWritefds[WRITE]);
    
    // Write to pipe and wait for child to respond
    char getQuoteMessage[] = "Get Quote";
    write(pipeParentWriteChildReadfds[WRITE], getQuoteMessage, strlen(getQuoteMessage) + 1);

    // Read from child pipe
    char quoteMessage[MAX_QUOTE_LINE_SIZE];
    read(pipeParentReadChildWritefds[READ], quoteMessage, sizeof(quoteMessage));
    printf("In Parent: Read from pipe pipeParentReadChildMessage read Message: %s\n", quoteMessage);

    // Close remaining read/write end of pipes
    close(pipeParentWriteChildReadfds[WRITE]);
    close(pipeParentReadChildWritefds[READ]);
}

void executeChildProcess(int pipeParentWriteChildReadfds[], int pipeParentReadChildWritefds[], char* lines[], unsigned noLines) {
	
    // Close unused pipe ends
    close(pipeParentWriteChildReadfds[1]);
    close(pipeParentReadChildWritefds[0]);

    // Read message from parent
    char buffer[MAX_PIPE_MESSAGE_SIZE];
    read(pipeParentWriteChildReadfds[0], buffer, MAX_PIPE_MESSAGE_SIZE);
    printf("In Child: Read from pipe pipeParentWriteChildMessage read Message: %s\n", buffer);

    // Send quotes to parent
    for (unsigned i = 0; i < noLines; i++) {
        write(pipeParentReadChildWritefds[1], lines[i], strlen(lines[i]) + 1);
        printf("In Child: Wrote to pipe pipeParentReadChildMessage Sent Message: %s\n", lines[i]);
        sleep(1);
    }

    // Close pipe ends
    close(pipeParentWriteChildReadfds[0]);
    close(pipeParentReadChildWritefds[1]);
}

int main(int argc, char* argv[]) {

 try {
   if (argc != 2)
       throw domain_error(LineInfo("Usage: ./forkpipe <number>", __FILE__, __LINE__));
    
   //argv[0] is the program name, argv[1] is the number, atoi = ascii to int
   int      noOfParentMessages2Send = atoi(argv[1]);
   char     *lines[1000];
   unsigned noLines;
  
   getQuotesArray(lines, noLines);
    
   int pipeParentWriteChildReadfds[2], 
       pipeParentReadChildWritefds[2];
   int pid;
    
   // create pipes    
   if (pipe(pipeParentWriteChildReadfds) == PIPE_ERROR)
     throw domain_error(LineInfo("Unable to create pipe pipeParentWriteChildReadfds", __FILE__, __LINE__));

   if (pipe(pipeParentReadChildWritefds) == PIPE_ERROR)
     throw domain_error(LineInfo("Unable to create pipe pipeParentReadChildWritefds", __FILE__, __LINE__));
   
   cout << endl <<endl;

   pid = fork();

   if      (pid == FORK_ERROR)
       throw domain_error(LineInfo("Fork Error", __FILE__, __LINE__));

   else if (pid != CHILD_PID) {
      // Parent process
      executeParentProcess(pipeParentWriteChildReadfds, pipeParentReadChildWritefds, noOfParentMessages2Send);
    }
   else { 
      //child process
      executeChildProcess(pipeParentWriteChildReadfds, pipeParentReadChildWritefds, lines, noLines);
   }
 }//try
 catch (exception& e) {
    cout << e.what() << endl;
    cout << endl << "Press the enter key once or twice to leave..." << endl;
    cin.ignore(); cin.get();
    exit(EXIT_FAILURE);
 }//catch

exit (EXIT_SUCCESS);

}
