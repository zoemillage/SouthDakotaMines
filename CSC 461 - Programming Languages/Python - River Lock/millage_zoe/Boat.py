"""
@author Zoe Millage
@summary the boats in the system. Moves across the river
    based on their behavior.
"""

import BehaviorBoat
import types


class Boat():
    def __init__(self, id, power=1, behavior=1):
        self.__id = id
        self._power = power
        self.__behavior = None

        if behavior == 1:
            self.setBehavior(BehaviorBoat.steady)

        else:
            self.setBehavior(BehaviorBoat.max)



    def __str__(self):
        return "⛴"



    def getId(self):
        return self.__id



    def setBehavior(self, behavior):
        theMethod = types.MethodType
        self.__behavior = theMethod(behavior, self)



    def update(self):
        """
        @summary gets the number of tiles to move the boat across
        @param flow: the flow of the boat's current river section
        @return: the number of tiles to move
        """
        return self.__behavior()
