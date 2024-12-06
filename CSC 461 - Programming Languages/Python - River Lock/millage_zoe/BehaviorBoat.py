"""
@author Zoe Millage
@summary Used to implement the strategy pattern. Holds the boat behaviors:
    steady: always moves one section length unless blocked by another boat
    max: moves between 1 and (power - riverFlow) river sections forward
"""


# GRADING: STEADY_TRAVEL
# the river sections hold the boat's position, so much of the logic is in the
# update functions in the Section and Subsection classes. "Section.py"
# around lines 145 and 287.
def steady(self):
    """
    @summary lets the section know this boat moves 1 tile
    @param self: useless for this function, but allows same call as max
    @return: the number of tiles to move the boat
    """
    return 1



# GRADING: MAX_TRAVEL
# the river sections hold the boat's position, so much of the logic is in the
# update functions in the Section and Subsection classes. "Section.py"
# around lines 145 and 287.
def max(self):
    """
    @summary determines the maximum number of tiles to move the boat
    @param self: forces deep binding on boat, allows access to power
    @return: the boat's power
    """
    return self._power
