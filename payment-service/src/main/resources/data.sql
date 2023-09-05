-- Drop the table if it exists
DROP TABLE IF EXISTS user_balance;

-- Create the user_balance table with appropriate columns and data types
CREATE TABLE user_balance
(
    user_id INT PRIMARY KEY,
    balance INT
);

-- Insert data into the user_balance table
INSERT INTO user_balance (user_id, balance)
VALUES (1, 1000),
       (2, 1000),
       (3, 1000);

-- -- Drop the table if it exists
-- DROP TABLE IF EXISTS user_balance;
--
-- -- Create the user_balance table with appropriate columns and data types
-- CREATE TABLE user_balance
-- (
--     user_id INT PRIMARY KEY,
--     balance INT
--     -- Add other columns as needed
-- );
--
-- -- Load data from the CSV file into the user_balance table
-- COPY user_balance FROM 'classpath:user_balance.csv' DELIMITER ',' CSV HEADER;