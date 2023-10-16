CREATE TABLE reserved_order_queue
(
   id          bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   max_size    mediumint NOT NULL,
   shop_id     bigint NOT NULL,
   status      varchar(10) NOT NULL,
   FOREIGN KEY (shop_id) REFERENCES shop(id)
);