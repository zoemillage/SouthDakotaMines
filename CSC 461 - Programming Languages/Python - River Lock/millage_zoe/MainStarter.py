"""
@name River Lock
@author Zoe Millage
@class CSC-461-M01, 2023f
@date Due 2 November 2023
@summary Simulates a river lock system, with river sections
    of varying lengths and flow speeds, locks of varying
    types and depths, and boats of different types and speeds.
    Implements the behavior pattern to allow different types of
    locks and boats.
    This file is the interface between the user and the RiverSystem.
    Allows the user to change the system

Grading tags in for all lines marked with *			        Hopefully

Tierless str meets D in SOLID (hidden test)*			    Hopefully
Check if above is done, but not its test was not reached	___

1. Initial Show system\Got it compiling
Menu\initial system working					tier passed, so hopefully
Bad input handled						    tier passed, so hopefully

2. Add Default
Added and shown properly					tier passed, so hopefully
Second+ item ignored						tier passed, so hopefully

3. Basic Update (single)
Moves along section						    tier passed, so hopefully
String format correct						tier passed, so hopefully
Iterator used*							    tier passed, so hopefully

4. Basic Update (multiple)					tier passed, so hopefully

5. Multi Update
Updates correctly						    tier passed, so hopefully
Bad input handled						    tier passed, so hopefully

6. Show details
Shows details properly 						tier passed, so hopefully
Iterator used*							    tier passed, so hopefully

6. Add user specified item
Basic movement still works					tier passed, so hopefully
Powered works							    tier passed, so hopefully
No passing							        tier passed, so hopefully

7. Tester part 1
Boats works up to second lock 				tier passed, so hopefully
Formatting correct 						    tier passed, so hopefully

8. Tester part 2
Boats works up to end						tier passed, so hopefully
Strategy pattern for basic fill*			tier passed, so hopefully
Strategy pattern for fast empty*			tier passed, so hopefully

9. Custom belt **
String formatting correct					tier passed, so hopefully
Everything still works 						tier passed, so hopefully
Bad input handled 						    tier passed, so hopefully
"""

from Boat import Boat
from RiverSystem import RiverSystem
from Lock import Lock
from Section import Section


global nextBoat
nextBoat = 1


def cleanInput(prompt):
    """
    @author Dr. Lisa Rebenitsch
    """
    result = input(prompt)
    # strips out blank lines in input
    while result == '':
        result = input()

    return result



def option7 (system):
    """
    @summary lets the user build a custom river system
    @param system: the system to customize
    """
    temp = "Section (1) or Lock (2):> "
    temp2 = "Add another component (n to stop):> "
    newSys = list()
    choice2 = 'y'
    restart = 0


    # allow adding as many parts as wanted
    while choice2 != 'n':
        try:
            if restart != 0:
                choice2 = str(cleanInput(temp2))
                restart = 0

            choice = int(cleanInput(temp))

            # error check input
            if choice < 1 or choice > 2:
                print("Input an option in the range 1-2")

            else:
                # add a section
                if choice == 1:
                    length = cleanInput("Length:> ")
                    length = int(length)
                    flow = int(cleanInput("Flow:> "))
                    newSys.append(1)
                    newSys.append(length)
                    newSys.append(flow)

                # add a lock
                else:
                    behavior = int(cleanInput("Fill behavior: None (1), Basic (2), or Fast Empty (3):> "))
                    depth = int(cleanInput("Depth:> "))
                    newSys.append(2)
                    newSys.append(behavior)
                    newSys.append(depth)

            choice2 = str(cleanInput(temp2))

        # handle a non-integer value
        except ValueError:
            print("Cannot accept value")
            restart = 1
            continue

        # handle other errors
        except:
            import traceback
            print(traceback.format_exc())

        if newSys:
            system.customRiver(newSys)



def main():
    menu = "\n" \
           "1) Add Default Boat\n" \
           "2) Update One Tick\n" \
           "3) Update X Ticks\n" \
           "4) Show Section Details\n" \
           "5) Add Boat\n" \
           "6) Make Tester\n" \
           "7) Make New Simulator\n" \
           "0) Quit\n"

    global nextBoat

    system = RiverSystem()
    system.defaultRiver()
    print(system)

    choice = -1
    altErr = 0
    while choice != 0:

        print(menu)
        try:
            choice = int(cleanInput("Choice:> "))

            # add default boat
            if choice == 1:
                newBoat = Boat(nextBoat)
                system.addBoat(newBoat)
                nextBoat += 1
                print(system)


            # update one time
            elif choice == 2:
                system.update()
                print(system)


            # update X number of times
            elif choice == 3:
                choice = int(cleanInput("How many updates:> "))

                for i in range(0, choice):
                    system.update()
                    print(system)


            # print out station details
            elif choice == 4:
                system.details()


            # make a new boat with a given speed and behavior
            elif choice == 5:
                # get the boat power and behavior
                choice = int(cleanInput("What engine power:> "))
                power = choice

                temp = "What travel method. (1) Steady or (2) Max :> "
                choice = int(cleanInput(temp))
                if choice < 1 or choice > 2:
                    print("Input an option in the range 1-2")

                else:
                    # make the boat and add it to the system
                    newBoat = Boat(nextBoat, power, choice)
                    system.addBoat(newBoat)
                    nextBoat += 1

                print(system)


            # make new system
            elif choice == 6:
                system.alternateRiver()
                print(system)


            # make new system
            elif choice == 7:
                # in its own function to do unique exceptions
                option7(system)
                print(system)


            # debug/check for D in SOLID in __str__
            elif choice == -1:
                # make the boats with proper ids
                boat1 = Boat(nextBoat)
                nextBoat += 1

                boat2 = Boat(nextBoat)
                nextBoat += 1

                boat3 = Boat(nextBoat)
                nextBoat += 1

                # make the section and lock
                section = Section.Subsection()  # section is all 6 sections; output just wants 1
                lock = Lock()

                # add boat1 to section, 2 to lock, and 3 to
                # current system
                section.addBoat(boat1)
                lock.addBoat(boat2)
                system.addBoat(boat3)

                # print everything
                # GRADING : TO_STR
                print(boat1)
                print(section)
                print(lock)
                print(system)


            elif choice == 0:
                pass


            # handle out of range inputs
            else:
                print("Input an option in the range 0-7")


        # handle a non-integer value
        except ValueError:
            print("Please, input a positive integer")

        # handle other errors
        except:
            import traceback
            print(traceback.format_exc())


if __name__ == '__main__':
    main()


