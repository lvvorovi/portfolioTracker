package portfolioTracker.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNotFoundException;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.mapper.TransactionMapper;
import portfolioTracker.transaction.repository.TransactionRepository;
import portfolioTracker.transaction.validation.TransactionValidationService;
import portfolioTracker.transaction.validation.exception.TransactionException;
import portfolioTracker.transaction.validation.exception.TransactionNotFoundTransactionException;

import java.util.List;

import static portfolioTracker.core.ExceptionErrors.*;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionValidationService validationService;
    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final PortfolioRepository portfolioRepository;

    @Override
    public TransactionDtoResponse save(TransactionDtoCreateRequest requestDto) {
        validationService.validate(requestDto);
        TransactionEntity requestEntity = mapper.createToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundException(PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE +
                        requestDto.getPortfolioId()));
        requestEntity.setPortfolio(portfolioEntity);
        TransactionEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> requestDtoList) {
        return requestDtoList.parallelStream()
                .map(this::save)
                .toList();
    }

    @Override
    public TransactionDtoResponse findById(String id) {
        TransactionEntity entity = repository.findById(id)
                .orElseThrow(() -> new TransactionException(TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE + id));
        return mapper.toDto(entity);
    }

    @Override
    public List<TransactionDtoResponse> findAllByUsername(String username) {
        return repository.findAllByUsername(username).parallelStream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<TransactionDtoResponse> findAllByPortfolioId(String id) {
        return repository.findAllByPortfolioId(id).parallelStream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public Boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public List<String> findAllUniqueTickers() {
        return repository.findAllUniqueTickers();
    }

    @Override
    public TransactionDtoResponse update(TransactionDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);

        if (existsById(requestDto.getId())) {
            TransactionEntity entity = mapper.updateToEntity(requestDto);
            TransactionEntity savedEntity = repository.save(entity);
            return mapper.toDto(savedEntity);

        } else {
            throw new TransactionNotFoundTransactionException(
                    TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE + requestDto);
        }
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(String id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new TransactionException(
                        TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE + id))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
