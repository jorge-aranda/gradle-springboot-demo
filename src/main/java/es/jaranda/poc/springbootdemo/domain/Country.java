
package es.jaranda.poc.springbootdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class Country {

    private @Id @GeneratedValue Long id;
    private String countryCode;
    private Long population;

}
