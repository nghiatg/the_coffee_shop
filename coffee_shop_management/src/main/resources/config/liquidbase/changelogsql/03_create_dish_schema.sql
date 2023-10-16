CREATE TABLE dish
(
   id          bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name        varchar(255) NOT NULL,
   price       decimal(10,2) NOT NULL,
   shop_id     bigint NOT NULL,
   status      varchar(10) NOT NULL,
   FOREIGN KEY (shop_id) REFERENCES shop(id)
);