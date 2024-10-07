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
#include <vector>
#include <iterator>
#include <numeric>
#include <algorithm>

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

int main(int argc, char* argv[]) {

	try {
	
		if (argc != 3){
			stringstream s;
			s << endl << endl
			  << "Wrong arguments" << endl
			  << "Usuage: " << endl
			  << "Calulate < sum | average | greatest | least > filename.txt" << endl
			  << "filename - a file that contains integers, 1 integer per line " << endl
			  << "Example: " << endl
		 	  << "Calculate sum filerandom.txt" << endl << endl;
			throw domain_error(LineInfo(s.str(), __FILE__, __LINE__));
		}
		
		cout << argv[0] << " " << argv [1] << " " << argv [2] << endl;

		vector<int> vectorInts;

		ifstream randomNosStreamObj(argv[2]);

		if (randomNosStreamObj.fail())
			throw domain_error(LineInfo("\n\nopen FAILURE File\n\n" + string(1, (*argv[2])),  __FILE__, __LINE__));

		istream_iterator<int> inputIt(randomNosStreamObj);

		copy(inputIt, istream_iterator<int>(), back_inserter(vectorInts));

		string command = argv[1];

		if (command == "sum")
			cout << "Sum: " << accumulate(vectorInts.begin(), vectorInts.end(), 0) << endl;

		else if (command == "average")
			cout << "Average: " << ((accumulate(vectorInts.begin(), vectorInts.end(), 0)) / ((float)vectorInts.size())) << endl;
		
		else if (command == "least")
			cout << "Least: " << *(min_element(vectorInts.begin(), vectorInts.end())) << endl;

		else if (command == "greatest")
			cout << "Greastest: " << *(max_element(vectorInts.begin(), vectorInts.end())) << endl;	

		else {
			stringstream s;
			s << endl << endl
			  << "Invalid Command: command" << endl
			  << "Usuage: " << endl
			  << "Calulate < sum | average | greatest | least > filename.txt" << endl
			  << "filename - a file that contains integers, 1 integer per line " << endl
			  << "Example: " << endl
		 	  << "Calculate sum filerandom.txt" << endl << endl;
			throw domain_error(LineInfo(s.str(), __FILE__, __LINE__));
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