package ru.practicum.shareit.server.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequester(User requester);

}
