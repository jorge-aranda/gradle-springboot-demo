
package es.jaranda.poc.springbootdemo.service;

import es.jaranda.poc.springbootdemo.domain.Country;
import es.jaranda.poc.springbootdemo.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void incrementPopulationTest() {

        final Country spainCountry =
                Country.builder().countryCode("ES").build();

        countryRepository.save(spainCountry);


        countryService.incrementPopulation(spainCountry, 1);

        countryService.incrementPopulation(spainCountry, 1000);

        countryService.incrementPopulation(
                spainCountry, 42000000
        );

        assertEquals(42001001l,
                (Object) countryRepository.findOne(spainCountry.getId())
                .getPopulation()
        );

    }

}
