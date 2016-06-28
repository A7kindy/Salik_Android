package com.me.salik.modal;

/**
 * Created by MAC on 6/23/16.
 */
public enum Car {
    ECONOMY("ECONOMY(QAR 30)", 0),
    SUV("SUV(QAR 60)",1),
    VIP("VIP(QAR 100)", 2),
    VVIP("VVIP(QAR 300)", 3),
    DELIVERY_SERVICE("QAR (20)", 4)
    ;

    private String stringValue;
    private int intValue;

    private Car(String stringValue, int intValue){
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
