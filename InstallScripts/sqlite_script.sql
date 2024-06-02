CREATE TABLE IF NOT EXISTS "Cities" (
	"cityId"	TEXT,
	"cityName"	TEXT,
	"cityLatitude"	REAL,
	"cityLongitude"	REAL,
	PRIMARY KEY("CityId")
);

CREATE TABLE "Apartments" (
	"apartmentID"	INTEGER,
	"apartmentName"	TEXT,
	"apartmentDesc" TEXT,
	"apartmentPrice"	REAL,
	"apartmentAdress"	TEXT,
	"cityID"	TEXT,
	FOREIGN KEY("cityID") REFERENCES "Cities",
	PRIMARY KEY("apartmentID" AUTOINCREMENT)
);

CREATE TABLE "Apartments_temp" (
	"apartmentName"	TEXT,
	"apartmentDesc" TEXT,
	"apartmentPrice"	REAL,
	"apartmentAdress"	TEXT,
	"cityID"	TEXT
);

CREATE TABLE "Reservations" (
	"reservationID"	INTEGER,
	"reservationName"	TEXT,
	"reservationDateYear"	INTEGER,
	"reservationDateNoSem"	INTEGER,
	"apartmentID"	INTEGER,
	FOREIGN KEY("ApartmentID") REFERENCES "Apartments",
	PRIMARY KEY("reservationID" AUTOINCREMENT)
);

.mode csv
.import cities_list.csv Cities
.import apartments_list.csv Apartments_temp
INSERT INTO Apartments(apartmentName,apartmentDesc,apartmentPrice,apartmentAdress,cityID) SELECT * FROM Apartments_temp;
DROP TABLE "Apartments_temp" ;
vacuum;
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('Toto', '2024', '10', '21');
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('Ben', '2024', '10', '4');
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('Luc', '2024', '10', '20');
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('Jean', '2024', '50', '19');
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('Carl', '2024', '50', '7');
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('José', '2024', '24', '21');
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('Daniel', '2025', '10', '3');
INSERT INTO "Reservations" ("reservationName", "reservationDateYear", "reservationDateNoSem", "apartmentID") VALUES ('Daube', '2025', '3', '15');
.quit

