"""
@author Zoe Millage
@summary Holds river sections split into subsections. Holds 1 boat per
    subsection, and has a flow value that slows boat movement
"""

from RiverPart import RiverPart


class Section(RiverPart):
    def __init__(self, flow=0, length=6):
        super().__init__()
        self.__flow = flow
        self.__length = length
        self.__sections = list()
        for i in range(0, self.__length):
            temp = self.Subsection()
            self.__sections.append(temp)



    def __iter__(self):
        """
        @summary starts a backwards iterator
        @return: the current instance of itself
        """
        self.__index = 0
        return self



    def __next__(self):
        """
        @summary backwards iterates through the subsections
        @return: the next subsection to the right
        """
        try:
            self.__index -= 1
            return self.__sections[self.__index]
        except:
            raise StopIteration()



    def __str__(self):
        """
        @summary combines the __str__ of each subsection together into one
        @return: the combination of all subsections' __str__
        """
        theString = ""
        # get all subsections' print
        for i in self:
            theString = i.__str__() + theString

        return theString



    def addBoat(self, boat, partNo=0):
        """
        @summary adds a boat to a subsection of this section; defaults
            to adding to the leftmost subsection
        @param boat: the boat to add
        @param partNo: the index of the subsection to add to
        """
        self.__sections[partNo].addBoat(boat)
        self.checkHasBoat()
        self.checkNumBoats()



    def checkHasBoat(self):
        """
        @summary goes through each subsection to see if the
            section has any boats
        """
        self._hasBoat = False

        # checks if there's any boats in the section
        for i in self:
            if i.hasABoat():
                self._hasBoat = True



    def checkNumBoats(self):
        """
        @summary goes through each subsection to see how many
            boats are in the section
        """
        self._numBoats = 0

        # gets the number of boats
        for i in self:
            if i.hasABoat():
                self._numBoats += 1



    def details(self):
        """
        @summary returns the number of boats and the flow in this section
        @return: number of boats and section flow in a list
        """
        temp = [self._numBoats, self.__flow]

        return temp



    def display(self):
        """
        @summary gets all the extra details from each subsection
        @return: the extra details from all subsections in the section
        """
        theString = ""

        # gets the secondary string from each subsection
        if not self._hasBoat:
            for i in range(0, self.__length):
                theString = "〜〜〜" + theString

        else:
            for i in self:
                theString = i.display() + theString

        return theString



    def getBoat(self):
        """
        @summary gets the leftmost boat of the section
        @return: the boat of the leftmost boat
        """
        return self.__sections[self.__length - 1].getBoat()



    def hasABoat(self):
        """
        @summary gets if the leftmost boat of the section
        @return: if the leftmost section has a boat
        """
        return self.__sections[0].hasABoat()



    def update(self, next):
        """
        @summary updates all the subsections in this section
        @param next: the next section to the right
        """
        # keep track of index, useful for max speed behavior
        index = len(self.__sections) - 1
        maxIndex = len(self.__sections) - 1

        # go through each subsection
        for i in self:
            if i.hasABoat():
                # get the number of spaces to move
                numMovements = i.getUpdates(self.__flow)
                # current update can get thrown into separate function: steadyUpdate
                if numMovements == 1:
                    i.update(next)

                # maxUpdate checks indices
                else:
                    newPos = index + numMovements

                    # set to max index if greater
                    if newPos > maxIndex:
                        newPos = maxIndex

                    # go through each applicable index, but stop boat
                    # at the end of section if it gets there
                    noBoat = True
                    fIndex = index

                    while fIndex < newPos and noBoat:
                        # if there's a boat in the next subsection, stop updating
                        tempNext = self.__sections[fIndex + 1]

                        if tempNext.hasABoat():
                            noBoat = False

                        # if no boat, do a steadyUpdate
                        else:
                            self.__sections[fIndex].update(tempNext)

                        fIndex += 1

                    # if we're at the end, move into the next section
                    if fIndex == newPos:
                        i.update(next)

            next = i

            index -= 1

        # make sure the number of boats in the section is accurate
        self.checkNumBoats()
        self.checkHasBoat()



    class Subsection:
        def __init__(self):
            self.__hasBoat = False
            self.__theBoat = None



        def __iter__(self):
            """
            @summary creates an interator
            @return: the current instance of itself
            """
            return self



        def __next__(self):
            """
            @summary allows iteration. This holds no collection, though,
                so this does nothing.
            """
            raise StopIteration()



        def __str__(self):
            """
            @summary gives a visual of the section: 〜〜〜 if there isn't a
                boat, ⛴〜〜 otherwise
            @return: the section as a string
            """
            if self.__hasBoat:
                return self.__theBoat.__str__() + "〜〜"

            else:
                return "〜〜〜"



        def addBoat(self, boat):
            """
            @summary adds a given boat if there isn't one already
            @param boat: the boat to add
            """
            if not self.__hasBoat:
                self.__theBoat = boat
                self.__hasBoat = True



        def canAcceptBoat(self):
            """
            @summary mostly used by locks with special behaviors.
            Exists in all sections for consistent calling
            @return: true
            """
            return True



        def display(self):
            """
            @summary gives extra details on the section; prints the id of a
                boat if it has one alongside the 〜s that'd appear in __str__
            @return: extra details on the section as a string
            """
            if self.__hasBoat:
                boatId = self.__theBoat.getId()

                # try to make sure only 3 characters appear
                if boatId < 10:
                    return "%d〜〜" % boatId

                elif boatId < 100:
                    return "%d〜" % boatId

                else:
                    return "%d" % boatId

            # just print 〜〜〜 if there's no boat
            else:
                return "〜〜〜"



        def getBoat(self):
            if self.__hasBoat:
                return self.__theBoat



        def getUpdates(self, flow):
            num = self.__theBoat.update() - flow

            if num < 1:
                num = 1

            return num



        def hasABoat(self):
            return self.__hasBoat



        def removeBoat(self):
            """
            @summary removes the boat from this section if there is one
            """
            if self.__hasBoat:
                self.__theBoat = None
                self.__hasBoat = False



        def update(self, next):
            """
            @summary if the current subsection has a boat and the next
            section or subsection to the right doesn't, move the boat to the
            next section
            @param next: the next subsection to the right
            """
            if next is not None:
                if not next.hasABoat() and next.canAcceptBoat():
                    if self.__hasBoat:
                        next.addBoat(self.__theBoat)
                        self.removeBoat()

            else:
                self.removeBoat()
