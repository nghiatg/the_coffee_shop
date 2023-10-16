CREATE TABLE shop
(
   id                         bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   shop_operator              varchar(255) NOT NULL,
   longitude                  decimal(5,2),
   latitude                   decimal(5,2),
   open_time                  varchar(8),
   close_time                 varchar(8),
   shop_chain_id              bigint NOT NULL,
   status                     varchar(10) NOT NULL,
   FOREIGN KEY (shop_chain_id) REFERENCES shop_chain(id)

);