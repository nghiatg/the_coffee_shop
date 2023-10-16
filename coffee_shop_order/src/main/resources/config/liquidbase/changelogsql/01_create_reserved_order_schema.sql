CREATE TABLE reserved_order
(
   id                         bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   reserved_order_queue_id    bigint NOT NULL,
   dish_id                    bigint NOT NULL,
   customer                   varchar(255) NOT NULL,
   reserved_time              bigint NOT NULL,
   status                     char(10) NOT NULL
);