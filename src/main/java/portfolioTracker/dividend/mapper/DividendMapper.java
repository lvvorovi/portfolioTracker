package portfolioTracker.dividend.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import javax.validation.Valid;

@Validated
public interface DividendMapper {

    @Valid DividendEntity updateToEntity(DividendDtoUpdateRequest requestDto);

    @Valid DividendEntity createToEntity(DividendDtoCreateRequest requestDto);

    @Valid DividendDtoResponse toDto(DividendEntity entity);

}
