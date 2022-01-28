package ir.maktab.homeservice.entities.orders;

public enum OrderStatus {
    WAITING_FOR_OFFERS,
    WAITING_FOR_SELECT,
    WAITING_ARRIVAL,
    BEGUN,
    DONE,
    PAID;
}
