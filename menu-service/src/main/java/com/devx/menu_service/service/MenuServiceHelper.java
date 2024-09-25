package com.devx.menu_service.service;

import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.repository.AddOnRepository;
import com.devx.menu_service.repository.CategoryRepository;
import com.devx.menu_service.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MenuServiceHelper {

    public static final Logger LOG = LoggerFactory.getLogger(MenuServiceHelper.class);

    protected final MenuItemRepository menuItemRepository;

    private final AddOnServiceHelper addOnServiceHelper;

    public MenuServiceHelper(MenuItemRepository menuItemRepository, AddOnRepository addOnRepository) {
        this.menuItemRepository = menuItemRepository;
        this.addOnServiceHelper = new AddOnServiceHelper(addOnRepository);
    }

    private AddOn handleDuplicateAddOnInsert(AddOn addOn) {
        return addOnServiceHelper.handleDuplicateInsert(addOn);
    }

    protected MenuItem insertMenuItem(MenuItem menuItem) {
        try {
            List<AddOn> newOrExistingAddOns = menuItem.getAddOns();
            List<AddOn> savedAddOns = insertAddOns(newOrExistingAddOns);
            menuItem.setAddOns(savedAddOns);
            return menuItemRepository.save(menuItem);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<AddOn> insertAddOns(List<AddOn> addOns)
    {
        List<AddOn> savedAddOns = new ArrayList<>();
        for (AddOn addOn : addOns) {
            AddOn savedAddOn = handleDuplicateAddOnInsert(addOn);
            savedAddOns.add(savedAddOn);
        }
        return  savedAddOns;
    }
}
