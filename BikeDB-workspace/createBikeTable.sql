-- create schema
CREATE SCHEMA `bikedb` ;


-- create tables
CREATE TABLE `bikedb`.`categories` (
	`category_id` INT AUTO_INCREMENT,
	`category_name` VARCHAR (255) NOT NULL,
    PRIMARY KEY (`category_id`)
);


CREATE TABLE `bikedb`.`brands` (
	`brand_id` INT AUTO_INCREMENT,
	`brand_name` VARCHAR (255) NOT NULL,
    PRIMARY KEY (`brand_id`)
);


CREATE TABLE `bikedb`.`products` (
	`product_id` INT AUTO_INCREMENT,
	`product_name` VARCHAR (255) NOT NULL,
	`brand_id` INT NOT NULL,
	`category_id` INT NOT NULL,
	`model_year` SMALLINT NOT NULL,
	`list_price` DECIMAL (10, 2) NOT NULL,
    PRIMARY KEY (`product_id`),
	FOREIGN KEY (`category_id`) REFERENCES `bikedb`.categories (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`brand_id`) REFERENCES `bikedb`.brands (`brand_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FULLTEXT (`product_name`)
);


CREATE TABLE `bikedb`.`creditcards` (
	`id` VARCHAR(20) NOT NULL,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `expiration` DATE NOT NULL,
    PRIMARY KEY (`id`));


CREATE TABLE `bikedb`.`customers` (
	`customer_id` INT AUTO_INCREMENT,
	`first_name` VARCHAR (255) NOT NULL,
	`last_name` VARCHAR (255) NOT NULL,
	`email` VARCHAR (255) NOT NULL,
	`address` VARCHAR (255),
    `cc_id` VARCHAR(20) NOT NULL,
	`password` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`customer_id`),
    FOREIGN KEY (`cc_id`) REFERENCES `bikedb`.creditcards (`id`)
);


CREATE TABLE `bikedb`.`stores` (
	`store_id` INT AUTO_INCREMENT,
	`store_name` VARCHAR (255) NOT NULL,
	`phone` VARCHAR (25),
	`email` VARCHAR (255),
	`address` VARCHAR (255),
    PRIMARY KEY (`store_id`)
);


CREATE TABLE `bikedb`.`staffs` (
	`staff_id` INT AUTO_INCREMENT,
	`first_name` VARCHAR (50) NOT NULL,
	`last_name` VARCHAR (50) NOT NULL,
	`email` VARCHAR (255) NOT NULL,
	`password` VARCHAR (25) NOT NULL,
	`active` TINYINT NOT NULL,
	`store_id` INT NOT NULL,
	`manager_id` INT,
    PRIMARY KEY (`staff_id`),
	FOREIGN KEY (`store_id`) REFERENCES `bikedb`.stores (`store_id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`manager_id`) REFERENCES `bikedb`.staffs (`staff_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


CREATE TABLE `bikedb`.`orders` (
	`order_id` INT AUTO_INCREMENT,
	`customer_id` INT,
	`order_status` TINYINT NOT NULL,
	-- Order status: 1 = Pending; 2 = Processing; 3 = Rejected; 4 = Completed
	`order_date` DATE NOT NULL,
	`required_date` DATE NOT NULL,
	`shipped_date` DATE,
	`store_id` INT,
	`staff_id` INT,
    PRIMARY KEY (`order_id`),
	FOREIGN KEY (`customer_id`) REFERENCES `bikedb`.customers (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE `bikedb`.`order_items` (
	`order_id` INT,
	`item_id` INT,
	`product_id` INT NOT NULL,
	`quantity` INT NOT NULL,
	`list_price` DECIMAL (10, 2) NOT NULL,
	`discount` DECIMAL (4, 2) NOT NULL DEFAULT 0,
	PRIMARY KEY (`order_id`, `item_id`),
	FOREIGN KEY (`order_id`) REFERENCES `bikedb`.orders (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`product_id`) REFERENCES `bikedb`.products (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE `bikedb`.`stocks` (
	`store_id` INT,
	`product_id` INT,
	`quantity` INT,
	PRIMARY KEY (`store_id`, `product_id`),
	FOREIGN KEY (`store_id`) REFERENCES `bikedb`.stores (`store_id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`product_id`) REFERENCES `bikedb`.products (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
    
    
CREATE TABLE `bikedb`.`ratings` (
	`bike_id` INT NOT NULL,
    `rating` FLOAT NOT NULL,
    `numVotes` INT NOT NULL,
    FOREIGN KEY (`bike_id`) REFERENCES `bikedb`.products (`product_id`));