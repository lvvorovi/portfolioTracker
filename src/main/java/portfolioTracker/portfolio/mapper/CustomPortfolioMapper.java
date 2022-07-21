package portfolioTracker.portfolio.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

@Component
@AllArgsConstructor
public class CustomPortfolioMapper implements PortfolioMapper {

    @Override
    public PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest requestDto) {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(requestDto.getId());
        entity.setName(requestDto.getName());
        entity.setStrategy(requestDto.getStrategy());
        entity.setCurrency(requestDto.getCurrency());
        entity.setUsername(requestDto.getUsername());
        return entity;
    }

    @Override
    public PortfolioEntity createToEntity(PortfolioDtoCreateRequest requestDto) {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setName(requestDto.getName());
        entity.setStrategy(requestDto.getStrategy());
        entity.setCurrency(requestDto.getCurrency());
        entity.setUsername(requestDto.getUsername());
        return entity;
    }

    @Override
    public PortfolioDtoResponse toDto(PortfolioEntity entity) {
        PortfolioDtoResponse responseDto = new PortfolioDtoResponse();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setStrategy(entity.getStrategy());
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setUsername(entity.getUsername());
        return responseDto;
    }

}