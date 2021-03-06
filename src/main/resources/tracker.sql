DROP TABLE IF EXISTS currency_rates, dividends, transactions, flyway_schema_history, trade_transactions, dividend_events, portfolios;

create TABLE portfolios (
    id                  VARCHAR(36)     NOT NULL,
    name                VARCHAR(50)     NOT NULL,
    strategy            VARCHAR(150)    NOT NULL,
    currency            VARCHAR(3)      NOT NULL,
    username            VARCHAR(50)     NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uc_username_name UNIQUE (username, name)
);

SELECT id, name, strategy, currency, username FROM portfolios WHERE id = '1'
SELECT id, name, strategy, currency, username FROM portfolios WHERE id= 1;

create TABLE transactions (
    id                  VARCHAR(36)     NOT NULL,
    ticker              VARCHAR(50)     NOT NULL,
    trade_date          DATE            NOT NULL,
    quantity            DECIMAL(20,0)   NOT NULL,
    price               DECIMAL(50,10)  NOT NULL,
    commission          DECIMAL(50,2)   NOT NULL,
    event_type          VARCHAR(10)     NOT NULL,
    portfolio_id        VARCHAR(36)     NOT NULL,
    username            VARCHAR(50)     NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (portfolio_id) REFERENCES portfolios (id)
);
create TABLE dividends (
    id                  VARCHAR(36)     NOT NULL,
    ticker              VARCHAR(50)     NOT NULL,
    ex_dividend_date    DATE            NOT NULL,
    payment_date        DATE            NOT NULL,
    amount              DECIMAL(50,2)   NOT NULL,
    event_type          VARCHAR(10)     NOT NULL,
    portfolio_id        VARCHAR(36)     NOT NULL,
    username            VARCHAR(50)     NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (portfolio_id) REFERENCES portfolios (id),
    CONSTRAINT uc_dividend_event UNIQUE (ticker, ex_dividend_date, portfolio_id)
);

