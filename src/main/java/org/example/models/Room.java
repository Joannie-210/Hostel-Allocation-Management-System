package org.example.models;

public class Room {
    private int id;
    private String roomNumber;
    private int hostelId;
    private String type;
    private boolean isAvailable;

    public Room() {}

    public Room(int id, String roomNumber, int hostelId, String type, boolean isAvailable) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.hostelId = hostelId;
        this.type = type;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getHostelId() { return hostelId; }
    public void setHostelId(int hostelId) { this.hostelId = hostelId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
