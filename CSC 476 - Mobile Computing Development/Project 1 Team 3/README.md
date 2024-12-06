# Mobile P1T3



## About

Project 1 for Team 3 in the Spring 2024 Mobile Computing Development (CSC 476/576) Course. Composed of
- Gabe Jerome: focusing on the model,
- Samantha Kaltved: focusing on the controller,
- Zoe Millage: focusing on the view.



## Tentative Task Allocation

Gabe
- Welcome Activity layout
- The Game class
- Possibly the “how to play” dialogue box or activity

Samantha
- Capture Selection Activity layout
- Activity sequencing
- 1 of the CSC 576 tasks

Zoe
- Game Activity and Game View layouts
- Touch events in the Game Activity
- Probably the custom graphics


## General Requirements

- 2 player local game
- Works correctly on 
    - A generic phone
    - A generic tablet
    - 5” phone
    - In both landscape and portrait modes
        - Rotating doesn’t interrupt game play
- Rotating / activity switching must complete within 2 seconds on course phone
- No redundant activities (1 or less of each activity on the stack at a time)
    - Must ensure this is true even with the back button
        - Dr. Rebenitsch will give the button functionality back for testing if it’s overridden to do nothing
- Back button behavior must be handled
    - **_cannot_** undo a player’s turn
    - needs inline comment: GRADING: BACK
- Must transfer/maintain game state with bundles/intents/serialization/parcelable
     - _**No static variables, will result in -25% to grade**_
- Must be able to play 2+ consecutive rounds
- Must have MVC pattern, with each part handled by a different person
    - Model
        - has a Game class that manages game state
        - applies capture rules to game board
        - person handles the welcome activity’s layout
    - Controller
        - Activity sequencing throughout the game
            - Detecting/carrying out when to move to which activity
            - Possibly command forwarding
        - Manages current player/round
        - Person handles the capture selection activity’s layout
    - View
        - Person handles game activity layout and game area custom view
        - Handles touches
        - Displays player names, turn/round info, and collection mode
- Needs an app icon
    - At all resolutions for the program
    - Not the android icon
- No @SuppressLint()/noinspection/tools:ignore other than standards from tutorials
 - 576 students must do one of
    - Intro splash screen with a timer that forwards to initial activity after 3-10 seconds
    - Create a custom color theme
    - Use paging fragments instead of activities for sequencing if doing the controller

## Activity/View Requirements
- Opening the game loads initial activity
    - 2 text boxes for player names
    - Number of rounds spinner (1, 3, 5)
    - Start button
        - Leads to game (or capture option) activity
    - “How to play” button that opens a dialog box or activity
        - Needs instructions on how to play
- Game activity
    - Board of collectables
        - Position must be constant, even with rotation
    - Current capture option, can move to capture option activity at any time
    - Visible at all times:
        - Player names
        - Player scores
        - Round number
        - Whose turn it is
        - What the player should do
    - All movement and placement must be done with touch events
    - Capture should probably be moved by any touch, forcing touch to be on the capture device can cause player frustration
    - Only 1 capture per turn, have some way to confirm the selection
    - Captures must have visual indicators (easiest is probably by changing color)
    - Captures can be made with primitives or bitmaps
    - Game play area must be in a view, with the boundary clearly marked
        - Must have a constant aspect ratio when rotating
        - Must stay as close in size as possible
- Capture option activity
    - Guaranteed collect
        - small movable circle that collects either
            - The single nearest collectable
            - All collectables under it
    - Rectangle collect
        - Resizable w/ 2 touches
        - Higher size = lower capture chance
        - Default must be 50% capture probability
    - Line collect
        - Rotatable and movable, but not resizable, with 2 touches
        - Collects under/near line
            - Collection distance must be size of collectable at minimum
            - Collection size must be 20% – 80% of play area’s width 
                - 1 – 4 tile widths for a 5x5 grid
            - 75% capture chance
        - Scaling and rotation must stay under touch point 
        - Scaling and rotation can’t send objects flying offscreen
 - Game end activity
    - Loads when the final collectable is collected
    - Shows which player won
    - Needs some “return to opening activity” button
