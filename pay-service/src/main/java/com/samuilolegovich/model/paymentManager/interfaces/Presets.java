package com.samuilolegovich.model.paymentManager.interfaces;

import com.samuilolegovich.enums.BooleanEnum;
import com.samuilolegovich.enums.StringEnum;

public interface Presets {
    void setPresets(BooleanEnum enums, boolean b);
    void setPresets(StringEnum enums, String s);
}
