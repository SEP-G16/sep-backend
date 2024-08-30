package com.devx.menu_service.service;

import com.devx.menu_service.exception.BadRequestException;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class MenuService{
    private final MenuItemRepository menuItemRepository;
    private final Scheduler jdbcScheduler;

    public static Logger LOG = LoggerFactory.getLogger(MenuService.class);

    public MenuService(
            @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
            MenuItemRepository menuItemRepository
            ){
        this.jdbcScheduler = jdbcScheduler;
        this.menuItemRepository = menuItemRepository;
    }


    private Mono<MenuItem> insertMenuItemToDatabase(MenuItem menuItem)
    {
        if(menuItem.hasNullAttributes())
        {
            throw new BadRequestException();
        }
        else {
            return Mono.fromCallable(() -> menuItemRepository.save(menuItem)).subscribeOn(jdbcScheduler);
        }
    }

    public ResponseEntity<Mono<MenuItem>> addMenuItem(MenuItem menuItem)
    {
        try {
            return ResponseEntity.ok(insertMenuItemToDatabase(menuItem));
        }
        catch (BadRequestException e)
        {
            LOG.error("Error saving menu item");
            return ResponseEntity.internalServerError().build();
        }
    }

    private Flux<MenuItem> getAllMenuItemsFromDatabase() {
        return Mono.fromCallable(menuItemRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    public ResponseEntity<Flux<MenuItem>> getAllMenuItems()
    {
        return ResponseEntity.ok(getAllMenuItemsFromDatabase());
    }
}
