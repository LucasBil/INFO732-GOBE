PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS University;
DROP TABLE IF EXISTS Profile;
DROP TABLE IF EXISTS Currency;
DROP TABLE IF EXISTS Advertisement;
DROP TABLE IF EXISTS TimeSlotStatus;
DROP TABLE IF EXISTS TimeSlot;
DROP TABLE IF EXISTS Transfer;
DROP TABLE IF EXISTS Message;
DROP TABLE IF EXISTS Good;
DROP TABLE IF EXISTS Alert;

-- ============================
-- Table: University
-- ============================
CREATE TABLE University (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255),
    city VARCHAR(255)
);

-- ============================
-- Table: Profile
-- ============================
CREATE TABLE Profile (
    id INTEGER PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    birthdate DATE,
    email VARCHAR(255),
    university INTEGER,
    FOREIGN KEY (university) REFERENCES University(id)
);

-- ============================
-- Table: Currency
-- ============================
CREATE TABLE Currency (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

-- ============================
-- Table: Advertisement
-- ============================
CREATE TABLE Advertisement (
    id INTEGER PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    holder INTEGER,
    price REAL,
    date DATE,
    expire DATE,
    university INTEGER,
    guarantee REAL,
    type VARCHAR(255),
    FOREIGN KEY (holder) REFERENCES Profile(id)
);

-- ============================
-- Table: TimeSlotStatus
-- ============================
CREATE TABLE TimeSlotStatus (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

-- ============================
-- Table: TimeSlot
-- ============================
CREATE TABLE TimeSlot (
    id INTEGER PRIMARY KEY,
    profile INTEGER,
    advertisement INTEGER,
    ammount REAL,
    date DATE,
    status INTEGER,
    FOREIGN KEY (profile) REFERENCES Profile(id),
    FOREIGN KEY (advertisement) REFERENCES Advertisement(id),
    FOREIGN KEY (status) REFERENCES TimeSlotStatus(id)
);

-- ============================
-- Table: Transfer
-- ============================
CREATE TABLE Transfer (
    id INTEGER PRIMARY KEY,
    sender INTEGER,
    timeslot INTEGER,
    date DATE,
    currency INTEGER,
    FOREIGN KEY (sender) REFERENCES Profile(id),
    FOREIGN KEY (timeslot) REFERENCES TimeSlot(id),
    FOREIGN KEY (currency) REFERENCES Currency(id)
);

-- ============================
-- Table: Message
-- ============================
CREATE TABLE Message (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sender INTEGER,
    receiver INTEGER,
    text TEXT,
    FOREIGN KEY (sender) REFERENCES Profile(id),
    FOREIGN KEY (receiver) REFERENCES Profile(id)
);

-- ============================
-- Table: Good
-- ============================
CREATE TABLE Good (
    id INTEGER PRIMARY KEY,
    advertisement INTEGER,
    status VARCHAR(255),
    state VARCHAR(255),
    FOREIGN KEY (advertisement) REFERENCES Advertisement(id)
);

-- ============================
-- Table: Alert
-- ============================
CREATE TABLE Alert (
    id INTEGER PRIMARY KEY,
    profile INTEGER,
    keyword VARCHAR(255),
    university INTEGER,
    FOREIGN KEY (profile) REFERENCES Profile(id),
    FOREIGN KEY (university) REFERENCES University(id)
);
