-- coffee

insert into roast_degree (name) values
('Light'),
('Medium'),
('Dark');

insert into coffee (name, roast_degree_id) values
('Coffee1', '1'),
('Coffee2', '2'),
('Coffee3', '3'),
('Coffee4', '1'),
('Coffee5', '2'),
('Coffee6', '3');

-- customer

insert into address (city, street, house_number, apartment_number) values
('City11', 'Street11', '11', '11'),
('City12', 'Street12', '12', '12'),
('City21', 'Street21', '21', '21'),
('City22', 'Street22', '22', '22');

insert into customer (login) values
('customer1'),
('customer2');

insert into customer_address (customer_id, address_id) values
('1', '1'),
('1', '2'),
('2', '3'),
('2', '4');

-- order

insert into package_size (weight) values
('250'),
('1000');