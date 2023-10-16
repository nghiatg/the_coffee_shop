CREATE TABLE contact
(
   id          bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   type        varchar(255) NOT NULL,
   value       varchar(255) NOT NULL,
   shop_id     bigint NOT NULL,
   status      varchar(10) NOT NULL,
   FOREIGN KEY (shop_id) REFERENCES shop(id)
);