package br.com.makersweb.delivery.tracking.domain;

import java.util.Arrays;
import java.util.List;

/**
 * @author andersonorione
 */
public enum DeliveryStatus {

    DRAFT,
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERED(IN_TRANSIT);

    private final List<DeliveryStatus> previousStatuses;

    DeliveryStatus(DeliveryStatus... previousStatuses) {
        this.previousStatuses = Arrays.asList(previousStatuses);
    }

    public boolean canNotChangeTo(DeliveryStatus newStatus) {
        var current = this;
        return !newStatus.previousStatuses.contains(current);
    }

    public boolean canChangeTo(DeliveryStatus newStatus) {
        return !canNotChangeTo(newStatus);
    }

}
