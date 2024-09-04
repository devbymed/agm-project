-- Insertion des données de test pour les membres
INSERT INTO members (member_number, type, company_name, address_1, address_2, city, phone_1, phone_2, membership_date, workforce, title, dbr_trimester, dbr_year, dtr_trimester, dtr_year, created_at)
VALUES
-- Adhérents de type 1
('10000028', '1', 'Cyberdyne Systems', '1 Silicon Valley St', '41667 Imelda Meadows', 'San Francisco', '555-4001', '555-4002', '2019-01-10', 150, 'CEO', 1, 2024, 4, 2024, CURRENT_TIMESTAMP),
('10000029', '1', 'Weyland-Yutani', '2 Weyland St', '941 Cletus Shore', 'New York', '555-4003', '555-4004', '2020-02-20', 60, 'Manager', 2, 2023, 3, 2023, CURRENT_TIMESTAMP),
('10000030', '1', 'Blue Sun Corp', '3 Serenity Ln', '81557 Delpha Streets', 'Los Angeles', '555-4005', '555-4006', '2018-03-30', 55, 'Director', 3, 2022, 1, 2023, CURRENT_TIMESTAMP),

-- Adhérents de type 2
('10000031', '2', 'Stark Industries', '4 Stark Tower', '733 Stehr Junctions', 'New York', '555-4010', '555-4011', '2021-05-15', 140, 'Engineer', 2, 2024, 4, 2024, CURRENT_TIMESTAMP),
('10000032', '2', 'Wayne Enterprises', '5 Gotham St', '9225 Verlie Parkway', 'Gotham City', '555-4012', '555-4013', '2019-07-25', 50, 'Director', 3, 2023, 2, 2023, CURRENT_TIMESTAMP),

-- Adhérents de type 3
('10000033', '3', 'Oscorp', '6 Osborn St', '5527 Koss Glens', 'New York', '555-4020', '555-4021', '2018-08-05', 49, 'Scientist', 1, 2024, 4, 2024, CURRENT_TIMESTAMP),
('10000034', '3', 'InGen Corp', '7 Isla Nublar', '97304 Joan Inlet', 'Costa Rica', '555-4022', '555-4023', '2020-09-10', 95, 'Manager', 4, 2023, 3, 2023, CURRENT_TIMESTAMP),

-- Adhérents de type 4
('10000035', '4', 'Massive Dynamic', '8 Fringe St', '772 Rogers Branch', 'Boston', '555-4030', '555-4031', '2017-10-20', 135, 'Chief Scientist', 2, 2024, 4, 2024, CURRENT_TIMESTAMP),
('10000036', '4', 'OCP', '9 Robo Ave', '74824 Jacobson Ramp', 'Detroit', '555-4032', '555-4033', '2021-11-15', 70, 'Engineer', 1, 2023, 4, 2022, CURRENT_TIMESTAMP),
('10000037', '4', 'Aperture Science', '10 Portal St', '566 Tristian Course', 'Portland', '555-4034', '555-4035', '2019-12-01', 40, 'Scientist', 3, 2022, 3, 2023, CURRENT_TIMESTAMP);