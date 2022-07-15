package portfolioTracker.dividend.service;

import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.mapper.DividendMapper;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.dividend.validation.DividendValidationService;
import portfolioTracker.dividend.validation.exception.DividendNotFoundException;
import portfolioTracker.portfolio.validation.exception.PortfolioNotFoundException;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        requestEntity.setPortfolio(portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundException(
                        "Portfolio with requested id not found. id: " + requestDto.getPortfolioId())));
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
    public ArrayList<DividendDtoResponse> findAll() {
        return repository.findAll().parallelStream()
                .map(mapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public DividendDtoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new
                DividendNotFoundException("DividendEvent with id " + id + " not found"));
    }

    @Override
    public List<DividendDtoResponse> findAllByPortfolioId(Long id) {
        return repository.findAllByPortfolioId(id).parallelStream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public DividendDtoResponse update(DividendDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        DividendEntity requestEntity = mapper.updateToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found with id: "
                        + requestDto.getPortfolioId()));
        requestEntity.setPortfolio(portfolioEntity);
        DividendEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        // No need to verify for existence. Will be verified by this.IsOwner()
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(Long id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new DividendNotFoundException("DividendEntity with id " + id + " not found"))
                .getUsername();
        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
