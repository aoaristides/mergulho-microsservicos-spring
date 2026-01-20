package br.com.makersweb.delivery.tracking.domain;

import lombok.*;

import java.util.UUID;

/**
 * @author andersonorione
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Item {

    @EqualsAndHashCode.Include
    private UUID id;
    private String name;

    @Setter(AccessLevel.PACKAGE)
    private Integer quantity;

    static Item brandNew(final String name, final Integer quantity) {
        var item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);
        item.setQuantity(quantity);
        return item;
    }

}
