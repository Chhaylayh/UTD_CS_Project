#include <iostream>
#include <iomanip>
#include <string>
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <vector>

#include "LineInfo.h"

using namespace std;

const int LINESIZE = 16;

int main(int argc, char* argv[]) {
    try {
        if (argc < 2) {
            throw domain_error(LineInfo("Usage: diagonal2 <textstring> [<textstring> ...]", __FILE__, __LINE__));
        }

        // Create a file with 16 rows of empty space
        int fdbinaryout = open("diagonal2.bin", O_CREAT | O_WRONLY | O_TRUNC, S_IRUSR | S_IWUSR);
        if (fdbinaryout == 0) {
            throw domain_error(LineInfo("Open Failed File: diagonal2.bin", __FILE__, __LINE__));
        }

        char space = '.';

        for (int line = 0; line < LINESIZE; line++) {
            for (int column = 0; column < LINESIZE; column++) {
                if (write(fdbinaryout, &space, 1) == -1) {
                    throw domain_error(LineInfo("write() failed ", __FILE__, __LINE__));
                }
            }
        }

        //Each line of od outputs 16 characters 
        //So, to make the output diagonal, we will use 0, 17, 34, ....
        
        int position = 0;
        for (int i = 1; i < argc; i++) {
            string word = argv[i];
            int length = word.length();
            for (int j = 0; j < length; j++) {
                position = (LINESIZE + 1) * (j + (i - 1) * length);
                if (lseek(fdbinaryout, position, SEEK_SET) == -1) {
                    throw domain_error(LineInfo("lseek() failed", __FILE__, __LINE__));
                }
                if (write(fdbinaryout, &word[j], 1) == -1) {
                    throw domain_error(LineInfo("write() failed ", __FILE__, __LINE__));
                }
            }
        }

        close(fdbinaryout);

        cout << "diagonal2.bin" << " has been created." << endl
             << "Use od -c diagonal2.bin to see the contents." << endl;
    }
    catch (exception& e) {
        cout << e.what() << endl;
        cout << endl << "Press the enter key once or twice to leave..." << endl;
        cin.ignore(); cin.get();
        exit(EXIT_FAILURE);
    }

    exit(EXIT_SUCCESS);
}
