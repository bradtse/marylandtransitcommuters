CREATE TABLE `trips` (
    route_id VARCHAR(20),
	service_id VARCHAR(20),
	trip_id VARCHAR(20) PRIMARY KEY,
	trip_headsign VARCHAR(255),
	trip_short_name VARCHAR(255),
	direction_id TINYINT(1),
	block_id INT(11),
	shape_id VARCHAR(50),
	KEY `route_id` (route_id),
	KEY `service_id` (service_id),
	KEY `direction_id` (direction_id),
	KEY `block_id` (block_id),
	KEY `shape_id` (shape_id)
);
