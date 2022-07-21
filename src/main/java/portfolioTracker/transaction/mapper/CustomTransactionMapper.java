package portfolioTracker.transaction.mapper;

import org.springframework.stereotype.Component;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

@Component
public class CustomTransactionMapper implements TransactionMapper {

    @Override
    public TransactionEntity updateToEntity(TransactionDtoUpdateRequest requestDto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(requestDto.getId());
        entity.setTicker((requestDto.getTicker()));
        entity.setShares(requestDto.getShares());
        entity.setPrice(requestDto.getPrice());
        entity.setDate(requestDto.getDate());
        entity.setCommission(requestDto.getCommission());
        entity.setType(requestDto.getType());
        entity.setUsername(requestDto.getUsername());
        return entity;
    }

    @Override
    public TransactionEntity createToEntity(TransactionDtoCreateRequest requestDto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTicker((requestDto.getTicker()));
        entity.setShares(requestDto.getShares());
        entity.setPrice(requestDto.getPrice());
        entity.setDate(requestDto.getDate());
        entity.setCommission(requestDto.getCommission());
        entity.setType(requestDto.getType());
        entity.setUsername(requestDto.getUsername());
        return entity;
    }

    @Override
    public TransactionDtoResponse toDto(TransactionEntity entity) {
        TransactionDtoResponse responseDto = new TransactionDtoResponse();
        responseDto.setId(entity.getId());
        responseDto.setTicker((entity.getTicker()));
        responseDto.setDate(entity.getDate());
        responseDto.setPrice(entity.getPrice());
        responseDto.setShares(entity.getShares());
        responseDto.setCommission(entity.getCommission());
        responseDto.setType(entity.getType());
        responseDto.setPortfolioId(entity.getPortfolio().getId());
        responseDto.setUsername(entity.getUsername());
        return responseDto;
    }
}