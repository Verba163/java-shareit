package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCase(String query);

    @Query("""
             SELECT i FROM Item i
             LEFT JOIN FETCH i.comments
             LEFT JOIN FETCH i.bookings b\s
             WHERE i.id = :id
            \s""")
    Optional<Item> findByIdInFull(@Param("id") Long id);

}
