"""
@author Zoe Millage
@summary Used to implement the strategy pattern. Holds the boat behaviors:
    pass-through: always allows a held boat to cross the update after adding it
    basic: fills and empties 1 unit per tick, only adds boats at level 0
        and release boats when level = depth
    fast empty: basic, but fills 1 unit per tick and empties 2 units per tick
"""


def basic(self, next):
    """
    @summary fills and empties 1 unit per tick, only adds boats at level 0
        and release boats when level = depth
    @param next: the next subsection to the right
    """
    # if there's a boat
    if self._hasBoat:

        # move level towards depth
        self._level += 1

        # release boat if we can
        if self._level == self._depth:
            # move into next area if it exists
            if next is not None:
                if not next.hasABoat() and next.canAcceptBoat():
                    next.addBoat(self._theBoat)
                    self.removeBoat()

            else:
                self.removeBoat()

    # move level towards 0 if not there already
    else:
        if self._level > 0:
            self._level -= 1



def fastEmpty(self, next):
    """
    @summary fills and empties at 1 and 2 units per tick, respectively.
        only adds boats at level 0 and release boats when level = depth
    @param next: the next subsection to the right
    """
    # if there's a boat
    if self._hasBoat:
        # move level towards depth, making sure we don't pass it
        self._level += 1

        # release boat if we can
        if self._level == self._depth:
            if next is not None:
                if not next.hasABoat() and next.canAcceptBoat():
                    next.addBoat(self._theBoat)
                    self.removeBoat()

            else:
                self.removeBoat()

    # move level towards 0 if not there already
    else:
        if self._level > 0:
            self._level -= 2
            if self._level < 0:
                self._level = 0



def passThrough(self, next):
    """
    @summary if the lock has a boat and the next section or
        subsection to the right doesn't, move the boat to the next section
    @param next: the next subsection to the right
    """
    if next is not None:
        if not next.hasABoat() and next.canAcceptBoat():
            if self._hasBoat:
                next.addBoat(self._theBoat)
                self.removeBoat()

    else:
        self.removeBoat()
