package portfolioTracker.dividend.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import javax.validation.Valid;

public interface DividendMapper {

    DividendEntity updateToEntity(DividendDtoUpdateRequest requestDto);

    DividendEntity createToEntity(DividendDtoCreateRequest requestDto);

    DividendDtoResponse toDto(DividendEntity entity);

}
