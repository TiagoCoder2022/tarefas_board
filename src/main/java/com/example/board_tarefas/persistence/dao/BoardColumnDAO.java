package com.example.board_tarefas.persistence.dao;

import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import com.example.board_tarefas.persistence.entity.BoardColumnKindEnum;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class BoardColumnDAO {
    private final JdbcTemplate jdbcTemplate;

    public BoardColumnDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<BoardColumnEntity> BOARDCOLUMN_ROW_MAPPER =
            (rs, rowNum) -> {

                BoardEntity board = new BoardEntity();
                board.setId(rs.getLong("board_id"));

                return new BoardColumnEntity(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("column_order"),
                        BoardColumnKindEnum.valueOf(rs.getString("kind")),
                        board
                );
            };

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

        if (keyHolder.getKey() != null) {
            entity.setId(keyHolder.getKey().longValue());
        }

        return entity;
    }

    public List<BoardColumnEntity> findByBoardId(Long boardId) {
        String sql = """
                SELECT 
                    id,
                    name,
                    column_order,
                    kind,
                    board_id
                FROM boards_columns
                WHERE board_id = ?
                ORDER BY column_order
                """;

        return jdbcTemplate.query(sql, BOARDCOLUMN_ROW_MAPPER, boardId);
    }
}
