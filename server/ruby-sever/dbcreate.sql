CREATE DATABASE IF NOT EXISTS powerteam;

CREATE TABLE IF NOT EXISTS powerteam.clientlogs (
	log_id INT NOT NULL AUTO_INCREMENT,
	hash VARCHAR(10) NOT NULL,
	push_time BIGINT NOT NULL,
	user_id INT NOT NULL,
	PRIMARY KEY (log_id)
);

CREATE UNIQUE INDEX uniq_hash ON powerteam.clientlogs (hash);

CREATE TABLE IF NOT EXISTS powerteam.pluginlogs (
	log_id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
	test_result VARCHAR(15) NOT NULL,
	start_time BIGINT,
	end_time BIGINT,
	PRIMARY KEY (log_id)
);

CREATE TABLE IF NOT EXISTS powerteam.users (
	user_id INT NOT NULL AUTO_INCREMENT,
	user_name VARCHAR(40) NOT NULL,
	PRIMARY KEY (user_id)
);

CREATE UNIQUE INDEX uniq_name ON powerteam.users (user_name);

INSERT INTO powerteam.users(user_name) VALUES ('Markiyan Matviyiv');
INSERT INTO powerteam.users(user_name) VALUES ('osklyarenko');