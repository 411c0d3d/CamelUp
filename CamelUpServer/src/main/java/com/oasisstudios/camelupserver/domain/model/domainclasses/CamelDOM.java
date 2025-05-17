package com.oasisstudios.camelupserver.domain.model.domainclasses;

public class CamelDOM {
    private int id;
    private String color;
    private Direction direction;

    public enum Direction {
        BACKWARDS(-1),
        FORWARDS(1);

        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public int getModifier() {
            return value;
        }
    }

    public CamelDOM(int id, String color, Direction direction) {
        this.id = id;
        this.color = color;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // Method to check if the space type matches a given enum value
    public boolean isCamelType(Direction type) {
        return this.direction == type;
    }

}

