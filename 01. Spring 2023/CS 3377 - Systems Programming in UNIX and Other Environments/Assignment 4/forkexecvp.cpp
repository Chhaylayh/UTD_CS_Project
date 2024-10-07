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
#include <sys/wait.h>

#include "LineInfo.h"

using namespace std;

static const char* const QUOTES_FILE_NAME = "randomnumber.txt";

int const READ   = 0;
int const WRITE  = 1;
int const PIPE_ERROR = -1;
int const FORK_ERROR = -1;
int const CHILD_PID  =  0;
int const MAX_PIPE_MESSAGE_SIZE = 1000;
int const MAX_QUOTE_LINE_SIZE   = 1000;
int const EXECVP_FAILED = 1000;

void CreateArg1FileWithArg2RandomNumbersArg3RandomRange(string randomFileNameStr, unsigned noRandomNumbersUns, unsigned randomRange)
{

	ofstream outfileStream(randomFileNameStr);

	if(outfileStream.fail())
	{
		stringstream s;
		s << "Error opening file Random Numbers File " << randomFileNameStr << endl;
		throw domain_error(LineInfo(s.str(), __FILE__, __LINE__)); 
	} 

	// seed the random number generator
	time_t t; srand((unsigned)time(&t));

	for (unsigned i = 0; i < noRandomNumbersUns; i++)
		outfileStream << ((rand() % randomRange) + 1) << endl;

	outfileStream.close();
}

int main(int argc, char* argv[]) 
{

	int status;

	try 
	{
	
		if(argc != 4)
		{
			stringstream s;
			s 	<< endl << endl 
				<< "Wrong arguments" << endl
				<< "Usuage: " << endl
				<< "forkexecvp <file to create> <amount of random numbers> <random number range>" << endl
				<< "Example" << endl
				<< "forkexecvp randomNumber 10000 10" << endl << endl;
			throw domain_error(LineInfo(s.str(), __FILE__, __LINE__)); 
		} 
		
		string randomFileNameStr = argv[1];
		unsigned noRandomNumbersUns = atoi(argv[2]), randomRange = atoi(argv[3]);
				 
		cout << endl << argv[0] << " " << randomFileNameStr << " " << noRandomNumbersUns << " " << randomRange;

		CreateArg1FileWithArg2RandomNumbersArg3RandomRange(randomFileNameStr, noRandomNumbersUns, randomRange);

		pid_t pid;
		int peipeParentWriteChildReadfds[2];

		string messages[] = {"sum", "average", "greatest", "least"};
		int noOfMessages = sizeof(messages) / sizeof(messages[0]);

		for (int childProcessNo = 0; childProcessNo < noOfMessages; ++childProcessNo)
		{

			if (pipe(peipeParentWriteChildReadfds) == PIPE_ERROR)
			{

				stringstream s;
				s << "Unable to create pipe peipeParentWriteChildReadfds";
				throw domain_error(LineInfo(s.str(), __FILE__, __LINE__)); 
			}

			pid_t forkPid = fork();

			if (forkPid != CHILD_PID)
			{

				// parent
				close(peipeParentWriteChildReadfds[READ]);

				cout << "Parent pid: " << getpid() << " to Child Process No: " << childProcessNo << endl 
					 << "Send Message : " << messages[childProcessNo] << endl;

				if(write(peipeParentWriteChildReadfds[WRITE], messages[childProcessNo].c_str(), sizeof(messages[childProcessNo].c_str())) == PIPE_ERROR){
					stringstream s;
					s << "pipe write failed";
					throw domain_error(LineInfo(s.str(), __FILE__, __LINE__)); 
				} 
			}
			else
			{
				// child code
				close(peipeParentWriteChildReadfds[WRITE]);

				char pipeReadMessage[MAX_PIPE_MESSAGE_SIZE] = {0};
				
				if (read(peipeParentWriteChildReadfds[READ], pipeReadMessage, sizeof(pipeReadMessage)) == PIPE_ERROR)
				{
					stringstream s;
					s << "pipe read failed";
					throw domain_error(LineInfo(s.str(), __FILE__, __LINE__)); 
				}

				cout << "Child pid: " << getpid() << " Child Process No : " << childProcessNo << "\nReceived Message : " << pipeReadMessage << endl;

				// execvp
				char * arglist[] = { (char *)"./calculate", pipeReadMessage, (char*)randomFileNameStr.c_str(), NULL };

				cout << "Child pid: " << getpid() << " Child Process No : " << childProcessNo << "\nexecvp(" << arglist[0] << ", ./calculate, " << randomFileNameStr.c_str() << endl;

				// replace stdout to the answer file named with pipeReadMessage
				string answerFileName = "answer";
				answerFileName += pipeReadMessage;
				freopen(answerFileName.c_str(), "w", stdout);

				close(peipeParentWriteChildReadfds[READ]);

				if(execvp(arglist[0], arglist) == EXECVP_FAILED)
				{
					stringstream s;
					s << "execvp failed";
					throw domain_error(LineInfo(s.str(), __FILE__, __LINE__));
				}
			}
		} // for
    
	cout << "Parent pid: " << getpid() << " Start - Wait for calculate children to finished. " << endl;
	while (wait(&status) > 0);
	cout << "Parent pid: " << getpid() << " Done - Wait for calculate children to finished. " << endl;
	return 0;

	close(peipeParentWriteChildReadfds[WRITE]);

	cout << "Parent pid: " << getpid() << " Use execvp() cat to display answer files: " << endl;

	for(int childProcessNo = 0; childProcessNo < noOfMessages; ++childProcessNo)
	{

		pid_t forkpid = fork();

		if(forkpid < 0 )
		{
			stringstream s;
			s << "pipe read failed";
			throw domain_error(LineInfo(s.str(), __FILE__, __LINE__));	
		}
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