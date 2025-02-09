package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.stand.StandEntityAction;

public class StickyFingersPassThroughWalls extends StandEntityAction {
    public StickyFingersPassThroughWalls(StandEntityAction.Builder builder) {
        super(builder.holdType());
    }
}
