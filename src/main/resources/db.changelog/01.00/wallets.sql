create table if not exists wallets (
    id UUID PRIMARY KEY,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    owner_id UUID NOT NULL,
    creation_date DATE NOT NULL,
    deletion_date DATE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);