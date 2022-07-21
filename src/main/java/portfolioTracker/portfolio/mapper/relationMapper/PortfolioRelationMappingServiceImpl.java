package portfolioTracker.portfolio.mapper.relationMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.mapper.DividendMapper;
import portfolioTracker.dividend.validation.exception.PortfolioNotFoundDividendException;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.mapper.PortfolioMapper;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.mapper.TransactionMapper;
import portfolioTracker.transaction.validation.exception.PortfolioNotFoundTransactionException;

import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@AllArgsConstructor
public class PortfolioRelationMappingServiceImpl implements PortfolioRelationMappingService {

    private final DividendMapper dividendMapper;
    private final TransactionMapper transactionMapper;
    private final PortfolioMapper portfolioMapper;
    private final PortfolioRepository portfolioRepository;

    @Override
    public DividendEntity updateToEntity(DividendDtoUpdateRequest requestDto) {
        DividendEntity dividendEntity = dividendMapper.updateToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundDividendException(
                        PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto));
        dividendEntity.setPortfolio(portfolioEntity);
        return dividendEntity;
    }

    @Override
    public DividendEntity createToEntity(DividendDtoCreateRequest requestDto) {
        DividendEntity dividendEntity = dividendMapper.createToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundDividendException(
                        PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto));
        dividendEntity.setPortfolio(portfolioEntity);
        return dividendEntity;
    }

    @Override
    public DividendDtoResponse toDto(DividendEntity entity) {
        return dividendMapper.toDto(entity);
    }

    @Override
    public PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest requestDto) {
        return portfolioMapper.updateToEntity(requestDto);
    }

    @Override
    public PortfolioEntity createToEntity(PortfolioDtoCreateRequest requestDto) {
        return portfolioMapper.createToEntity(requestDto);
    }

    @Override
    public PortfolioDtoResponse toDto(PortfolioEntity entity) {
        PortfolioDtoResponse responseDto = portfolioMapper.toDto(entity);

        if (entity.getDividendEntityList() != null) {
            responseDto.setDividendList(
                    entity.getDividendEntityList().stream()
                            .map(dividendMapper::toDto)
                            .toList());
        }

        if (entity.getTransactionEntityList() != null) {
            responseDto.setTransactionList(
                    entity.getTransactionEntityList().stream()
                            .map(transactionMapper::toDto)
                            .toList());
        }

        return responseDto;
    }

    @Override
    public TransactionEntity updateToEntity(TransactionDtoUpdateRequest requestDto) {
        TransactionEntity transactionEntity = transactionMapper.updateToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException(
                        PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto));
        transactionEntity.setPortfolio(portfolioEntity);
        return transactionEntity;
    }

    @Override
    public TransactionEntity createToEntity(TransactionDtoCreateRequest requestDto) {
        TransactionEntity transactionEntity = transactionMapper.createToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException(
                        PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto));
        transactionEntity.setPortfolio(portfolioEntity);
        return transactionEntity;
    }

    @Override
    public TransactionDtoResponse toDto(TransactionEntity entity) {
        return transactionMapper.toDto(entity);
    }


}
