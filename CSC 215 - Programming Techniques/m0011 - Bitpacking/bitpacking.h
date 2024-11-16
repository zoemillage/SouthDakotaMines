/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the enum and function prototypes for bitpacking.
 *****************************************************************************/

#ifndef __BITPACKING__H__
#define __BITPACKING__H__

#include <iostream>

using namespace std;

//write enum here
enum CHARCASE { UPPERCASE,   //represents uppercase letters, valued at 0
                LOWERCASE    //represents lowercase letters, valued at 1
              };

//write prototypes here
short int packThree ( char high, char middle, char low );
void unpackThree ( short int myCodes, char & high, char & middle, char & low, CHARCASE upperLower );

#endif