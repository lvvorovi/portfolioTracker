package portfolioTracker.dividend.repository.cache;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import portfolioTracker.dividend.domain.DividendEntity;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryDividendCacheImpl implements DividendCache {

    private final Map<String, DividendEntity> dividendCashById = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastTimeUsedTimeStamp = new ConcurrentHashMap<>();
    private final Integer ttlMinutes = 60; //TODO

    @Override
    public DividendEntity save(DividendEntity requestEntity) {
        if (requestEntity != null) {
            DividendEntity entity;

            if (dividendCashById.containsKey(requestEntity.getId())) {
                DividendEntity finalEntity = dividendCashById.get(requestEntity.getId());
                Field[] fields = requestEntity.getClass().getDeclaredFields();

                Arrays.stream(fields).forEach(field -> {
                    try {
                        if (field.get(requestEntity) != null)
                            field.set(finalEntity, field.get(requestEntity));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

                entity = finalEntity;
            } else {
                entity = requestEntity;
            }

            dividendCashById.put(entity.getId(), entity);
            lastTimeUsedTimeStamp.put(entity.getId(), LocalDateTime.now());
        }


        return null;
    }

    @Override
    public Optional<DividendEntity> findById(String id) {
        if (id != null) {
            lastTimeUsedTimeStamp.put(id, LocalDateTime.now());
            return Optional.of(dividendCashById.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<DividendEntity> findAllByTicker(String ticker) {
        if (ticker != null) {
            return dividendCashById.values().stream()
                    .filter(entity -> entity.getTicker().equalsIgnoreCase(ticker))
                    .peek(entity -> lastTimeUsedTimeStamp.put(entity.getId(), LocalDateTime.now()))
                    .toList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<DividendEntity> findAllByPortfolioId(String id) {
        if (id != null) return dividendCashById.values().stream()
                .filter(entity -> entity.getPortfolio().getId().equalsIgnoreCase(id))
                .peek(entity -> lastTimeUsedTimeStamp.put(entity.getId(), LocalDateTime.now()))
                .toList();
        return new ArrayList<>();
    }

    @Override
    public List<DividendEntity> findAllByUsername(String username) {
        if (username != null) return dividendCashById.values().stream()
                .filter(entity -> entity.getUsername().equalsIgnoreCase(username))
                .peek(entity -> lastTimeUsedTimeStamp.put(entity.getId(), LocalDateTime.now()))
                .toList();
        return new ArrayList<>();
    }

    @Override
    public Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId) {
        if (ticker != null && exDate != null && portfolioId != null)
            return dividendCashById.values().stream()
                    .filter(entity -> entity.getPortfolio().getId().equalsIgnoreCase(portfolioId))
                    .filter(entity -> entity.getTicker().equalsIgnoreCase(ticker))
                    .filter(entity -> entity.getExDate().equals(exDate))
                    .peek(entity -> lastTimeUsedTimeStamp.put(entity.getId(), LocalDateTime.now()))
                    .findFirst();
        return Optional.empty();
    }

    @Override
    public boolean existsById(String id) {
        if (id != null) {
            boolean exists =  dividendCashById.containsKey(id);
            if (exists) lastTimeUsedTimeStamp.put(id, LocalDateTime.now());
            return exists;
        }
        return false;
    }

    @Override
    public boolean existsByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId) {
        if (ticker != null && exDate != null && portfolioId != null)
            return dividendCashById.values().stream()
                    .filter(entity -> entity.getPortfolio().getId().equalsIgnoreCase(portfolioId))
                    .filter(entity -> entity.getTicker().equalsIgnoreCase(ticker))
                    .filter(entity -> entity.getExDate().equals(exDate))
                    .peek(entity -> lastTimeUsedTimeStamp.put(entity.getId(), LocalDateTime.now()))
                    .anyMatch(entity -> entity.getExDate().equals(exDate));
        return false;
    }

    @Override
    public void deleteById(String id) {
        if (id != null) dividendCashById.remove(id);
    }

    @Async
    @Scheduled(fixedRateString = "PT60M")
    @Override
    public void removeTtlOutdated() {
        List<String> deletedIdList = new ArrayList<>();

        lastTimeUsedTimeStamp.entrySet().stream()
                .peek(set -> {
                    try {
                        Thread.currentThread().wait(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(entry -> entry.getValue().isBefore(LocalDateTime.now().minusMinutes(ttlMinutes)))
                .forEach(entry -> {
                    lastTimeUsedTimeStamp.remove(entry.getKey());
                    deletedIdList.add(entry.getKey());
                });

        deletedIdList.forEach(lastTimeUsedTimeStamp::remove);
    }
}
