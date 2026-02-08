package com.example.board_tarefas.persistence.dao;

import com.example.board_tarefas.persistence.entity.BoardEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class BoardDAO {

    private final JdbcTemplate jdbcTemplate;

    public BoardDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<BoardEntity> BOARD_ROW_MAPPER =
            (rs, rowNum) -> new BoardEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    new ArrayList<>()
            );

    public BoardEntity insert(BoardEntity entity) {
        String sql = """
                INSERT INTO boards (name)
                VALUES (?)
                """;

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, entity.getName());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        return new BoardEntity(generatedId, entity.getName(), new ArrayList<>());
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM boards WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public Optional<BoardEntity> findById(Long id) {
        String sql = "SELECT * FROM boards WHERE id = ?";

        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, BOARD_ROW_MAPPER, id)
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public boolean exists(Long id) {
        String sql = "SELECT COUNT(1) FROM boards WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
