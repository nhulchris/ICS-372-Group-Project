package com.brewbite.model;

/**
 * Represents the lifecycle of an order:
 * PENDING -> IN_PROGRESS -> READY_FOR_PICKUP -> COMPLETED.
 * CANCELLED is a terminal state outside the normal flow.
 */
public enum OrderStatus {
    PENDING,
    IN_PROGRESS,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED
}
