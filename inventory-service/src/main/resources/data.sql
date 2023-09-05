-- Drop the table if it exists
DROP TABLE IF EXISTS order_inventory;

-- Create the order_inventory table with appropriate columns and data types
CREATE TABLE order_inventory
(
    product_id          INT PRIMARY KEY,
    available_inventory INT
);

-- Insert data into the order_inventory table
INSERT INTO order_inventory (product_id, available_inventory)
VALUES (1, 5),
       (2, 5),
       (3, 5);


-- DROP TABLE IF EXISTS order_inventory;
-- CREATE TABLE order_inventory AS SELECT * FROM CSVREAD('classpath:order_inventory.csv');