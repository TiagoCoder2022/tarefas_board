package com.example.board_tarefas.ui;

import com.example.board_tarefas.exception.MenuExceptionHandler;
import com.example.board_tarefas.persistence.entity.BoardColumnEntity;
import com.example.board_tarefas.persistence.entity.BoardColumnKindEnum;
import com.example.board_tarefas.persistence.entity.BoardEntity;
import com.example.board_tarefas.service.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.example.board_tarefas.persistence.entity.BoardColumnKindEnum.*;
@Component
public class MainMenu {
    private final BoardService boardService;
    private final BoardQueryService boardQueryService;
    private final BoardColumnQueryService boardColumnQueryService;
    private final CardQueryService cardQueryService;
    private final CardService cardService;
    private final MenuExceptionHandler exceptionHandler;
    private final Scanner scanner = new Scanner(System.in);

    public MainMenu(BoardService boardService,
                    BoardQueryService boardQueryService,
                    BoardColumnQueryService boardColumnQueryService,
                    CardQueryService cardQueryService,
                    CardService cardService,
                    MenuExceptionHandler exceptionHandler
    ) {
        this.boardService = boardService;
        this.boardQueryService = boardQueryService;
        this.boardColumnQueryService = boardColumnQueryService;
        this.cardQueryService = cardQueryService;
        this.cardService = cardService;
        this.exceptionHandler = exceptionHandler;
    }

    public void execute() {
        System.out.println("Bem vindo ao gerenciamento de boards, escolha a opcao desejada");

        var option = -1;
        while (true) {
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opcao invalida, informe uma opcao do menu");
            }
        }
    }

    private void createBoard() {
        var entity = new BoardEntity();
        System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim, informe quantas, se não digite '0'");
        var additionalColumns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initalColumn = createColumn(initialColumnName, INITIAL, 0);
        columns.add(initalColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente do board");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final do board");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do board");
        var canceledColumnName = scanner.next();
        var canceledColumn = createColumn(canceledColumnName, CANCELED, additionalColumns + 2);
        columns.add(canceledColumn);

        entity.setBoardsColumns(columns);

        boardService.create(entity);
    }

    private void selectBoard() {
        System.out.println("Informe o id do board que deseja selecionar");
        var id = scanner.nextLong();
        var optional = boardQueryService.findById(id);
        optional.ifPresentOrElse(
                b -> new BoardMenu(b, boardQueryService, boardColumnQueryService, cardQueryService, cardService, exceptionHandler).execute(),
                () -> System.out.printf("Não foi possível encontrar um board com id %s\n", id)
        );
    }

    private void deleteBoard() {
        System.out.println("Informe o id do board que será excluído");
        var id = scanner.nextLong();

        if (boardService.delete(id)) {
            System.out.println();
        } else {
            System.out.printf("Não foi possível encontrar um board com id %s\n", id);
        }

    }

    private BoardColumnEntity createColumn(String name, BoardColumnKindEnum kind, int order) {
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setColumnOrder(order);
        return boardColumn;
    }
}
