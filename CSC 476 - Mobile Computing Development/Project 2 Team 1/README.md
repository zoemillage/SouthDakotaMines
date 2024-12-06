# Mobile P1T3

## About
Project 2 for Team 1 in the Spring 2024 Mobile Computing Development (CSC 476/576) Course. Composed of
- Gage Jager: focusing on sequencing.
- Marcus Kane: focusing uploading.
- Zoe Millage: focusing downloading.



## Tentative Task Allocation
Gage
- Sequencing

Marcus
- Uploading

Zoe
- Downloading

## General Requirements
- Remember to allocate / remove listeners at appropriate times
- No duplicate activities on the stack
- No static variables for sending data between classes
- Support rotation at all times
- Need a reset JSON for easier testing


### Sequencing:
- Create account / login / auth / logout for users
 -Password must be entered with the password type edittext
 - NO EMAIL VERIFICATION
- Login remembrance
- Exit menu option functionality
 - Move both players to some game ended sequence. Either end activity or new activity
- Handle which player is active
- Handle server turn change / game state maintenance
 - Don’t check more than once per second
 - Users cannot access another’s data

### Uploading:
- Sending player timeout
 - 30 - 90 seconds
 - Has grading tag GRADING: TIMEOUT
- Handle temporary connection loss
- Sending exit notification
- Sending data
- Handle upload errors

### Downloading:
- Getting data
- Handle download errors
- Detecting player timeout and exit notifications
 - Forwarding the exit
