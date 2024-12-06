"""
@author Zoe Millage
@summary The manager for the river. Holds sections and locks,
    which in turn hold boats.
"""

from Section import Section
from Lock import Lock
from Boat import Boat


class RiverSystem:

    def __init__(self):
        self.__sections = None



    def __iter__(self):
        """
        @summary starts a backwards iterator
        @return: the current instance of itself
        """
        # GRADING: ITER_RESTRICT
        self.__index = 0
        return self



    def __next__(self):
        """
        @summary backwards iterates through the river sections
        @return: the next section to the right
        """
        try:
            self.__index -= 1
            return self.__sections[self.__index]
        except:
            raise StopIteration()



    def __str__(self):
        """
        @summary combines the __str__ of each section together into one,
            then adds all the section's extra details
        @return: the combination of all sections' __str__ and details
        """
        theString = ""

        # get all sections' print
        for i in self:
            theString = i.__str__() + theString

        # add all the sections' details
        theString = theString + "\n" + self.display()

        return theString



    def addBoat(self, boat):
        """
        @summary adds a boat (or creates a default one) to the
            leftmost section of the river
        @param nextBoat: the id of the default boat
        @param boat: the pre-made boat to add
        """
        self.__sections[0].addBoat(boat)



    def addPart(self, newPart):
        """
        @summary adds a RiverPart to the river
        @param newPart: the part to add
        """
        self.__sections.append(newPart)



    def alternateRiver(self):
        """
        @summary resets the river to an alternate default:
            a section of length 5 and flow 0, a none lock with depth 0,
            a section of length 6 and a flow of 2, a basic lock with depth 2,
            a section of length 3 and flow 3, and a fast-emptying lock with
            a depth of 5
        """
        # reset river
        self.__sections = list()

        # add the sections
        newPart = Section(0, 5)
        self.addPart(newPart)
        newPart = Lock()
        self.addPart(newPart)
        newPart = Section(2, 6)
        self.addPart(newPart)
        newPart = Lock(2, 2)
        self.addPart(newPart)
        newPart = Section(3, 3)
        self.addPart(newPart)
        newPart = Lock(5, 3)
        self.addPart(newPart)



    def customRiver(self, parts):
        """
        @summary lets the user create a custom system,
        adding as many sections as they want, with whatever length and
        flow they want, and adding as many locks, with whichever behavior
        and depth that they want.
        @param parts: an integer list specifying what parts to add
        @return:
        """
        # reset river
        self.__sections = list()

        # the list has 3 integers per river system part
        length = len(parts) // 3

        # add sections
        for i in range(length):
            temp = i * 3

            # if value of parts[i] is 1, make section
            # parts[i + 1] is length, parts[i + 2] is flow
            if parts[temp] == 1:
                newPart = Section(parts[temp + 2], parts[temp + 1])
                self.addPart(newPart)

            # if value of parts[i] is 2, make lock
            # parts[i + 1] is behavior, parts[i + 2] is depth
            else:
                newPart = Lock(parts[temp + 2], parts[temp + 1])
                self.addPart(newPart)



    def defaultRiver(self):
        """
        @summary adds a default river, which has a section 6 units long
        with 0 flow, a none lock with depth 0, and another section of
        length 3 and 1 flow
        """
        # reset river
        self.__sections = list()

        # add the sections
        newPart = Section()
        self.addPart(newPart)
        newPart = Lock()
        self.addPart(newPart)
        newPart = Section(1, 3)
        self.addPart(newPart)



    def details(self):
        """
        @summary gets all the (non lock) sections, list them and the
            number of boats and flow in each
        """
        theString = ""
        theList = list()
        index = 1

        # gets the details from each section
        # GRADING: LOOP_RESTRICT
        for i in self:
            listAddition = i.details()

            # only river sections will have useful info
            # the number of boats and the section's flow
            if listAddition is not None:
                theList = listAddition + theList

        # make the loop condition one less, it will be
        #   incremented 2 in each loop
        theRange = len(theList) - 1

        # get all the data and put it into the right string format
        i = 0
        while i < theRange:
            theString += "Section %d\n" % index
            theString += "Boats: %d " % theList[i]
            i += 1
            theString += "Flow: %d\n" % theList[i]
            index += 1
            i += 1

        # print the string instead of returning it
        print(theString)



    def display(self):
        """
        @summary combines the extra details for every section into 1 string
        @return: a string with the river system's extra details
        """
        theString = ""

        # gets the details of each section
        for i in self:
            theString = i.display() + theString

        return theString



    def update(self):
        """
        @summary updates every section in the system
        """
        # default behavior needs the next section to the right
        next = None

        for i in self:
            i.update(next)
            next = i
