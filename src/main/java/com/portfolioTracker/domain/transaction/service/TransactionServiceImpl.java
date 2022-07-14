package com.portfolioTracker.domain.transaction.service;

import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.portfolio.validation.exception.PortfolioNotFoundException;
import com.portfolioTracker.domain.transaction.TransactionEntity;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import com.portfolioTracker.domain.transaction.mapper.TransactionMapper;
import com.portfolioTracker.domain.transaction.repository.TransactionRepository;
import com.portfolioTracker.domain.transaction.validation.TransactionValidationService;
import com.portfolioTracker.domain.transaction.validation.exception.TransactionException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionValidationService validationService;
    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final PortfolioRepository portfolioRepository;

    @Override
    public TransactionDtoResponse save(TransactionDtoCreateRequest requestDto) {
        validationService.validate(requestDto);
        TransactionEntity requestEntity = mapper.createToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found with id: " +
                        requestDto.getPortfolioId()));
        requestEntity.setPortfolio(portfolioEntity);
        TransactionEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> requestDtoList) {
        return requestDtoList.parallelStream()
                .map(this::save)
                .toList();
    }

    @Override
    public TransactionDtoResponse findById(Long id) {
        TransactionEntity entity = repository.findById(id)
                .orElseThrow(() -> new TransactionException("Transaction not found with id :" + id));
        return mapper.toDto(entity);
    }

    @Override
    public List<TransactionDtoResponse> findAll() {
        return repository.findAll().parallelStream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<String> findAllUniqueTickers() {
        return repository.findAllUniqueTickers();
    }

    @Override
    public List<TransactionDtoResponse> findAllByPortfolioId(Long id) {
        return repository.findAllByPortfolioId(id).parallelStream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
   public TransactionDtoResponse update(TransactionDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        if (requestDto.getId() == null)
            throw new TransactionException("id cannot not be null for method update()");

        if (existsById(requestDto.getId())) {
            TransactionEntity entity = mapper.updateToEntity(requestDto);
            TransactionEntity savedEntity = repository.save(entity);
            return mapper.toDto(savedEntity);

        } else {
            throw new TransactionException("EquityTransaction with id " +
                    requestDto.getId() + " was not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(Long id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new TransactionException("Transaction not found with id :" + id))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
