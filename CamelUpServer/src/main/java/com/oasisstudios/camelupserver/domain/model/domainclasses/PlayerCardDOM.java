package com.oasisstudios.camelupserver.domain.model.domainclasses;

public class PlayerCardDOM {
    private int playerId;
    private SpacesMoved spacesMoved;

    public enum SpacesMoved {
        BACKWARDS(-1),
        FORWARDS(1);

        private final int value;

        SpacesMoved(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SpacesMoved fromValue(int value) {
            if (value == -1) {
                return BACKWARDS;
            } else if (value == 1) {
                return FORWARDS;
            }
            throw new IllegalArgumentException("Invalid value for SpacesMoved: " + value);
        }
    }

    public PlayerCardDOM(int playerId, SpacesMoved spacesMoved) {
        this.playerId = playerId;
        this.spacesMoved = spacesMoved;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public SpacesMoved getSpacesMoved() {
        return this.spacesMoved;
    }

    public void setSpacesMoved(SpacesMoved spacesMoved) {
        this.spacesMoved = spacesMoved;
    }

    // Method to check if the space type matches a given enum value
    public boolean isCardType(SpacesMoved type) {
        return this.spacesMoved == type;
    }

}

