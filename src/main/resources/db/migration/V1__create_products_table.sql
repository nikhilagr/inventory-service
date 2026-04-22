CREATE TABLE products (
    id          UUID            NOT NULL,
    name        VARCHAR(255)    NOT NULL,
    sku         VARCHAR(100)    NOT NULL,
    quantity    INT             NOT NULL,
    price       NUMERIC(19, 4)  NOT NULL,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT uq_products_sku UNIQUE (sku)
);
