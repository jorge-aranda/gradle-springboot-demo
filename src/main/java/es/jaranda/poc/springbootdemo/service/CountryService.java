
package es.jaranda.poc.springbootdemo.service;

import es.jaranda.poc.springbootdemo.domain.Country;

public interface CountryService {

    void incrementPopulation(Country country, long incrementedPopulation);

}
