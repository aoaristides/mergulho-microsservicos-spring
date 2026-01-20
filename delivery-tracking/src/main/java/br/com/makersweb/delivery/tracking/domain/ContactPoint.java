package br.com.makersweb.delivery.tracking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author andersonorione
 */
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Getter
public class ContactPoint {

    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String name;
    private String phone;

}
