insert into roles (name) values
('CUSTOMER'),
('ROASTER');

insert into users (username, password) values
('customer1', '$2a$10$pocRoV8nSSy2Dg3Fcvic8OxvbpA5yQVQTDjRLI7A2XqCMnSz4.K9O'),
('customer2', '$2a$10$pocRoV8nSSy2Dg3Fcvic8OxvbpA5yQVQTDjRLI7A2XqCMnSz4.K9O'),
('roaster1', '$2a$10$pocRoV8nSSy2Dg3Fcvic8OxvbpA5yQVQTDjRLI7A2XqCMnSz4.K9O'),
('roaster2', '$2a$10$pocRoV8nSSy2Dg3Fcvic8OxvbpA5yQVQTDjRLI7A2XqCMnSz4.K9O');

insert into users_roles (user_id, role_id) values
('1', '1'),
('2', '1'),
('3', '2'),
('4', '2');
