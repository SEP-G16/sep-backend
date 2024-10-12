package com.devx.menu_service.service.helper;

import com.devx.menu_service.exception.AddOnNotFoundException;
import com.devx.menu_service.exception.BadRequestException;
import com.devx.menu_service.exception.NullFieldException;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.repository.AddOnRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddOnServiceHelperImpl implements AddOnServiceHelper{
    protected final AddOnRepository addOnRepository;

    public AddOnServiceHelperImpl(AddOnRepository addOnRepository) {
        this.addOnRepository = addOnRepository;
    }

    @Override
    public AddOn addAddOn(AddOn addOn) {
        // Check if both ID and name are null
        if (addOn.getId() == null && addOn.getName() == null) {
            throw new NullFieldException("Both ID and name cannot be null for an AddOn");
        }

        // If ID is null, try to find by name or save new AddOn
        if (addOn.getId() == null) {
            return addOnRepository.findByName(addOn.getName())
                    .orElseGet(() -> addOnRepository.save(addOn));
        }

        // If ID is present, check for AddOn by ID
        AddOn existingAddOn = addOnRepository.findById(addOn.getId())
                .orElseThrow(() -> new AddOnNotFoundException("AddOn Not Found"));

        // If name is null, return the existing AddOn
        if (addOn.getName() == null) {
            return existingAddOn;
        }

        // If names match, return the existing AddOn, else throw an exception
        if (!addOn.getName().equals(existingAddOn.getName())) {
            throw new BadRequestException("AddOn Details Mismatch");
        }
        return existingAddOn;
    }

    @Override
    public List<AddOn> getAllAddOns() {
        return addOnRepository.findAll();
    }
}
