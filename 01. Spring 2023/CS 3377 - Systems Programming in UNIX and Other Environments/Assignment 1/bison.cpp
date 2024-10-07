#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>

#include <chrono>
#include <limits>
#include <locale>

#include "LineInfo.h"

using namespace std;
using namespace chrono;

const string INPUT_FILE_NAME = "bisonsearchin.txt";
const string OUTPUT_FILE_NAME = "bisonfoundin.txt";
const uint8_t MAX_FILE_NO = 10;

int main(){
	
	//condition cout set to local digit separation commas (USA)
	cout.imbue(locale("")) ;
	
	string searchBisonInGrassStr;
	string inputFileNameStr,  outputFilenameStr;
	uint16_t fileCount = 0;
	
	try {
		do{
			fileCount++;
			
			inputFileNameStr = outputFilenameStr = to_string(fileCount);
			
			if(inputFileNameStr.size() == 1){
				inputFileNameStr= "0" + inputFileNameStr;
				outputFilenameStr = "0" + outputFilenameStr;
			}

			inputFileNameStr = inputFileNameStr + INPUT_FILE_NAME;
			outputFilenameStr = outputFilenameStr + OUTPUT_FILE_NAME;

			ifstream inputParensStreamObj(inputFileNameStr);
			
			if (condition that checks for error)
				throw domain_error(LineInfo("application error message", __FILE__, __LINE__));

			ofstream outputParensStreamObj(outputFilenameStr);

			inputParensStreamObj >> searchBisonInGrassStr;

			unsigned answerFoundBisonPatternCount = 0, 
				backParenCount = 0;

			auto timeStart = steady_clock::now();

			size_t size = searchBisonInGrassStr.size();
			for (unsigned i = 1; i < size; i++)
			
				if(searchBisonInGrassStr[i - 1] == ')' && searchBisonInGrassStr[i] == ')')
					answerFoundBisonPatternCount += backParenCount;

				else if (searchBisonInGrassStr[i - 1] == '(' && searchBisonInGrassStr[i] == '(')
					backParenCount++;

				auto timeElapsed = duration_cast<nanoseconds> (steady_clock::now() - timeStart);

				outputParensStreamObj << "Time Elapsed (nano) : " << setw(15) << timeElapsed.count() << endl << endl;
				outputParensStreamObj << "Found Pattern Count : " << answerFoundBisonPatternCount << endl << endl;
				outputParensStreamObj << "Search Pattern	  : "  << endl << endl;
				outputParensStreamObj << searchBisonInGrassStr;

				outputParensStreamObj.close();

		} while (fileCount != MAX_FILE_NO);
	} //try

	catch(exception& e){
		cout << e.what() << endl;
		cout << endl << "Press the enter key once or twice to leave..." << endl;
		cin.ignore(); cin.get();
		exit(EXIT_FAILURE);
	} //catch

	return EXIT_SUCCESS;
    
} // main()