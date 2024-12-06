"""
@author Zoe Millage
@summary Holds locks, which can only hold 1 boat, need to be at level 0 to
    accept a boat, and needs level == depth to release a boat.
    Filling/emptying is based on their behaviors.
"""

import BehaviorLock
from RiverPart import RiverPart
import types


class Lock(RiverPart):
    def __init__(self, depth=0, behavior=1):
        super().__init__()
        self.__behavior = None
        self._depth = depth
        self._level = 0
        self._theBoat = None

        if behavior == 1:
            self.setBehavior(BehaviorLock.passThrough)

        elif behavior == 2:
            self.setBehavior(BehaviorLock.basic)

        else:
            self.setBehavior(BehaviorLock.fastEmpty)



    def __str__(self):
        """
        @summary returns _X( #)_ where # is the water level of the lock.
            If the lock holds a boat, it prints _⛴( #)_ instead
        @return: a visual of the lock
        """
        if self._hasBoat:
            return "_" + self._theBoat.__str__() + "(%2d)_" % self._level

        else:
            return "_X(%2d)_" % self._level



    def addBoat(self, boat):
        """
        @summary adds the given boat to the lock if it doesn't have one already
        @param boat: the boat to add
        """
        if not self._hasBoat:
            self._theBoat = boat
            self._hasBoat = True
            self._numBoats = 1



    def canAcceptBoat(self):
        """
        @summary locks can only take in a boat if level = 0.
            This checks for that
        @return: if the lock can accept a boat
        """
        if self._level == 0:
            return True

        return False



    def display(self):
        """
        @summary gets extra details on the lock as a string. prints ....... or
            the boat id and some .s if there is one
        @return: extra details on the lock
        """
        if self._hasBoat:
            boatId = self._theBoat.getId()

            # try to make sure each output has the same number of characters
            if boatId < 10:
                return "%d......" % boatId

            elif boatId < 100:
                return "%d....." % boatId

            else:
                return "%d...." % boatId

        # just output ....... if there's no boats
        else:
            return "......."



    def getBoat(self):
        return self._theBoat



    def removeBoat(self):
        """
        @summary removes the boat from the lock if there is one
        """
        if self._hasBoat:
            self._theBoat = None
            self._hasBoat = False
            self._numBoats = 0



    def setBehavior(self, behavior):
        theMethod = types.MethodType
        self.__behavior = theMethod(behavior, self)



    def update(self, next):
        """
        @summary updates the lock; depends on behavior
        @param next: the next subsection to the right
        """
        self.__behavior(next)
