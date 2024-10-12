package com.devx.menu_service.service.helper;

import com.devx.menu_service.model.AddOn;

import java.util.List;

public interface AddOnServiceHelper {
    AddOn addAddOn(AddOn addOn);

    List<AddOn> getAllAddOns();
}
