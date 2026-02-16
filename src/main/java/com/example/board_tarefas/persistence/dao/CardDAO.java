package com.example.board_tarefas.persistence.dao;

import com.example.board_tarefas.dto.CardDetailsDTO;
import com.example.board_tarefas.persistence.entity.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.Optional;

import static com.example.board_tarefas.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
@Repository
public class CardDAO {

    private final JdbcTemplate jdbcTemplate;

    public CardDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CardEntity insert(CardEntity entity) {
        var sql = """
            INSERT INTO cards (title, description, board_column_id) 
            VALUES (?, ?, ?);
            """;

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getDescription());
            ps.setLong(3, entity.getBoardColumn().getId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            entity.setId(keyHolder.getKey().longValue());
        }

        return entity;
    }

    public Optional<CardDetailsDTO> findById(Long id) {
        var sql = """
            SELECT c.id,
                   c.title,
                   c.description,
                   b.blocked_at,
                   b.block_reason,
                   c.board_column_id,
                   bc.name,
                   (SELECT COUNT(sub_b.id) 
                    FROM blocks sub_b 
                    WHERE sub_b.card_id = c.id) AS blocks_amount
            FROM cards c
            LEFT JOIN blocks b
                ON c.id = b.card_id
                AND b.unblocked_at IS NULL
            INNER JOIN boards_columns bc
                ON bc.id = c.board_column_id
            WHERE c.id = ?;
            """;

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                var blockReason = rs.getString("b.block_reason");
                var dto = new CardDetailsDTO(
                        rs.getLong("c.id"),
                        rs.getString("c.title"),
                        rs.getString("c.description"),
                        blockReason != null && !blockReason.isEmpty(),
                        toOffsetDateTime(rs.getTimestamp("b.blocked_at")),
                        blockReason,
                        rs.getInt("blocks_amount"),
                        rs.getLong("c.board_column_id"),
                        rs.getString("bc.name")
                );
                return Optional.of(dto);
            }
            return Optional.empty();
        }, id);
    }
}
