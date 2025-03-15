package ru.practicum.shareit.server.item.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCommentsSerialization() throws Exception {
        Comments comments = Comments.builder()
                .id(1L)
                .text("Nice item!")
                .created(new Timestamp(System.currentTimeMillis()))
                .build();

        String json = objectMapper.writeValueAsString(comments);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"text\":\"Nice item!\"");
    }

    @Test
    public void testCommentsDeserialization() throws Exception {
        String json = "{\"id\":1,\"text\":\"Nice item!\",\"created\":\"2023-10-10T10:10:10.000+00:00\"}";

        Comments comments = objectMapper.readValue(json, Comments.class);

        assertThat(comments.getId()).isEqualTo(1L);
        assertThat(comments.getText()).isEqualTo("Nice item!");
    }
}