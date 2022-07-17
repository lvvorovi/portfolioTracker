package portfolioTracker.dividend.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.core.contract.DomainMapper;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import javax.validation.Valid;

@Validated
public interface DividendMapper extends DomainMapper<DividendEntity, DividendDtoCreateRequest, DividendDtoUpdateRequest, DividendDtoResponse> {

    @Override
    @Valid DividendEntity updateToEntity(DividendDtoUpdateRequest dto);

    @Override
    @Valid DividendEntity createToEntity(DividendDtoCreateRequest dto);

    @Override
    @Valid DividendDtoResponse toDto(DividendEntity entity);

}
