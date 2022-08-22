# My Personal Project

## Connections between people

My personal project topic is to make an application that models connections between different people. 
Possible features:
- add people and their connections
- track who different people are connected to, and how they are connected
- visualize the connections between people in a group

This project might be used by people wanting to examine the structure of a particular group, or to have a way of
visualizing how a group is connected. This project is of interest to me because I find it difficult to keep track of 
people and understand complex social networks, and think this might help me understand them a little better.


## User Stories
Phase 1:
- As a user, I want to be able to add a person to the group.
- As a user, I want to be able to include a relationship between two people in the group.
- As a user, I want to be able to select a person and view their relationships.
- As a user, I want to be able to change the relationship between two people in the group.

Phase 2:
- As a user, I want to be able to save the group model to file.
- As a user, I want to be able to load a group model from file.

## Phase 4: Task 2
A sample of events that can occur. The large number of events with the same time (Sat Nov 20 12:03:31 PST 2021)
represent data being loaded from file.

Sat Nov 20 12:03:31 PST 2021
Person Alice added to the model
Sat Nov 20 12:03:31 PST 2021
Person Bob added to the model
Sat Nov 20 12:03:31 PST 2021
Connection Friends between Alice and Bob added to the model
Sat Nov 20 12:03:31 PST 2021
Person Carol added to the model
Sat Nov 20 12:03:45 PST 2021
Person Dave added to the model
Sat Nov 20 12:04:03 PST 2021
Connection Employer/Employee between Dave and Carol added to the model
Sat Nov 20 12:04:29 PST 2021
Connection Neighbors between Carol and Alice added to the model
Sat Nov 20 12:05:06 PST 2021
Converted group to JSON for saving

## Phase 4: Task 3
Refactors I might have made, had I more time:
- remove direct association between Connection and Group (reroute through Person, or make Group have a map of Persons to
Connections)
- replace some lists with sets if it makes handling duplicates easier
- get rid of Group's almost completely unused "name" field, if possible
- use Java Swing's Label class in ConnectionImage, if it would simplify things

Though I have other changes I want to make, only the above relate to refactoring.