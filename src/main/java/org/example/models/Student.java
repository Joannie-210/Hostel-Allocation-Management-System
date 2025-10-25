package org.example.models;

public class Student {
    private Integer id;
    private String regNo;
    private String name;
    private String email;
    private String gender;
    private String level;
    private String phone;
    private String department;
    private String role;
    private Integer roomId;
    private String password;  // ✅ Add this

    // Constructor including password
    public Student(Integer id, String regNo, String name, String email, String gender,
                   String level, String phone, String department, String role,
                   Integer roomId, String password) {
        this.id = id;
        this.regNo = regNo;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.level = level;
        this.phone = phone;
        this.department = department;
        this.role = role;
        this.roomId = roomId;
        this.password = password;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public String getPassword() { return password; }   // ✅ Add getter
    public void setPassword(String password) { this.password = password; } // ✅ Add setter
}
