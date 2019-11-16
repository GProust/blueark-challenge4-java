DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS NOTIFICATION_USERS;

CREATE TABLE USERS (
  USER_ID VARCHAR(50) PRIMARY KEY,
  DEPARTURE_DATE DATE,
  RETURN_DATE DATE,
  RESIDENCE_TYPE VARCHAR(50)
);

CREATE TABLE NOTIFICATION_USERS (
    NOTIFICATION_ID INT AUTO_INCREMENT PRIMARY KEY,
    USER_ID VARCHAR(50),
    NOTIFICATION_TYPE VARCHAR(50)
);

INSERT INTO USERS (USER_ID, DEPARTURE_DATE, RETURN_DATE, RESIDENCE_TYPE) VALUES
('32336_16857', null, null, 'PRIMARY'),
('32740', null, null, 'PRIMARY'),
('34199', null, null, 'PRIMARY'),
('45088_46311', null, null, 'PRIMARY'),
('45607_44239', null, null, 'PRIMARY'),
('51679_12620', null, null, 'PRIMARY'),
('57130', null, null, 'PRIMARY'),
('55677_55733', null, null, 'PRIMARY'),
('55677_55734', null, null, 'PRIMARY'),
('55677_55735', null, null, 'PRIMARY');