package com.devx.menu_service.service;

import com.devx.menu_service.dto.request.AddMenuItemRequestBody;
import com.devx.menu_service.exception.BadRequestException;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.repository.AddOnRepository;
import com.devx.menu_service.repository.CategoryRepository;
import com.devx.menu_service.repository.MenuItemRepository;
import com.devx.menu_service.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class MenuService extends MenuServiceHelper {
    private final Scheduler jdbcScheduler;

    public static Logger LOG = LoggerFactory.getLogger(MenuService.class);

    public MenuService(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, MenuItemRepository menuItemRepository, AddOnRepository addOnRepository) {
        super(menuItemRepository, addOnRepository);
        this.jdbcScheduler = jdbcScheduler;
    }

    private Mono<MenuItem> insertMenuItemToDatabase(MenuItem menuItem) {

        return Mono.fromCallable(() -> insertMenuItem(menuItem)).subscribeOn(jdbcScheduler);
    }

    public ResponseEntity<Mono<MenuItem>> addMenuItem(AddMenuItemRequestBody addMenuItemRequestBody) {

        try {
            if (addMenuItemRequestBody.hasNullFields()) {
                throw new BadRequestException();
            }
            MenuItem menuItem = AppUtils.MenuItemFromAddMenuItemRequestBody(addMenuItemRequestBody);
            return new ResponseEntity<>(insertMenuItemToDatabase(menuItem), HttpStatus.CREATED);
        } catch (BadRequestException e) {
            LOG.error("Error saving menu item : Bad Request");
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            LOG.error("Error saving menu item");
            return ResponseEntity.internalServerError().build();
        }
    }

    private Flux<MenuItem> getAllMenuItemsFromDatabase() {
        return Mono.fromCallable(menuItemRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    public ResponseEntity<Flux<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(getAllMenuItemsFromDatabase());
    }
}
