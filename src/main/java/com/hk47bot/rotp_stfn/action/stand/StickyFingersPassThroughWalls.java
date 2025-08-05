package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.stand.StandAction;

public class StickyFingersPassThroughWalls extends StandAction {
    public StickyFingersPassThroughWalls(StandAction.Builder builder) {
        super(builder.holdType());
    }
}
