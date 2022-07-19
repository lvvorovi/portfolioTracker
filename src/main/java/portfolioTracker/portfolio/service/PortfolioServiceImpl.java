package portfolioTracker.portfolio.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.mapper.PortfolioMapper;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.PortfolioValidationService;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.repository.TransactionRepository;
import portfolioTracker.transaction.validation.exception.PortfolioNotFoundTransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioValidationService validationService;
    private final PortfolioRepository repository;
    private final PortfolioMapper mapper;
    private final TransactionRepository transactionRepository;
    private final DividendRepository dividendRepository;

    @Override
    public PortfolioDtoResponse findByIdSkipEvents(String id) {
        PortfolioEntity entity = repository.findByIdSkipEvents(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException(
                        PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id));
        return mapper.toDto(entity);
    }

    @Override
    public PortfolioDtoResponse findByIdWithEvents(String id) {
        PortfolioEntity entity = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException(
                        PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id));
        return mapper.toDto(entity);
    }

    @Override
    public ArrayList<PortfolioDtoResponse> findAllWithEvents() {
        return repository.findAll().parallelStream()
                .map(mapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<PortfolioDtoResponse> findAllSkipEvents() {
        return repository.findAllSKipEvents().parallelStream()
                .map(mapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public PortfolioDtoResponse save(PortfolioDtoCreateRequest requestDto) {
        validationService.validate(requestDto);
        PortfolioEntity requestEntity = mapper.createToEntity(requestDto);
        PortfolioEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        PortfolioEntity requestEntity = mapper.updateToEntity(requestDto);
        List<TransactionEntity> transactionEntityList = transactionRepository.findAllByPortfolioId(requestDto.getId());
        List<DividendEntity> dividendEntityList = dividendRepository.findAllByPortfolioId(requestDto.getId());
        requestEntity.setTransactionEntityList(transactionEntityList);
        requestEntity.setDividendEntityList(dividendEntityList);
        PortfolioEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        // No need to check for existence. Done by Spring Security through this.isOwner()
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(String id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException(
                        PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

    @Override
    public List<String> findAllPortfolioCurrencies() {
        return repository.findAllPortfolioCurrencies();
    }
}
