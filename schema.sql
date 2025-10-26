PRAGMA foreign_keys = ON;

ALTER TABLE students ADD COLUMN phone TEXT;
ALTER TABLE students ADD COLUMN department TEXT;
ALTER TABLE rooms ADD COLUMN capacity INTEGER DEFAULT 1;
ALTER TABLE rooms ADD COLUMN occupants INTEGER DEFAULT 0;

-- Drop existing tables safely (reverse dependency order)
DROP VIEW IF EXISTS hostel_dashboard;
DROP TABLE IF EXISTS allocations;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS hostels;

------------------------------------------------------------
-- 1. HOSTELS TABLE
------------------------------------------------------------
CREATE TABLE IF NOT EXISTS hostels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    gender TEXT CHECK(gender IN ('Male', 'Female', 'Mixed')) DEFAULT 'Mixed',
    total_rooms INTEGER DEFAULT 0,
    total_capacity INTEGER DEFAULT 0,
    occupied_beds INTEGER DEFAULT 0,
    warden_name TEXT,
    contact_number TEXT,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER IF NOT EXISTS trg_hostels_updated_at
AFTER UPDATE ON hostels
FOR EACH ROW
BEGIN
    UPDATE hostels
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.id;
END;

------------------------------------------------------------
-- 2. ROOMS TABLE
------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rooms (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    hostel_id INTEGER NOT NULL,
    room_number TEXT NOT NULL,
    capacity INTEGER NOT NULL DEFAULT 1,
    occupants INTEGER DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(hostel_id) REFERENCES hostels(id) ON DELETE CASCADE
);

CREATE TRIGGER IF NOT EXISTS trg_rooms_after_insert
AFTER INSERT ON rooms
FOR EACH ROW
BEGIN
    UPDATE hostels
    SET
        total_rooms = total_rooms + 1,
        total_capacity = total_capacity + NEW.capacity
    WHERE id = NEW.hostel_id;
END;

CREATE TRIGGER IF NOT EXISTS trg_rooms_after_delete
AFTER DELETE ON rooms
FOR EACH ROW
BEGIN
    UPDATE hostels
    SET
        total_rooms = total_rooms - 1,
        total_capacity = total_capacity - OLD.capacity
    WHERE id = OLD.hostel_id;
END;

CREATE INDEX IF NOT EXISTS idx_rooms_hostel ON rooms(hostel_id);

------------------------------------------------------------
-- 3. STUDENTS TABLE (updated with phone and department)
------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reg_no TEXT UNIQUE,
    name TEXT NOT NULL,
    email TEXT UNIQUE,
    gender TEXT CHECK(gender IN ('Male', 'Female')) NOT NULL,
    level TEXT,
    password TEXT NOT NULL,
    phone TEXT,
    department TEXT,
    room_id INTEGER,
    role TEXT NOT NULL DEFAULT 'Student',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(room_id) REFERENCES rooms(id) ON DELETE SET NULL
);

CREATE TRIGGER IF NOT EXISTS trg_students_regno
AFTER INSERT ON students
FOR EACH ROW
BEGIN
    UPDATE students
    SET reg_no = 'STU' || printf('%04d', NEW.id)
    WHERE id = NEW.id;
END;

-- Student moved OUT
CREATE TRIGGER IF NOT EXISTS trg_student_moved_out
AFTER UPDATE OF room_id ON students
WHEN OLD.room_id IS NOT NULL AND NEW.room_id IS NULL
BEGIN
    UPDATE hostels
    SET occupied_beds = occupied_beds - 1
    WHERE id = (SELECT hostel_id FROM rooms WHERE id = OLD.room_id);
END;

-- Student moved IN
CREATE TRIGGER IF NOT EXISTS trg_student_moved_in
AFTER UPDATE OF room_id ON students
WHEN NEW.room_id IS NOT NULL
BEGIN
    UPDATE hostels
    SET occupied_beds = occupied_beds + 1
    WHERE id = (SELECT hostel_id FROM rooms WHERE id = NEW.room_id);
END;

CREATE TRIGGER IF NOT EXISTS trg_students_after_delete
AFTER DELETE ON students
WHEN OLD.room_id IS NOT NULL
BEGIN
    UPDATE hostels
    SET occupied_beds = occupied_beds - 1
    WHERE id = (SELECT hostel_id FROM rooms WHERE id = OLD.room_id);
END;

CREATE INDEX IF NOT EXISTS idx_students_room ON students(room_id);

------------------------------------------------------------
-- 4. ALLOCATIONS TABLE
------------------------------------------------------------
CREATE TABLE IF NOT EXISTS allocations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    room_id INTEGER NOT NULL,
    date_allocated DATETIME DEFAULT CURRENT_TIMESTAMP,
    allocated_by TEXT,
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY(room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_alloc_student ON allocations(student_id);
CREATE INDEX IF NOT EXISTS idx_alloc_room ON allocations(room_id);

------------------------------------------------------------
-- 5. DASHBOARD VIEW
------------------------------------------------------------
CREATE VIEW IF NOT EXISTS hostel_dashboard AS
SELECT
    h.id AS hostel_id,
    h.name AS hostel_name,
    h.gender,
    h.total_rooms,
    h.total_capacity,
    h.occupied_beds,
    (h.total_capacity - h.occupied_beds) AS available_beds,
    CASE
        WHEN h.total_capacity > 0
        THEN ROUND((h.occupied_beds * 100.0) / h.total_capacity, 2)
        ELSE 0
    END AS occupancy_rate,
    h.warden_name,
    h.contact_number,
    h.created_at,
    h.updated_at
FROM hostels h
ORDER BY h.name ASC;
