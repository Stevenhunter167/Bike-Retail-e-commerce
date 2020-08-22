use bikedb;

DROP PROCEDURE IF EXISTS add_bike;
DELIMITER $$
CREATE PROCEDURE add_bike(
IN pname varchar(255), 
IN s_name varchar(255), 
IN s_phone varchar(25), 
IN s_email varchar(255), 
IN s_addr varchar(255), 
IN b_name varchar(255), 
IN c_name varchar(255), 
IN myear smallint,
IN price decimal(10,2),
IN p_rating float,
IN qty int)
BEGIN
	if (exists (
		select * from products, brands, categories 
		where products.product_name = pname and brands.brand_id = products.brand_id and categories.category_id = products.category_id)) then
        select "product already exists" as answer;
	elseif (s_name = '' or s_phone = '' or s_addr= '' or s_email='') then
		select "please specify store information" as answer;
	else
		set @bid := (select brand_id from bikedb.brands where brand_name = b_name);
		if (not exists (select brand_id from bikedb.brands where brand_name = b_name)) then
			select @bid as answer;
			INSERT INTO bikedb.brands(brand_name) value(b_name);
			set @bid := (select brand_id from bikedb.brands where brand_name = b_name);
		end if;
		
		set @cid := (select category_id from bikedb.categories where category_name = c_name);
		if (not exists (select category_id from bikedb.categories where category_name = c_name)) then
			INSERT INTO bikedb.categories(category_name) value(c_name);
			set @cid := (select category_id from bikedb.categories where category_name = c_name);
		end if;
		
		set @sid := (select store_id 
					 from stores
					 where store_name = s_name and email = s_email and address = s_addr and phone = s_phone);
					 
		if (not exists (select store_name from stores where store_name = s_name and email = s_email and address = s_addr and phone = s_phone)) then
			INSERT INTO bikedb.stores(store_name, phone, email, address) value(s_name, s_phone, s_email, s_addr);
			set @sid := (select store_id 
					 from stores
					 where store_name = s_name and email = s_email and address = s_addr and phone = s_phone);
		end if;
		
		INSERT INTO bikedb.products(product_name, brand_id, category_id, model_year, list_price) value(pname, @bid, @cid, myear, price);
		set @pid := (select product_id from bikedb.products where product_name = pname and brand_id = @bid and category_id = @cid and model_year = myear and list_price = price);
        
        INSERT INTO bikedb.ratings(bike_id, rating, numVotes) VALUE(@pid, p_rating, 4);
		INSERT INTO bikedb.stocks(store_id, product_id, quantity) value(@sid, @pid, qty);
		select "bike insertion successful" as answer;
	end if;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS add_store;
DELIMITER $$
CREATE PROCEDURE add_store(IN s_name varchar(255), IN s_phone varchar(25), IN s_email varchar(255), IN s_address varchar(255))
BEGIN
	if (exists (
		select * from stores
		where store_name = s_name and phone = s_phone and email = s_email and address = s_address)) then
        select "store already exists" as answer;
	else
		INSERT INTO bikedb.stores(store_name, phone, email, address)
		value(s_name, s_phone, s_email, s_address);
        select "store insertion successful" as answer;
	end if;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS meta;
DELIMITER $$
CREATE PROCEDURE meta()
BEGIN
	select TABLE_NAME, COLUMN_NAME, IS_NULLABLE, DATA_TYPE, COLUMN_TYPE, COLUMN_KEY, CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION, EXTRA from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA="bikedb";
END $$
DELIMITER ;