package portfolioTracker.core;

import org.springframework.stereotype.Component;
import portfolioTracker.dividend.DividendController;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.portfolio.PortfolioController;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.transaction.TransactionController;
import portfolioTracker.transaction.dto.TransactionDtoResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinkUtil {

    public void addLinks(TransactionDtoResponse responseDto) {
        responseDto.add(linkTo(methodOn(TransactionController.class)
                .findById(responseDto.getId()))
                .withSelfRel());
    }

    public void addLinks(DividendDtoResponse responseDto) {
        responseDto.add(linkTo(methodOn(DividendController.class)
                .findById(responseDto.getId()))
                .withSelfRel());
    }

    public void addLinks(PortfolioDtoResponse responseDto) {
        responseDto
                .add(linkTo(methodOn(PortfolioController.class)
                        .findById(responseDto.getId(), null))
                        .withSelfRel());
    }

    public void addLinksInclEvents(PortfolioDtoResponse responseDto) {
        addLinks(responseDto);
        responseDto.getTransactionList().forEach(this::addLinks);
        responseDto.getDividendList().forEach(this::addLinks);
    }

}
