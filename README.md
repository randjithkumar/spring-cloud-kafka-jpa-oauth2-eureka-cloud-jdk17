[Environment Used]

  1. Java JDK - 17
  2. MariaDB
  3. IDE - Spring Tool Suite 4.28.1.RELEASE

[Ports Used]

  1. MariaDB - 3307
  2. authentic - 9999
  3. account - 5551
  4. discovery (Netflix Eureka) - 8761
  5. gateway - 5559

[DB Changes]

-- Connect to MariaDB
mysql -u root -p

-- Create database if missing
CREATE DATABASE IF NOT EXISTS oauth2_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Switch to auth database  
USE oauth2_db;

-- Drop existing incomplete tables (if any)
DROP TABLE IF EXISTS group_members, group_authorities, groups, authorities, users;

-- Create COMPLETE Spring Security schema
CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL,
  authorities VARCHAR(255)  -- Single column for comma-separated authorities
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username),
  UNIQUE(username, authority)
);

CREATE TABLE groups (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  group_name VARCHAR(50) NOT NULL
);

CREATE TABLE group_authorities (
  group_id BIGINT NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (group_id) REFERENCES groups(id),
  UNIQUE(group_id, authority)
);

CREATE TABLE group_members (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  group_id BIGINT NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username),
  FOREIGN KEY (group_id) REFERENCES groups(id)
);
 

CREATE TABLE oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(200) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO oauth2_registered_client (
    id, client_id, client_id_issued_at, client_secret, client_name,
    client_authentication_methods, authorization_grant_types, scopes, 
    client_settings, token_settings
) VALUES (
    'account-client-id',
    'account-client',
    CURRENT_TIMESTAMP,
    '{noop}secret',
    'Account Client',
    'client_secret_basic',
    'client_credentials,authorization_code',
    'read,write',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":"","settings.client.require-authorization-consent":"true"}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.access-token-time-to-live":"PT1H","settings.token.authorization-code-time-to-live":"PT10M","settings.token.id-token-signature-algorithm":["org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256"]}'
);


-- Insert test user
INSERT INTO users (username, password, enabled) VALUES ('user', '{noop}password', true);
INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');

INSERT INTO users (username, password, enabled, authorities) 
VALUES ('user0', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, 'ROLE_USER,ROLE_READ');

Execute mvn clean install, Deploy and execute in the following Order:

1. Authentic
2. Account
3. Discovery
4. Gateway

Thank you,
Regards
Randjith
