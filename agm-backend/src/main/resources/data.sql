INSERT INTO members (member_number, type, company_name, address_1, address_2, city, phone_1, phone_2, membership_date, workforce, title, dbr_trimester, dbr_year, dtr_trimester, dtr_year, created_at)
VALUES
-- Adhérents de type 1
('10000001', '1', 'Acme Corp', '123 Main St', 'Suite 500', 'New York', '555-1001', '555-1002', '2020-01-01', 150, 'CEO', 1, 2024, 4, 2023, CURRENT_TIMESTAMP),
('10000002', '1', 'Globex Inc', '456 Elm St', '', 'Los Angeles', '555-1003', '555-1004', '2019-06-15', 40, 'Manager', 2, 2023, 1, 2023, CURRENT_TIMESTAMP),
('10000003', '1', 'Soylent Corp', '789 Oak St', 'Apt 12', 'Chicago', '555-1005', '555-1006', '2018-03-20', 120, 'CTO', 3, 2022, 2, 2022, CURRENT_TIMESTAMP),

-- Adhérents de type 2
('10000004', '2', 'Wayne Enterprises', '333 Cedar St', 'Suite 200', 'Gotham', '555-1011', '555-1012', '2021-02-14', 140, 'Director', 2, 2024, 4, 2023, CURRENT_TIMESTAMP),
('10000005', '2', 'Stark Industries', '444 Maple St', '', 'Boston', '555-1013', '555-1014', '2020-08-07', 100, 'Engineer', 3, 2022, 2, 2022, CURRENT_TIMESTAMP),
('10000006', '2', 'LexCorp', '666 Oak St', 'Suite 400', 'Metropolis', '555-1017', '555-1018', '2018-05-23', 30, 'Analyst', 1, 2023, 4, 2022, CURRENT_TIMESTAMP),

-- Adhérents de type 3
('10000007', '3', 'Oscorp', '555 Walnut St', 'Apt 8B', 'Newark', '555-1015', '555-1016', '2019-11-19', 110, 'Scientist', 4, 2023, 4, 2023, CURRENT_TIMESTAMP),
('10000008', '3', 'Aperture Science', '1010 Redwood St', 'Floor 2', 'Portland', '555-1025', '555-1026', '2018-04-27', 85, 'Engineer', 1, 2023, 4, 2021, CURRENT_TIMESTAMP),

-- Adhérents de type 4
('10000009', '4', 'Massive Dynamic', '1515 Oak St', '', 'New York', '555-1035', '555-1036', '2019-01-12', 135, 'Chief Scientist', 2, 2024, 4, 2023, CURRENT_TIMESTAMP),
('10000010', '4', 'Blue Sun Corporation', '1313 Cherry St', 'Apt 3C', 'Houston', '555-1031', '555-1032', '2021-05-05', 160, 'Director', 3, 2022, 2, 2022, CURRENT_TIMESTAMP);
