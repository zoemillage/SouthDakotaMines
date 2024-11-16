/** ***************************************************************************
 * @file  
 * 
 * @brief Contains the functions for bitpacking
 *****************************************************************************/

#include "bitpacking.h"


/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Packs three characters into a short int.
 * It packs them by bitwise or-ing 0 and the 1st character, shifting left by
 * 5 bits, or-ing the result with the 2nd character and shifting left 5 bits,
 * then or-ing with the last character.
 *
 * @param[in] high - the character to become the most significant bits
 * @param[in] middle - the character to become the middle bits
 * @param[in] low - the character to become the least significant bits
 * 
 * @returns the characters packed into a short int
 *
 *****************************************************************************/
short int packThree ( char high, char middle, char low )
{
    char maskAnd = 31;          //binary 0001 1111

    short int packedBits = 0;

    //zeros out extraneous bits 
    high = high & maskAnd;
    middle = middle & maskAnd;
    low = low & maskAnd;

    //copies and shifts bits to form the final value
    packedBits = packedBits | high;
    packedBits = packedBits << 5;

    packedBits = packedBits | middle;
    packedBits = packedBits << 5;

    packedBits = packedBits | low;

    //return the final value
    return packedBits;
}



/** ***************************************************************************
 * @author Zoe Millage
 *
 * @par Description:
 * Takes a packed short int along with a case signifier to unpack the int
 * into 3 separate characters
 *
 * @param[in] myCodes - the bitpacked characters
 * @param[in, out] high - the unpacked character in the most significant bits
 * @param[in, out] middle - the unpacked character in the middle bits
 * @param[in, out] low - the unpacked character in the least significant bits
 * @param[in] upperLower - the case of the characters, uppercase or lowercase
 *
 *****************************************************************************/
void unpackThree ( short int myCodes, char & high, char & middle, char & low, CHARCASE upperLower )
{
    char maskAnd = 31;          //binary 0001 1111
    char maskLowCase = 96;      //binary 0110 0000
    char maskUpCase = 64;       //binary 0100 0000

    //set letter char values to 0
    high = 0;
    middle = 0;
    low = 0;

    //copy values from myCodes to each letter char
    low = low | myCodes;

    //shift myCodes between bitwise functions so each char gets correct values
    myCodes = myCodes >> 5;

    middle = middle | myCodes;
    myCodes = myCodes >> 5;

    high = high | myCodes;

    //ensure there are no extraneous bits
    high = high & maskAnd;
    middle = middle & maskAnd;
    low = low & maskAnd;

    //add bits based on the intended case of the chars
    if (upperLower == UPPERCASE)
    {
        high = high | maskUpCase;
        middle = middle | maskUpCase;
        low = low | maskUpCase;
    }

    else
    {
        high = high | maskLowCase;
        middle = middle | maskLowCase;
        low = low | maskLowCase;
    }

    return;
}