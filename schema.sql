PRAGMA foreign_keys = ON;

-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS allocations;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS hostels;

-- Hostels table
CREATE TABLE IF NOT EXISTS hostels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    gender TEXT CHECK(gender IN ('Male', 'Female', 'Mixed')) DEFAULT 'Mixed',
    total_rooms INTEGER DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    hostel_id INTEGER NOT NULL,
    room_number TEXT NOT NULL,
    capacity INTEGER NOT NULL DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(hostel_id) REFERENCES hostels(id) ON DELETE CASCADE
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reg_no TEXT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    email TEXT UNIQUE,
    gender TEXT CHECK(gender IN ('Male', 'Female')) NOT NULL,
    level TEXT,
    password TEXT NOT NULL,
    room_id INTEGER,
    role TEXT NOT NULL DEFAULT 'Student',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(room_id) REFERENCES rooms(id) ON DELETE SET NULL
);

-- Allocations table
CREATE TABLE IF NOT EXISTS allocations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    room_id INTEGER NOT NULL,
    date_allocated DATETIME DEFAULT CURRENT_TIMESTAMP,
    allocated_by TEXT,
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY(room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Auto-generate student registration numbers after insert
CREATE TRIGGER IF NOT EXISTS trg_students_regno
AFTER INSERT ON students
FOR EACH ROW
BEGIN
    UPDATE students
    SET reg_no = 'STU' || printf('%04d', NEW.id)
    WHERE id = NEW.id;
END;

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_rooms_hostel ON rooms(hostel_id);
CREATE INDEX IF NOT EXISTS idx_students_room ON students(room_id);
CREATE INDEX IF NOT EXISTS idx_alloc_student ON allocations(student_id);