/*

insert into portfolios values (null, 'Long Term Investment Portfolio', 'no strategy set', 'EUR');

insert into trade_transactions values (null, 'BRK-B', '2019-04-05', 14, 203, 23.61, 1.0935, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'KHC', '2019-11-14', 75, 32.97746667, 23.61, 1.0935, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NCC-B.ST', '2019-11-25', 345, 147.5, 224.56, 10.523, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'DANSKE.CO', '2020-03-30', 100, 74.4, 134.43, 7.359, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'SRNKE-B.ST', '2020-03-30', 320, 34.8171875, 197.35, 10.757, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2020-09-30', 6000, 0.9322, 200.60, 10.9165, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2020-11-19', 10000, 0.5, 193.16, 10.614, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2020-11-04', 3000, 0.6036, 199.69, 10.932, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2020-11-11', 3000, 0.53913333333333, 196.57, 10.645, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2020-11-17', 3000, 0.5, 192.78, 10.499, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2021-05-31', 910, 6.26, 264.09, 9.8685, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2021-05-31', 240, 6.26, 264.09, 9.8685, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2021-05-31', 135, 6.26, 264.09, 9.8685, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'SRNKE-B.ST', '2021-11-24', 320, 51.2, 101.28, 10.318, 'EUR', 'Sell', 1);
insert into trade_transactions values (null, 'KHC', '2021-11-24', 75, 35.5, 11.28, 1.146, 'EUR', 'Sell', 1);
insert into trade_transactions values (null, 'SWED-A.ST', '2021-12-29', 27, 181, 103.30, 10.145, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'SHB-A.ST', '2021-12-29', 51, 97, 103.30, 10.145, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NAS.OL', '2022-01-31', 850, 12, 150.17, 10.1325, 'EUR', 'Sell', 1);
insert into trade_transactions values (null, 'SAS.ST', '2022-02-08', 5870, 1.72, 103.87, 10.219, 'EUR', 'Buy', 1);
insert into trade_transactions values (null, 'NCC-B.ST', '2022-05-02', 400, 123.5, 123.50, 10.1985, 'EUR', 'Buy', 1);

ALTER TABLE dividend_events DROP COLUMN exchange_rate;

--                                   id     ticker     ex_D          date         amount  shares portfolio_id
insert into dividend_events values (null, 'NCC-B.ST', '2020-11-19', '2020-11-19', '733.12', 345, 1);
insert into dividend_events values (null, 'NCC-B.ST', '2021-03-31', '2021-04-08', '733.13', 345, 1);
insert into dividend_events values (null, 'NCC-B.ST', '2021-11-08', '2021-11-08', '733.12', 345, 1);
insert into dividend_events values (null, 'NCC-B.ST', '2022-04-06', '2022-04-12', '879.75', 345, 1);

insert into dividend_events values (null, 'KHC', '2019-11-14', '2019-12-13', '21.00', 75, 1);
insert into dividend_events values (null, 'KHC', '2020-03-12', '2020-03-30', '21.00', 75, 1);
insert into dividend_events values (null, 'KHC', '2020-05-28', '2020-06-29', '21.00', 75, 1);
insert into dividend_events values (null, 'KHC', '2020-08-27', '2020-09-28', '21.00', 75, 1);
insert into dividend_events values (null, 'KHC', '2020-11-25', '2020-12-21', '21.00', 75, 1);
insert into dividend_events values (null, 'KHC', '2021-03-11', '2021-03-29', '21.00', 75, 1);
insert into dividend_events values (null, 'KHC', '2021-05-27', '2021-06-28', '21.00', 75, 1);
insert into dividend_events values (null, 'KHC', '2021-08-31', '2021-09-27', '21.00', 75, 1);

insert into dividend_events values (null, 'DANSKE.CO', '2021-03-17', '2021-03-19', '146.00', 100, 1);
insert into dividend_events values (null, 'DANSKE.CO', '2022-03-18', '2022-03-22', '146.00', 100, 1);

insert into dividend_events values (null, 'SHB-A.ST', '2022-03-24', '2022-03-30', '216.75', 51, 1);

insert into dividend_events values (null, 'SWED-A.ST', '2022-03-31', '2022-04-06', '258.19', 27, 1);
*/

SELECT * FROM dividends;
SELECT * FROM transactions;
SELECT * FROM portfolios;

INSERT INTO portfolios VALUES ('123456789012345678901234567890123456789012345678901234567890', 'name', 'strategy', 'EUR', 'john@email.com')
INSERT INTO portfolios VALUES ('123456789012345678901234567890123456789012345678901234567891', 'name', 'strategy', 'EUR', 'bill@email.com')
INSERT INTO transactions VALUES ('123456789012345678901234567890123456789012345678901234567890', 'BRK-B', '2021-05-27', 500, 200, 2, 'BUY', '123456789012345678901234567890123456789012345678901234567890', 'john@email.com')
INSERT INTO transactions VALUES ('123456789012345678901234567890123456789012345678901234567891', 'BRK-B', '2021-05-27', 500, 200, 2, 'BUY', '123456789012345678901234567890123456789012345678901234567891', 'bill@email.com')
INSERT INTO dividends VALUES ('123456789012345678901234567890123456789012345678901234567890', 'BRK-B', '2020-01-01', '2020-01-01', 100, 'DIVIDEND', '123456789012345678901234567890123456789012345678901234567890', 'john@email.com')
INSERT INTO dividends VALUES ('123456789012345678901234567890123456789012345678901234567891', 'BRK-B', '2020-01-01', '2020-01-01', 100, 'DIVIDEND', '123456789012345678901234567890123456789012345678901234567891', 'bill@email.com')

UPDATE dividends SET event_type = 'DIVIDEND';
UPDATE transactions SET event_type = 'SELL' where event_type = 'Sell'
UPDATE transactions SET event_type = 'BUY' where event_type = 'Buy'

DELETE FROM dividends;
DELETE FROM transactions;
DELETE FROM portfolios;

DROP TABLE dividends;
DROP TABLE transactions;
DROP TABLE portfolios;