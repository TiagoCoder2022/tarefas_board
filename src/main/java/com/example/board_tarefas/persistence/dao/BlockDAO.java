package com.example.board_tarefas.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

import static com.example.board_tarefas.persistence.converter.OffsetDateTimeConverter.toTimestamp;
@Repository
public class BlockDAO {
    private final JdbcTemplate jdbcTemplate;

    public BlockDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void block(String reason, Long cardId) {
        var sql = "INSERT INTO blocks (blocked_at, block_reason, cards_id) VALUES (?, ?, ?);";

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql);
            ps.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            ps.setString(2, reason);
            ps.setLong(3, cardId);

            return ps;
        });
    }
}
