package portfolioTracker.transaction.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

import javax.validation.Valid;

@Validated
public interface TransactionMapper {
    @Valid TransactionEntity updateToEntity(TransactionDtoUpdateRequest requestDto);

    @Valid TransactionEntity createToEntity(TransactionDtoCreateRequest requestDto);

    @Valid TransactionDtoResponse toDto(TransactionEntity entity);

}
