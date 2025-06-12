CREATE TABLE users (
                       id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       name     VARCHAR(255) NOT NULL,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
