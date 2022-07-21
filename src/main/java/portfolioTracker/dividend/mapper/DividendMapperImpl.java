package portfolioTracker.dividend.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

@Component
@AllArgsConstructor
public class DividendMapperImpl implements DividendMapper {

    @Override
    public DividendEntity updateToEntity(DividendDtoUpdateRequest requestDto) {
        DividendEntity entity = new DividendEntity();
        entity.setId(requestDto.getId());
        entity.setTicker((requestDto.getTicker()));
        entity.setExDate(requestDto.getExDate());
        entity.setDate(requestDto.getDate());
        entity.setAmount(requestDto.getAmount());
        entity.setType(requestDto.getType());
        entity.setUsername(requestDto.getUsername());
        return entity;
    }

    @Override
    public DividendEntity createToEntity(DividendDtoCreateRequest requestDto) {
        DividendEntity entity = new DividendEntity();
        entity.setTicker((requestDto.getTicker()));
        entity.setExDate(requestDto.getExDate());
        entity.setDate(requestDto.getDate());
        entity.setAmount(requestDto.getAmount());
        entity.setType(requestDto.getType());
        entity.setUsername(requestDto.getUsername());
        return entity;
    }

    @Override
    public DividendDtoResponse toDto(DividendEntity entity) {
        DividendDtoResponse responseDto = new DividendDtoResponse();
        responseDto.setId(entity.getId());
        responseDto.setTicker((entity.getTicker()));
        responseDto.setExDate(entity.getExDate());
        responseDto.setDate(entity.getDate());
        responseDto.setAmount(entity.getAmount());
        responseDto.setType(entity.getType());
        responseDto.setUsername(entity.getUsername());
        responseDto.setPortfolioId(entity.getPortfolio().getId());
        return responseDto;
    }
}