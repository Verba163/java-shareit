package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}