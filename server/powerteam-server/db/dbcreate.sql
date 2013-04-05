CREATE DATABASE powerteam;

CREATE TABLE powerteam.clientlogs (
	log_id INT NOT NULL AUTO_INCREMENT,
	hash VARCHAR(10) NOT NULL,
	push_time BIGINT NOT NULL,
	user_id INT NOT NULL,
	PRIMARY KEY (log_id)
);

ALTER TABLE powerteam.clientlogs ADD UNIQUE (hash);

CREATE TABLE powerteam.pluginlogs (
	log_id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
	test_result VARCHAR(15) NOT NULL,
	start_time BIGINT,
	end_time BIGINT,
	PRIMARY KEY (log_id)
);

CREATE TABLE powerteam.users (
	user_id INT NOT NULL AUTO_INCREMENT,
	user_name VARCHAR(40) NOT NULL,
	PRIMARY KEY (user_id)
);

INSERT INTO powerteam.users(user_name) VALUES ('Markiyan Matviyiv');
INSERT INTO powerteam.users(user_name) VALUES ('Babichok');