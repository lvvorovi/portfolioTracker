package portfolioTracker.transaction.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.core.contract.DomainMapper;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

import javax.validation.Valid;

@Validated
public interface TransactionMapper extends DomainMapper<TransactionEntity, TransactionDtoCreateRequest, TransactionDtoUpdateRequest, TransactionDtoResponse> {

    @Override
    @Valid TransactionEntity updateToEntity(TransactionDtoUpdateRequest dto);

    @Override
    @Valid TransactionEntity createToEntity(TransactionDtoCreateRequest dto);

    @Override
    @Valid TransactionDtoResponse toDto(TransactionEntity entity);

}
