CREATE USER 'coffee_shop_management'@'%' IDENTIFIED BY 'coffee_shop_management_pw';
CREATE DATABASE IF NOT EXISTS `coffee_shop_management_db`;
GRANT ALL ON coffee_shop_management_db.* TO 'coffee_shop_management'@'%';

CREATE USER 'coffee_shop_order'@'%' IDENTIFIED BY 'coffee_shop_order_pw';
CREATE DATABASE IF NOT EXISTS `coffee_shop_order_db`;
GRANT ALL ON coffee_shop_order_db.* TO 'coffee_shop_order'@'%';

CREATE USER 'coffee_keycloak_server'@'%' IDENTIFIED BY 'coffee_keycloak_server_pw';
CREATE DATABASE IF NOT EXISTS `keycloak`;
GRANT ALL ON keycloak.* TO 'coffee_keycloak_server'@'%';