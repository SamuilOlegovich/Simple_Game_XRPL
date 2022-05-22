CREATE TABLE IF NOT EXISTS users (
    id                      BIGINT AUTO_INCREMENT   NOT NULL,
    uuid                    VARCHAR(255)            NOT NULL,
    account                 VARCHAR(255)            NOT NULL,
    destination_tag         VARCHAR(255)            NOT NULL,
    available_funds         DECIMAL                 NOT NULL,
    bet                     DECIMAL                 NOT NULL,
    datas                   VARCHAR(255)            NOT NULL,

    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS users_test (
    id                      BIGINT AUTO_INCREMENT   NOT NULL,
    uuid                    VARCHAR(255)            NOT NULL,
    account                 VARCHAR(255)            NOT NULL,
    destination_tag         VARCHAR(255)            NOT NULL,
    available_funds         DECIMAL                 NOT NULL,
    bet                     DECIMAL                 NOT NULL,
    datas                   VARCHAR(255)            NOT NULL,

    PRIMARY KEY (id)
    );



CREATE TABLE IF NOT EXISTS payouts (
    id                      BIGINT   AUTO_INCREMENT NOT NULL,
    user_id                 BIGINT,
    uuid                    VARCHAR(255)            NOT NULL,
    user_uuid               VARCHAR(255),
    account                 VARCHAR(255)            NOT NULL,
    destination_tag         VARCHAR(255)            NOT NULL,
    tag_out                 VARCHAR(255)            NOT NULL,
    available_funds         DECIMAL                 NOT NULL,
    payouts                 DECIMAL                 NOT NULL,
    bet                     DECIMAL,
    datas                   VARCHAR(255)            NOT NULL,

    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS payouts_test (
    id                      BIGINT   AUTO_INCREMENT NOT NULL,
    user_id                 BIGINT,
    uuid                    VARCHAR(255)            NOT NULL,
    user_uuid               VARCHAR(255),
    account                 VARCHAR(255)            NOT NULL,
    destination_tag         VARCHAR(255)            NOT NULL,
    tag_out                 VARCHAR(255)            NOT NULL,
    available_funds         DECIMAL                 NOT NULL,
    payouts                 DECIMAL                 NOT NULL,
    bet                     DECIMAL,
    datas                   VARCHAR(255)            NOT NULL,

    PRIMARY KEY (id)
    );



CREATE TABLE IF NOT EXISTS conditions (
    id                      BIGINT   AUTO_INCREMENT NOT NULL,
    bet                     DECIMAL                 NOT NULL,
    bias                    INT,

    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS conditions_test (
    id                      BIGINT   AUTO_INCREMENT NOT NULL,
    bet                     DECIMAL                 NOT NULL,
    bias                    INT,

    PRIMARY KEY (id)
    );



CREATE TABLE IF NOT EXISTS lotto (
    id                      BIGINT          AUTO_INCREMENT NOT NULL,
    total_lotto             DECIMAL         NOT NULL,

    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS lotto_test (
    id                      BIGINT          AUTO_INCREMENT NOT NULL,
    total_lotto             DECIMAL         NOT NULL,

    PRIMARY KEY (id)
    );
