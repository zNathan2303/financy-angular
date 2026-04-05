CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    email VARCHAR(300) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    color VARCHAR(50) NOT NULL,
    icon VARCHAR(100) NOT NULL,
    user_id UUID NOT NULL
);

CREATE TABLE transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(100),
    date DATE DEFAULT CURRENT_DATE NOT NULL,
    value DECIMAL(11,2) NOT NULL,
    income BOOLEAN NOT NULL,
    category_id BIGINT NOT NULL,
    user_id UUID NOT NULL
);

ALTER TABLE categories
ADD CONSTRAINT fk_categories_user
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_category
FOREIGN KEY (category_id)
REFERENCES categories(id)
ON DELETE CASCADE;

ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_user
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;
