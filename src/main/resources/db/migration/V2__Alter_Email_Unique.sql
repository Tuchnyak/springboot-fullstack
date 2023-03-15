ALTER TABLE customer
    ADD CONSTRAINT cons_customer_email_unique UNIQUE (email);