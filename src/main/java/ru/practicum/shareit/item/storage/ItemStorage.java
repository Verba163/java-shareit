package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item addItem(Item item);

    Collection<Item> getAllItems();

    Item updateItem(Item item);

    void deleteItem(long id);

    Item getItemById(long id);

    Collection<Item> searchItemsByQuery(String query);
}
