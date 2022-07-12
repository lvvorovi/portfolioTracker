package com.portfolioTracker.domain.dto.positionSummaryDto;

import com.portfolioTracker.domain.dto.position.PositionDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class PositionSummaryDtoService {

    public List<PositionSummaryDto> getPositionSummaryList(@NotNull List<PositionDto> positionList) {
        List<PositionSummaryDto> positionSummaryList = new ArrayList<>();

        positionList.forEach(positionDto -> {
            PositionSummaryDto positionSummary = new PositionSummaryDto();
            positionSummary.setTicker(positionDto.getName().substring(0, positionDto.getName().indexOf(" ")));
            positionSummary.setTotalBough(positionDto.getTotalBought().subtract(positionDto.getTotalSold()));
            positionSummary.setTotalShares(positionDto.getTotalShares());
            positionSummary.setCurrentSharePrice(positionDto.getCurrentSharePrice());
            positionSummary.setCurrentValue(positionDto.getCurrentValue());
            positionSummary.setCapitalGain(positionDto.getCapitalGain());
            positionSummary.setDividend(positionDto.getDividend());
            positionSummary.setCommission(positionDto.getCommission());
            positionSummary.setCurrencyGain(positionDto.getCurrencyGain());
            positionSummary.setTotalGain(positionDto.getTotalGain());
            positionSummary.setTotalReturn(positionDto.getTotalReturn());

            positionSummaryList.add(positionSummary);

        });

        return positionSummaryList;
    }

}
