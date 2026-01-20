package br.com.makersweb.delivery.tracking.domain;

import br.com.makersweb.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    public void shoudChangeToPlaced() {
        var delivery = Delivery.draft();
        delivery.editPreparationDetails(createdValidPreparationDetails());
        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }

    @Test
    public void shoudNotToPlaced() {
        var delivery = Delivery.draft();

        assertThrows(DomainException.class, () -> {
            delivery.place();
        });

        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
        var sender = ContactPoint.builder()
                .zipCode("00000000")
                .street("Rua Test")
                .number("123")
                .complement("casa")
                .name("João Silva")
                .phone("43 9 8823-7877")
                .build();
        var recipient = ContactPoint.builder()
                .zipCode("00000001")
                .street("Rua Test1")
                .number("123")
                .complement("casa")
                .name("João Nelore")
                .phone("42 9 8823-7877")
                .build();
        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("15.00"))
                .courierPayout(new BigDecimal("5.00"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }

}