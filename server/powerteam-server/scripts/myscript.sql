CREATE TABLE SERVER.pluginlog (log_id INTEGER NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    test_result VARCHAR(15) NOT NULL,
    user_name VARCHAR(40) NOT NULL,
    req_type VARCHAR(15) NOT NULL,
    PRIMARY KEY (log_id)); 