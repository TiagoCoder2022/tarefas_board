package com.example.board_tarefas.persistence.dao;

import com.example.board_tarefas.dto.BoardColumnDTO;
import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import com.example.board_tarefas.persistence.entity.BoardColumnKindEnum;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import com.example.board_tarefas.persistence.entity.CardEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static final RowMapper<BoardColumnDTO> BOARD_COLUMN_DTO_ROW_MAPPER =
            (rs, rowNum) -> new BoardColumnDTO(
                    rs.getLong("id"),
                    rs.getString("name"),
                    BoardColumnKindEnum.valueOf(rs.getString("kind")),
                    rs.getInt("cards_amount")
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
            ps.setString(2, entity.getKind().name());
            ps.setLong(3, entity.getBoard().getId());
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

    public List<BoardColumnDTO> findByBoardIdWithDetails(Long boardId) {
        String sql = """
                SELECT
                     bc.id,
                     bc.name,                     
                     bc.kind,
                     (
                         SELECT COUNT(c.id)
                         FROM cards c
                         WHERE c.board_column_id = bc.id
                     ) AS cards_amount
                 FROM boards_columns bc
                 WHERE bc.board_id = ?
                 ORDER BY bc.column_order;              
                """;

        return jdbcTemplate.query(sql, BOARD_COLUMN_DTO_ROW_MAPPER, boardId);
    }

    public Optional<BoardColumnEntity> findById(Long columnId) {
        String sql = """
            SELECT                     
                bc.id,
                bc.name,                    
                bc.column_order,
                bc.kind,
                bc.board_id,
                c.id AS card_id,
                c.title,
                c.description                    
            FROM boards_columns bc
            LEFT JOIN cards c
                ON c.board_column_id = bc.id
            WHERE bc.id = ?;                
            """;

        return jdbcTemplate.query(sql, rs -> {
            BoardColumnEntity column = null;
            List<CardEntity> cards = new ArrayList<>();

            while (rs.next()) {
                if (column == null) {
                    BoardEntity board = new BoardEntity();
                    board.setId(rs.getLong("board_id"));

                    column = new BoardColumnEntity(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getInt("column_order"),
                            BoardColumnKindEnum.valueOf(rs.getString("kind")),
                            board
                    );
                }

                Long cardId = rs.getLong("card_id");
                if (!rs.wasNull()) {
                    CardEntity card = new CardEntity();
                    card.setId(cardId);
                    card.setTitle(rs.getString("title"));
                    card.setDescription(rs.getString("description"));
                    cards.add(card);
                }
            }

            if (column != null) {
                column.setCards(cards);
                return Optional.of(column);
            }

            return Optional.empty();
        }, columnId);
    }
}
