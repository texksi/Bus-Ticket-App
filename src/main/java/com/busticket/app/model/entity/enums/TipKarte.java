package com.busticket.app.model.entity.enums;

public enum TipKarte {
    STUDENT(0.80),
    STANDARD(1.00),
    VIP(1.50);

    private final double koeficijent;

    TipKarte(double koeficijent) {
        this.koeficijent = koeficijent;
    }

    public double getKoeficijent() {
        return koeficijent;
    }
}
