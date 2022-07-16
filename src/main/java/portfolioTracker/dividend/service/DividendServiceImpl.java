package portfolioTracker.dividend.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.mapper.DividendMapper;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.dividend.validation.DividendValidationService;
import portfolioTracker.dividend.validation.exception.DividendNotFoundDividendException;
import portfolioTracker.dividend.validation.exception.PortfolioNotFoundDividendException;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.repository.PortfolioRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DividendServiceImpl implements DividendService {

    private final DividendValidationService validationService;
    private final DividendMapper mapper;
    private final DividendRepository repository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public DividendDtoResponse save(DividendDtoCreateRequest requestDto) {
        validationService.validate(requestDto);
        DividendEntity requestEntity = mapper.createToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundDividendException(
                        "Portfolio not found for: " + requestDto));
        requestEntity.setPortfolio(portfolioEntity);
        requestEntity.setId(UUID.randomUUID().toString());
        DividendEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    @Transactional
    public List<DividendDtoResponse> saveAll(List<DividendDtoCreateRequest> requestDtoList) {
        return requestDtoList.parallelStream()
                .map(this::save)
                .toList();
    }

    @Override
    public DividendDtoResponse findById(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new
                        DividendNotFoundDividendException("Dividend not found for id:" + id));
    }

    @Override
    public List<DividendDtoResponse> findAllByUsername(String username) {
        return repository.findAllByUsername(username).parallelStream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<DividendDtoResponse> findAllByPortfolioId(String id) {
        return repository.findAllByPortfolioId(id).parallelStream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public DividendDtoResponse update(DividendDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        DividendEntity requestEntity = mapper.updateToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundDividendException("Portfolio not found for: " + requestDto));
        requestEntity.setPortfolio(portfolioEntity);
        DividendEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        // No need to verify for existence. Will be verified by this.IsOwner(),
        // called by Spring Security through @PreAuthorize().
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(String id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new DividendNotFoundDividendException("Dividend not found for id:" + id))
                .getUsername();
        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
