package portfolioTracker.transaction.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

import javax.validation.Valid;

public interface TransactionMapper {
    TransactionEntity updateToEntity(TransactionDtoUpdateRequest requestDto);

    TransactionEntity createToEntity(TransactionDtoCreateRequest requestDto);

    TransactionDtoResponse toDto(TransactionEntity entity);

}
