package com.example.board_tarefas.persistence.dao;

import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import com.example.board_tarefas.persistence.entity.BoardColumnKindEnum;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;

public class BoardColumnDAO {
    private final JdbcTemplate jdbcTemplate;

    public BoardColumnDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<BoardColumnEntity> BOARDCOLUMN_ROW_MAPPER =
            (rs, rowNum) -> new BoardColumnEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("column_order"),
                    BoardColumnKindEnum.valueOf(rs.getString("kind")),
                    new BoardEntity(rs.getLong("board_id"), null)
            );

    public BoardColumnEntity insert(BoardColumnEntity entity) {
        String sql = """
                INSERT INTO boards_columns (name, column_order, kind, board_id)
                VALUES (?, ?, ?, ?);
                """;

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getColumnOrder());
            ps.setString(3, entity.getKind().name());
            ps.setLong(4, entity.getBoard().getId());
            return ps;
        }, keyHolder);

        var generatedId = keyHolder.getKey();
        if (generatedId != null) {
            entity.setId(generatedId.longValue());
        }

        return entity;
    }

    public List<BoardColumnEntity> findByBoardId(Long id) {
        return null;
    }
}
