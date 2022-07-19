package portfolioTracker.dto.portfolioWithEventsDto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import portfolioTracker.dividend.service.DividendService;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.service.PortfolioService;
import portfolioTracker.transaction.service.TransactionService;

import java.util.List;

@Service
@AllArgsConstructor
public class PortfolioWithEventsServiceImpl implements PortfolioWithEventsService {

    private final PortfolioService portfolioService;
    private final TransactionService transactionService;
    private final DividendService dividendService;

    @Override
    public PortfolioDtoResponse findById(String portfolioId) {
        PortfolioDtoResponse portfolioDtoResponse = portfolioService.findById(portfolioId);
        portfolioDtoResponse.setDividendList(dividendService.findAllByPortfolioId(portfolioId));
        portfolioDtoResponse.setTransactionList(transactionService.findAllByPortfolioId(portfolioId));
        return portfolioDtoResponse;
    }

    @Override
    public List<PortfolioDtoResponse> findAllByUsername(String username) {
        return portfolioService.findAllByUsername(username).parallelStream()
                .peek(dto -> dto.setTransactionList(transactionService.findAllByPortfolioId(dto.getId())))
                .peek(dto -> dto.setDividendList(dividendService.findAllByPortfolioId(dto.getId())))
                .toList();
    }
}
