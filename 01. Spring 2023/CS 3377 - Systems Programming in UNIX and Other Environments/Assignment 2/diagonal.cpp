#include <iostream>
#include <iomanip>
#include <string>
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "LineInfo.h"

using namespace std;

const int    LINESIZE = 16;

//use one command line argument
int main(int argc, char* argv[]) {
    try {
        if (argc != 2) {
            throw domain_error(LineInfo("Usage: diagonal <textstring>", __FILE__, __LINE__));
        }

        //create a file so that 16 rows of empty will appear with od -Ax -c command
        int fdbinaryout = open("diagonal.bin", O_CREAT | O_WRONLY | O_TRUNC, S_IRUSR | S_IWUSR);
        if (fdbinaryout == 0)
            throw domain_error(LineInfo("Open Failed File: diagonal.bin", __FILE__, __LINE__));

        char space = '.';

        for (int line = 0; line < LINESIZE; line++)
            for (int column = 0; column < LINESIZE; column++)
                if (write(fdbinaryout, &space, 1) == -1)
                    throw domain_error(LineInfo("write() failed ", __FILE__, __LINE__));

        //Each line of od outputs 16 characters 
        //So, to make the output diagonal, we will use 0, 17, 34, ....

        int n = strlen(argv[1]);
        for (int i = 0; i < n; i++) {
            if (lseek(fdbinaryout, (LINESIZE + 1) * i, SEEK_SET) == -1)
                throw domain_error(LineInfo("lseek() failed", __FILE__, __LINE__));
            if (write(fdbinaryout, &argv[1][i], 1) == -1)
                throw domain_error(LineInfo("write() failed ", __FILE__, __LINE__));
        }

        close(fdbinaryout);

        cout << "diagonal.bin" << " has been created." << endl
            << "Use od -c diagonal.bin to see the contents." << endl;
    }
    catch (exception& e) {
        cout << e.what() << endl;
        cout << endl << "Press the enter key once or twice to leave..." << endl;
        cin.ignore(); cin.get();
        exit(EXIT_FAILURE);
    }//catch

    exit(EXIT_SUCCESS);

}