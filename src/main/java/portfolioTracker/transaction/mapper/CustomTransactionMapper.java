package portfolioTracker.transaction.mapper;

import org.springframework.stereotype.Component;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

@Component
public class CustomTransactionMapper implements TransactionMapper {

    @Override
    public TransactionEntity updateToEntity(TransactionDtoUpdateRequest dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(dto.getId());
        entity.setTicker((dto.getTicker()));
        entity.setShares(dto.getShares());
        entity.setPrice(dto.getPrice());
        entity.setDate(dto.getDate());
        entity.setCommission(dto.getCommission());
        entity.setType(dto.getType());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    @Override
    public TransactionEntity createToEntity(TransactionDtoCreateRequest dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTicker((dto.getTicker()));
        entity.setShares(dto.getShares());
        entity.setPrice(dto.getPrice());
        entity.setDate(dto.getDate());
        entity.setCommission(dto.getCommission());
        entity.setType(dto.getType());
        entity.setUsername(dto.getUsername());
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