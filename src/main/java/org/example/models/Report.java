package org.example.models;

public class Report {
    private int hostelId;
    private String hostelName;
    private int totalHostels;
    private int totalRooms;
    private int totalCapacity;
    private int occupiedBeds;
    private int availableBeds;

    // Getters and setters
    public int getHostelId() { return hostelId; }
    public void setHostelId(int hostelId) { this.hostelId = hostelId; }

    public String getHostelName() { return hostelName; }
    public void setHostelName(String hostelName) { this.hostelName = hostelName; }

    public int getTotalHostels() { return totalHostels; }
    public void setTotalHostels(int totalHostels) { this.totalHostels = totalHostels; }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }

    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }

    public int getOccupiedBeds() { return occupiedBeds; }
    public void setOccupiedBeds(int occupiedBeds) { this.occupiedBeds = occupiedBeds; }

    public int getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }
}
