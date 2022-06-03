CREATE TABLE currency_rates (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    portfolio_currency  VARCHAR(3)      NOT NULL,
    event_currency      VARCHAR(3)      NOT NULL,
    rate_date           DATE            NOT NULL,
    rate_client_sells   DECIMAL(50,10)  NOT NULL,
    rate_client_buys    DECIMAL(50,10)  NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uc_currency_rates UNIQUE (portfolio_currency, event_currency, rate_date)
)