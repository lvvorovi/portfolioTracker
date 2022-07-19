package portfolioTracker.dividend.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import javax.validation.Valid;

@Validated
public interface DividendMapper {

    @Valid DividendEntity updateToEntity(DividendDtoUpdateRequest dto);

    @Valid DividendEntity createToEntity(DividendDtoCreateRequest dto);

    @Valid DividendDtoResponse toDto(DividendEntity entity);

}
