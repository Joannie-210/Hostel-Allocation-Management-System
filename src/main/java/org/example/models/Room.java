package org.example.models;

public class Room {
    private int id;
    private String roomNumber;
    private int hostelId;
    private int capacity;
    private int occupants;
    private String createdAt;

    // --- Constructors ---
    public Room() {}

    public Room(int id, String roomNumber, int hostelId, int capacity, int occupants, String createdAt) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.hostelId = hostelId;
        this.capacity = capacity;
        this.occupants = occupants;
        this.createdAt = createdAt;
    }
    public boolean isAvailable() {
        return occupants < capacity;
    }

    // --- Getters and Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getHostelId() {
        return hostelId;
    }

    public void setHostelId(int hostelId) {
        this.hostelId = hostelId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupants() {
        return occupants;
    }

    public void setOccupants(int occupants) {
        this.occupants = occupants;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // --- Optional helper method ---
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", hostelId=" + hostelId +
                ", capacity=" + capacity +
                ", occupants=" + occupants +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
