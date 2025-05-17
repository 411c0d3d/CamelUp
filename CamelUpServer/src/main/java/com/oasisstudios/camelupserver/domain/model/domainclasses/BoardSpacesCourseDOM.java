package com.oasisstudios.camelupserver.domain.model.domainclasses;

import java.util.NoSuchElementException;

public class BoardSpacesCourseDOM {
    private final int size; // size of the array (n)
    private int targetBoardSpaceIndex; // current index in the array
    private final BoardSpaceDOM[] course; // array to hold the items (of type BoardSpaceDOM object)

    // Constructor to initialize the CircularArray with a specified size
    public BoardSpacesCourseDOM(int size) {
        this.size = size;
        this.targetBoardSpaceIndex = 0; // Default starting index is 0
        this.course = new BoardSpaceDOM[size]; // initializing the array of BoardSpaceDTO objects
        initializeTrack();
    }

    // Separate method to initialize the BoardSpaceDOM array. Important: index is
    // equal to spaceId!
    private void initializeTrack() {
        for (int id = 0; id < size; id++) {
            this.course[id] = new BoardSpaceDOM(id);
        }
    }

    // Method to check if FinishLineBoardSpace has camels
    public boolean hasFinishLineCamels() {
        return !this.getFinishlineBoardSpace().getCamelPack().getCamels().isEmpty();
    }

    // Method to move from a given starting position based on a given offset
    // (positive or negative)
    public BoardSpaceDOM getDestinationBoardspace(BoardSpaceDOM startBoardSpaceDOM, int offset) {
//        }
        if (startBoardSpaceDOM.getSpaceId() != 0 && startBoardSpaceDOM.getSpaceId() + offset <= 0) {
            targetBoardSpaceIndex = 0;


        } else if (startBoardSpaceDOM.getSpaceId() + offset > size - 1) {
            targetBoardSpaceIndex = 0;

        } else {
            targetBoardSpaceIndex = (startBoardSpaceDOM.getSpaceId() + offset);
            if (targetBoardSpaceIndex < 0) {
            targetBoardSpaceIndex += size;
            }

        }
        return this.course[targetBoardSpaceIndex];
    }


    public BoardSpaceDOM[] getCourse() {
        return this.course;
    }

    public BoardSpaceDOM getFinishlineBoardSpace() {
        if (this.course[0].getSpaceId() != 0) {
            throw new NoSuchElementException("Finishline BoardSpaceDOM with spaceId 0 not found at Index 0.");
        } else {
            return this.course[0];
        }
    }

    public BoardSpaceDOM getStartBoardSpace() {
        if (this.course[1].getSpaceId() != 1) {
            throw new NoSuchElementException("Startingline BoardSpaceDOM with spaceId 1 not found at Index 1.");
        } else {
            return this.course[1];
        }
    }

    public BoardSpaceDOM getBoardSpaceById(int spaceId) {
        if (this.course[spaceId].getSpaceId() != spaceId) {
            throw new NoSuchElementException("BoardSpaceDOM with spaceId not found at Index " + spaceId);
        } else {
            return this.course[spaceId];
        }
    }

    public void updateBoardSpaceCoursePosition(BoardSpaceDOM newBoardSpaceDOM) {
        // check if the spaceId of new BoardSpaceDOM Reference is matching the spaceId of the previous
        // BoardSpaceDOM object at the identical index
        if (isSpaceIdMatchingSpaceIdAtIndex(newBoardSpaceDOM)) {
            this.course[newBoardSpaceDOM.getSpaceId()] = newBoardSpaceDOM;
        } else {
            throw new IllegalStateException("ID does not match to BoardSpaceDOM ID at index: spaceId is " + newBoardSpaceDOM.getSpaceId() + " BoardSpaceDOM ID at index: " +getBoardSpaceById(newBoardSpaceDOM.getSpaceId()).getSpaceId());
        }
        
    }

    public boolean isSpaceIdMatchingSpaceIdAtIndex(BoardSpaceDOM newBoardSpaceDOM) {
        for (int i = 0; i == newBoardSpaceDOM.getSpaceId(); i++) {
            // casually checking all matchings of boardspace ids to their indexes
            // up to the newBoardSpaceDOM id to ensure consistency.
            if (this.course[i].getSpaceId() != i) {
                throw new IllegalStateException("Mismatch of BoardSpaceDOM ID: " + this.course[i].getSpaceId() + ", at course index: " + i);
            }
            // actually checking the Matching of newBoardSpaceDOM id, index and id of boardspace at index.
            if (i == newBoardSpaceDOM.getSpaceId()) {
                return this.course[i].getSpaceId() == newBoardSpaceDOM.getSpaceId();
            }
    }
    throw new IllegalArgumentException("No matching found for: " + newBoardSpaceDOM.getSpaceId());
}

    public static void main(String[] args) {
        // Size of the circular array
        int size = 5;

        // Create a course object with the specified size
        BoardSpacesCourseDOM course = new BoardSpacesCourseDOM(size);

        // Optionally, print the items at each index to confirm they were initialized
        // correctly
        for (int i = 0; i < size; i++) {
            BoardSpaceDOM item = course.getBoardSpaceById(i);
            System.out.println("Item at index " + i + ": " + item);
        }

        // Example of moving within the course
        System.out.println("space1");
        int offset = 3;
        BoardSpaceDOM space1 = course.getDestinationBoardspace(course.getBoardSpaceById(0), offset); // Move from index 0 by 3 positions
        System.out.println("Test a forward camel initialization roll. Move by offset of " + offset + " reaching Boardspace " + space1.getSpaceId());

        System.out.println("space2");
        offset = -2;
        BoardSpaceDOM space2 = course.getDestinationBoardspace(course.getBoardSpaceById(0), offset); // Move from index 0 by -2 position
        System.out.println("Test a backward camel initialization roll. Move by offset of " + offset + " reaching Boardspace " + space2.getSpaceId());

        System.out.println("space3");
        offset = 2;
        BoardSpaceDOM space3 = course.getDestinationBoardspace(course.getBoardSpaceById(2), offset); // Move from index 2 by -2 position, crossing finish line by 1 step
        System.out.println("Test a regular forward camel roll. Move by offset of " + offset + " reaching Boardspace " + space3.getSpaceId());

        System.out.println("space4");
        offset = -2;
        BoardSpaceDOM space4 = course.getDestinationBoardspace(course.getBoardSpaceById(3), offset); // Move from index -2 by 3 position, crossing finish line by 1 step
        System.out.println("Test a regular backward camel roll. Move by offset of " + offset + " reaching Boardspace " + space4.getSpaceId());

        System.out.println("space5");
        offset = -2;
        BoardSpaceDOM space5 = course.getDestinationBoardspace(course.getBoardSpaceById(2), offset); // Move from index 2 by -2 position, landing on finishline
        System.out.println("Test a winning backward camel, crossing finish line by offset of " + offset + " reaching Boardspace " + space5.getSpaceId());

        System.out.println("space6");
        offset = 4;
        BoardSpaceDOM space6 = course.getDestinationBoardspace(course.getBoardSpaceById(1), offset); // Move from index 1 by 4 position, landing on finish line
        System.out.println("Test a winning forward camel, crossing finish line by offset of " + offset + " reaching Boardspace " + space6.getSpaceId());



    }

}