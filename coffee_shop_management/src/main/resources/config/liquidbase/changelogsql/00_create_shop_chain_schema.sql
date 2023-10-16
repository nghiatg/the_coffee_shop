CREATE TABLE shop_chain
(
   id                   bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   shop_chain_name      varchar(255) NOT NULL,
   shop_chain_owner     varchar(255) NOT NULL,
   status               varchar(10) NOT NULL
);