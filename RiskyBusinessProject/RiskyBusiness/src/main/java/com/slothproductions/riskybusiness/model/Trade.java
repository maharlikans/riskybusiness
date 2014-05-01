package com.slothproductions.riskybusiness.model;

/**
 * Created by riv on 16.04.14.
 */
public class Trade implements java.io.Serializable {
    private final Player callee;
    private final Player partner;
    public final Player.ImmutablePlayer callee_id;
    public final Player.ImmutablePlayer partner_id;
    public final Integer sell_amount;
    public final Resource sell_type;
    public final Integer buy_amount;
    public final Resource buy_type;
    private Boolean confirmed;

    protected Trade(Player c, Player p, Integer sa, Resource st, Integer ba, Resource bt) {
        callee = c;
        callee_id = c.immutable;
        partner = p;
        partner_id = p.immutable;
        sell_amount = sa;
        sell_type = st;
        buy_amount = ba;
        buy_type = bt;
        confirmed = false;
    }

    protected void confirm(Player p) {
        if (p == partner && !confirmed && partner.hasResources(buy_type, buy_amount)) {
            confirmed = true;
        }
    }

    protected boolean isConfirmed() {
        return confirmed;
    }
}
