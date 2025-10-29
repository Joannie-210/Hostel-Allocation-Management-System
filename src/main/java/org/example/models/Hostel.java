package org.example.models;

public class Hostel {
    private int id;
    private String name;
    private String gender;
    private int totalRooms;
    private int totalCapacity;
    private int occupiedBeds;
    private String wardenName;
    private String contactNumber;
    private String description;
    private int availableBeds;
    private double occupancyRate;

    // Default constructor
    public Hostel() {}

    // Constructor for full hostel details (used in adding/editing hostels)
    public Hostel(String name, String gender, String wardenName, String contactNumber, String description) {
        this.name = name;
        this.gender = gender;
        this.wardenName = wardenName;
        this.contactNumber = contactNumber;
        this.description = description;
    }

    // ✅ Constructor for report data (used in HostelDAO.getHostelReports)
    public Hostel(int id, String name, int totalCapacity, int totalRooms, int totalOccupants, int availableRooms) {
        this.id = id;
        this.name = name;
        this.totalCapacity = totalCapacity;
        this.totalRooms = totalRooms;
        this.occupiedBeds = totalOccupants;
        this.availableBeds = availableRooms;
        this.occupancyRate = totalCapacity > 0 ? (totalOccupants * 100.0 / totalCapacity) : 0.0;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }

    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }

    public int getOccupiedBeds() { return occupiedBeds; }
    public void setOccupiedBeds(int occupiedBeds) { this.occupiedBeds = occupiedBeds; }

    public String getWardenName() { return wardenName; }
    public void setWardenName(String wardenName) { this.wardenName = wardenName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }

    public double getOccupancyRate() { return occupancyRate; }
    public void setOccupancyRate(double occupancyRate) { this.occupancyRate = occupancyRate; }
}
