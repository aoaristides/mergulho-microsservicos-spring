package br.com.makersweb.delivery.tracking.domain;

import br.com.makersweb.delivery.tracking.domain.exception.DomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author andersonorione
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    private ContactPoint sender;
    private ContactPoint recipient;

    private List<Item> items = new ArrayList<>();

    public static Delivery draft() {
        var delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(final String name, final int quantity) {
        var item = Item.brandNew(name, quantity);
        items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(final UUID itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void changeItemQuantity(final UUID itemId, final int quantity) {
        var item = getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow();

        item.setQuantity(quantity);
        calculateTotalItems();
    }

    public void removeItems() {
        items.clear();
        calculateTotalItems();
    }

    public void editPreparationDetails(final PreparationDetails preparationDetails) {
        verifyIfCanBeEdited();
        this.setSender(preparationDetails.getSender());
        this.setRecipient(preparationDetails.getRecipient());
        this.setDistanceFee(preparationDetails.getDistanceFee());
        this.setCourierPayout(preparationDetails.getCourierPayout());

        this.setExpectedDeliveryAt(OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveryTime()));
        this.setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    public void place() {
        verifyIfCanBePlaced();
        this.changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void pickUp(final UUID courierId) {
        this.setCourierId(courierId);
        this.changeStatusTo(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(OffsetDateTime.now());
    }

    public void markAsDelivered() {
        this.changeStatusTo(DeliveryStatus.DELIVERED);
        this.setFulfilledAt(OffsetDateTime.now());
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems() {
        int totalItems = getItems().stream().mapToInt(Item::getQuantity).sum();
        setTotalItems(totalItems);
    }

    private void verifyIfCanBePlaced() {
        if (!isFilled()) {
            throw new DomainException("Delivery must have at least one item");
        }

        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException("Delivery can only be placed in draft status");
        }
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException("Delivery can not be edited after draft status");
        }
    }

    private boolean isFilled() {
        return this.getSender() != null && this.getRecipient() != null && this.getTotalCost() != null;
    }

    private void changeStatusTo(final DeliveryStatus newStatus) {
        if (newStatus != null && this.getStatus().canNotChangeTo(newStatus)) {
            throw new DomainException("Invalid status transition from " + this.getStatus() + " to " + newStatus);
        }
        this.setStatus(newStatus);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails {

        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;

    }

}
