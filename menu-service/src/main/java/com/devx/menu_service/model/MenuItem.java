package com.devx.menu_service.model;

import com.devx.menu_service.enums.MenuItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    private double price;

    @ElementCollection
    private List<String> ingredients;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "menu_item_add_ons",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "add_on_id")
    )
    private List<AddOn> addOns;

    private String imageUrl;
    private String cuisine;

    @ElementCollection
    private List<String> tags;

    @Enumerated(EnumType.ORDINAL)
    private MenuItemStatus status;

    public boolean hasNullAttributes() {
        return id == null ||
                name == null ||
                shortDescription == null ||
                longDescription == null ||
                category == null ||
                ingredients == null ||
                addOns == null ||
                imageUrl == null ||
                cuisine == null ||
                tags == null ||
                status == null;
    }
}
