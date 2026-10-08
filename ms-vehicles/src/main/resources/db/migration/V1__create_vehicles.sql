CREATE TABLE vehicles (
    id              UUID         PRIMARY KEY,
    plate           VARCHAR(20)  NOT NULL,
    make            VARCHAR(60)  NOT NULL,
    model           VARCHAR(60)  NOT NULL,
    year            INTEGER      NOT NULL,
    type            VARCHAR(20)  NOT NULL,
    fuel_type       VARCHAR(20)  NOT NULL,
    tank_capacity_l INTEGER      NOT NULL,
    odometer_km     INTEGER      NOT NULL DEFAULT 0,
    status          VARCHAR(20)  NOT NULL,
    version         BIGINT       NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ  NOT NULL,
    updated_at      TIMESTAMPTZ  NOT NULL,
    CONSTRAINT uq_vehicles_plate UNIQUE (plate),
    CONSTRAINT ck_vehicles_tank_positive CHECK (tank_capacity_l > 0),
    CONSTRAINT ck_vehicles_odometer_non_negative CHECK (odometer_km >= 0)
);

CREATE INDEX idx_vehicles_status ON vehicles (status);
CREATE INDEX idx_vehicles_type ON vehicles (type);
