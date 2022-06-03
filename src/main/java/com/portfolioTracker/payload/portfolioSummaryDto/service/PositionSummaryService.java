package com.portfolioTracker.payload.portfolioSummaryDto.service;

import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.PositionSummary;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.Position;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class PositionSummaryService {

    public List<PositionSummary> getSummaryList(@NotNull List<Position> positions) {
        List<PositionSummary> positionSummaryList = new ArrayList<>();

        positions.forEach(position -> {
            PositionSummary positionSummary = new PositionSummary();
            positionSummary.setTicker(position.getName().substring(0, position.getName().indexOf(" ")));
            positionSummary.setTotalBough(position.getTotalBought().subtract(position.getTotalSold()));
            positionSummary.setTotalShares(position.getTotalShares());
            positionSummary.setCurrentSharePrice(position.getCurrentSharePrice());
            positionSummary.setCurrentValue(position.getCurrentValue());
            positionSummary.setCapitalGain(position.getCapitalGain());
            positionSummary.setDividend(position.getDividend());
            positionSummary.setCommission(position.getCommission());
            positionSummary.setCurrencyGain(position.getCurrencyGain());
            positionSummary.setTotalGain(position.getTotalGain());
            positionSummary.setTotalReturn(position.getTotalReturn());

            positionSummaryList.add(positionSummary);

        });

        return positionSummaryList;
    }

}
