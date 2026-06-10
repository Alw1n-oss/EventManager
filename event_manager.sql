CREATE DATABASE IF NOT EXISTS event_manager;

USE event_manager;

CREATE TABLE UserRole (
    idUserRole INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE User (
    idUser INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100),
    idUserRole INT NOT NULL,
    FOREIGN KEY (idUserRole) REFERENCES UserRole(idUserRole)
);

CREATE TABLE Venue (
    idVenue INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    capacity INT
);

CREATE TABLE Event (
    idEvent INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    eventDate DATETIME NOT NULL,
    idVenue INT,
    maxParticipants INT NOT NULL,
    currentParticipants INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    idOrganizer INT NOT NULL,
    version BIGINT DEFAULT 0,
    FOREIGN KEY (idVenue) REFERENCES Venue(idVenue),
    FOREIGN KEY (idOrganizer) REFERENCES User(idUser)
);

CREATE TABLE Registration (
    idRegistration INT AUTO_INCREMENT PRIMARY KEY,
    idUser INT NOT NULL,
    idEvent INT NOT NULL,
    registeredAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    FOREIGN KEY (idUser) REFERENCES User(idUser),
    FOREIGN KEY (idEvent) REFERENCES Event(idEvent),
    UNIQUE (idUser, idEvent)
);
