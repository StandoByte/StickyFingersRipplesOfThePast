package com.hk47bot.rotp_stfn.util;

import net.minecraft.client.renderer.model.ModelRenderer;

public class LayerInfo {
    public final ModelRenderer layer;
    public final String fieldName;

    public LayerInfo(ModelRenderer layer, String fieldName) {
        this.layer = layer;
        this.fieldName = fieldName;
    }
}

