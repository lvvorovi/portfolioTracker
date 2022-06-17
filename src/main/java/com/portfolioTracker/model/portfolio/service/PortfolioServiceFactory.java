package com.portfolioTracker.model.portfolio.service;

public class PortfolioServiceFactory {

    private static PortfolioService portfolioService;

    public PortfolioServiceFactory(PortfolioService portfolioService) {
        PortfolioServiceFactory.portfolioService = portfolioService;
    }

    public static PortfolioService getPortfolioService() {
        return portfolioService;
    }

}
