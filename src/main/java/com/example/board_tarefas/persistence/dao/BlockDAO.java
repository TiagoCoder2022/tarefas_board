package com.example.board_tarefas.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import java.time.OffsetDateTime;

import static com.example.board_tarefas.persistence.converter.OffsetDateTimeConverter.toTimestamp;

public class BlockDAO {
    private final JdbcTemplate jdbcTemplate;

    public BlockDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void block(String reason, Long cardId) {
        var sql = "INSERT INTO blocks (blocked_at, block_reason, cards_id) VALUES (?, ?, ?);";

        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            preparedStatement.setString(2, reason);
            preparedStatement.setLong(3, cardId);

            return preparedStatement;
        });
    }
}
