package com.slothproductions.riskybusiness.model;

/**
 * Created by riv on 16.03.14.
 */
public class ResourceDescription {
    final int quantity;
    final Resource type;

    public ResourceDescription(int qty, Resource t) {
        this.quantity = qty;
        this.type = t;
    }
}
