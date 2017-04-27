
package es.jaranda.poc.springbootdemo.service.impl;

import es.jaranda.poc.springbootdemo.domain.Country;
import es.jaranda.poc.springbootdemo.repository.CountryRepository;
import es.jaranda.poc.springbootdemo.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public void incrementPopulation(final Country country,
                                    final long incrementedPopulation) {

        final String countryCode = country.getCountryCode();
        final Long currentPopulation = country.getPopulation();

        log.info("Current population of " + countryCode +
                logPopulation(currentPopulation));

        final Long newPopulation = nullSafeSum(
                country.getPopulation(), incrementedPopulation
        );

        country.setPopulation(newPopulation);

        countryRepository.save(country);

        log.info("New population of " + countryCode +
                logPopulation(newPopulation));

    }

    private String logPopulation(final Long currentPopulation) {
        return currentPopulation != null && currentPopulation != 1 ?
                " are: " + currentPopulation + " people" : " is 1 person";
    }

    private Long nullSafeSum(final Long one, final long two) {
        return one != null ? one + two : two;
    }

}
