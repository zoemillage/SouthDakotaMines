"""
@author Zoe Millage
@summary the parent class for locks and sections. The general class
    that the RiverSystem holds/manages.
"""

from abc import abstractmethod


class RiverPart:
    def __init__(self):
        self._hasBoat = False
        self._numBoats = 0



    def __iter__(self):
        """
        @summary creates an interator
        @return: the current instance of itself
        """
        self.__index = 0
        return self



    def __next__(self):
        """
        @summary allows iteration. This holds no collection, though,
            so this does nothing.
        """
        raise StopIteration()



    @abstractmethod
    def addBoat(self):
        """
        @summary adds a boat to the part. More details in
            proper implementations.
        """
        pass



    def canAcceptBoat(self):
        """
        @summary mostly used by locks with special behaviors.
        Exists in all sections for consistent calling
        @return: true
        """
        return True



    def details(self):
        """
        @summary only useful with sections. Is here so the lock has
        this function defined.
        @return:
        """
        return None



    def getNumBoats(self):
        return self._numBoats



    def hasABoat(self):
        return self._hasBoat



    @abstractmethod
    def update(self):
        """
        @summary updates the section by 1 tick. More details in
            proper implementations.
        """
        pass
