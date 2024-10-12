package com.devx.order_service.message.parser;

import com.devx.order_service.dto.MenuItemDto;
import com.devx.order_service.exception.NullFieldException;
import com.devx.order_service.model.AddOn;
import com.devx.order_service.model.MenuItem;
import com.devx.order_service.repository.AddOnRepository;
import com.devx.order_service.repository.MenuItemRepository;
import com.devx.order_service.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MenuItemMessageParser {

    private final MenuItemRepository menuItemRepository;
    private final AddOnRepository addOnRepository;

    public static Logger LOG = LoggerFactory.getLogger(MenuItemMessageParser.class);

    public MenuItemMessageParser(MenuItemRepository menuItemRepository, AddOnRepository addOnRepository){
        this.addOnRepository = addOnRepository;
        this.menuItemRepository = menuItemRepository;
    }

    private AddOn addAddOn(AddOn addOn) {
        if (addOn.getId() == null && addOn.getName() == null) {
            throw new NullFieldException("Both ID and name cannot be null for an AddOn");
        }
        Optional<AddOn> existingAddOnOptional = addOnRepository.findById(addOn.getId());
        return existingAddOnOptional.orElseGet(() -> addOnRepository.save(addOn));
    }

    private List<AddOn> saveAddOns(List<AddOn> addOns)
    {
        List<AddOn> saved = new ArrayList<>();
        for(AddOn addOn : addOns)
        {
            saved.add(addAddOn(addOn));
        }
        return saved;
    }

    public void addMenuItem(MenuItemDto menuItemDto)
    {
        LOG.info("Adding menu item: {}", menuItemDto.toString());
        if(menuItemDto.hasNullFields())
        {
            throw new NullFieldException("MenuItemDto has null fields");
        }
        MenuItem menuItem = AppUtils.MenuItemUtils.dtoToEntity(menuItemDto);
        menuItem.setAddOns(saveAddOns(menuItem.getAddOns()));
        menuItemRepository.save(menuItem);
    }

}
