package portfolioTracker.dividend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DividendMapperFactoryTest {

    @InjectMocks
    DividendMapperFactory victim;

    @Test
    void getInstance() {
        assertThat(victim.getInstance()).isInstanceOf(DividendMapper.class);
    }

}