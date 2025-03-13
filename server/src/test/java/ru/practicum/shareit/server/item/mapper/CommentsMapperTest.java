package ru.practicum.shareit.server.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.item.dto.CommentsDto;
import ru.practicum.shareit.server.item.model.Comments;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentsMapperTest {

    private CommentsMapper commentsMapper;

    @BeforeEach
    public void setUp() {
        commentsMapper = new CommentsMapper();
    }

    @Test
    public void toCommentsDto_ShouldReturnCorrectCommentsDto() {

        User author = new User(1L, "AuthorName","Test@mail.ru");
        Item item = new Item();
        Comments comment = Comments.builder()
                .id(1L)
                .text("This is a comment")
                .item(item)
                .author(author)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        CommentsDto result = commentsMapper.toCommentsDto(comment);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(comment.getId());
        assertThat(result.getText()).isEqualTo(comment.getText());
        assertThat(result.getItemId()).isEqualTo(comment.getItem().getId());
        assertThat(result.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(result.getCreated()).isEqualTo(comment.getCreated());
    }

    @Test
    public void toCommentsEntity_ShouldReturnCorrectCommentsEntity() {

        User author = new User(1L, "AuthorName", "Test@mail.ru");
        Item item = new Item();
        CommentsDto commentsDto = CommentsDto.builder()
                .text("This is a comment")
                .build();

        Comments result = commentsMapper.toCommentsEntity(commentsDto, item, author);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo(commentsDto.getText());
        assertThat(result.getItem()).isEqualTo(item);
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getCreated()).isNotNull();
    }
}
