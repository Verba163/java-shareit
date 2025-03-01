package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class CommentsMapper {
    public CommentsDto toCommentsDto(Comments comment) {

        return CommentsDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItem().getId())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public Comments toCommentsEntity(CommentsDto commentsDto, Item item, User author) {
        return Comments.builder()
                .text(commentsDto.getText())
                .item(item)
                .author(author)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }
}