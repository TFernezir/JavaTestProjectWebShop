INSERT INTO ROLES (name) VALUES ('USER');
INSERT INTO ROLES (name) VALUES ('ADMIN');

INSERT INTO PRODUCTS (name, price, about) VALUES ('Spring boot', 15.5, 'Book');
INSERT INTO PRODUCTS (name, price, about) VALUES ('Second Product', 146.5, 'Shoes');

INSERT INTO BASKETS (user_id, status, total_amount) VALUES (1, 'ACTIVE', 100.0);
INSERT INTO BASKETS (user_id, status, total_amount) VALUES (2, 'SAVED', 45.0);

INSERT INTO BASKET_ITEMS (basket_id, product_id, quantity, unit_price) VALUES (1, 1, 2, 25.0);
INSERT INTO BASKET_ITEMS (basket_id, product_id, quantity, unit_price) VALUES (1, 2, 2, 25.0);
INSERT INTO BASKET_ITEMS (basket_id, product_id, quantity, unit_price) VALUES (2, 1, 2, 25.0);
INSERT INTO BASKET_ITEMS (basket_id, product_id, quantity, unit_price) VALUES (2, 2, 2, 25.0);