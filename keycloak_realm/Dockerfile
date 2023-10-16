FROM keycloak/keycloak
MAINTAINER coffee_shop_builder
COPY ./coffee_shop_realm_all.json /opt/keycloak/data/import/coffee_shop_realm_all.json
ENTRYPOINT /opt/keycloak/bin/kc.sh start-dev --import-realm --hostname-admin-url=${APIGW_HTTP_API}