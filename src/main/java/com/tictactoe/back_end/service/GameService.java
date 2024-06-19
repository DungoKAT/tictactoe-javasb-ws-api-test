package com.tictactoe.back_end.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import com.tictactoe.back_end.storage.GameStorage;
import com.tictactoe.back_end.storage.UserStorage;

import com.tictactoe.back_end.model.Game;
import com.tictactoe.back_end.model.GameBoard;
import com.tictactoe.back_end.model.GamePlay;
import com.tictactoe.back_end.model.Player;
import com.tictactoe.back_end.model.TicTacToe;
import com.tictactoe.back_end.model.User;
import com.tictactoe.back_end.model.GameStatus;
import com.tictactoe.back_end.model.Move;

import com.tictactoe.back_end.exception.InvalidGameException;
import com.tictactoe.back_end.exception.InvalidParamException;
import com.tictactoe.back_end.exception.NotFoundException;

@Service
@AllArgsConstructor
public class GameService {
    private static final Logger logger = Logger.getLogger(GameService.class.getName());
    private GameStorage gameStorage;
    private UserStorage userStorage;

    public GameService() {
        gameStorage = GameStorage.getInstance();
        userStorage = UserStorage.getInstance();
    }

    public Collection<Game> getAllGames() {
        return gameStorage.getAllGames();
    }

    public Game getGame(String gameId) {
        Game game = gameStorage.getGameById(gameId);
        return game;
    }

    public Game createGame(String usernameX, Integer size) throws InvalidParamException {
        if (!userStorage.userExists(usernameX)) {
            throw new InvalidParamException("Usernae: " + usernameX + " doesn't exist");
        }
        User userX = userStorage.getUserByUsername(usernameX);
        Game game = new Game();
        Player playerX = game.getPlayerX();
        GameBoard gameBoard = new GameBoard();
        playerX.setPlayerId(userX.getUserId());
        playerX.setPlayerName(userX.getUsername());
        game.setPlayerX(playerX);
        gameBoard.setSize(size);
        gameBoard.setBoard(new String[size][size]);
        game.setGameBoard(gameBoard);
        gameStorage.addGame(game);
        return game;
    }

    public Game connectToGame(String usernameO, String gameId) throws InvalidParamException, InvalidGameException {
        logger.info("Provided game password: " + gameId);
        logger.info("Available game passwords: " + gameStorage.getAllGameIdKeys());
        
        if (!userStorage.userExists(usernameO)) {
            throw new InvalidParamException("Username: " + usernameO + " doesn't exist");
        }
        if (!gameStorage.containsGameById(gameId)) {
            throw new InvalidParamException("Game with provided password doesn't exist " + gameStorage.containsGameById(gameId));
        }
        
        Game game = gameStorage.getGameById(gameId);
        logger.info("Is PlayerO null?: " + game.getPlayerO());
        if (game.getPlayerO().getPlayerId() != null && game.getPlayerO().getPlayerName() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        }
        if (game.getGameHistory().getStartTime() == null) {
            game.getGameHistory().setStartTime(LocalDateTime.now());;
        }

        User userO = userStorage.getUserByUsername(usernameO);
        Player playerO = game.getPlayerO();
        playerO.setPlayerId(userO.getUserId());
        playerO.setPlayerName(userO.getUsername());
        game.setPlayerO(playerO);
        game.setStatus(GameStatus.IN_PROGRESS);
        gameStorage.addGame(game);
        return game;
    }

    public Game connectToRandomGame(String usernameO) throws InvalidParamException, NotFoundException {
        if (!userStorage.userExists(usernameO)) {
            throw new InvalidParamException("Username: " + usernameO + " doesn't exist");
        }
        Game game = gameStorage.getAllGames().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(() -> new NotFoundException("Game not found"));

        User userO = userStorage.getUserByUsername(usernameO);
        Player playerO = game.getPlayerO();
        playerO.setPlayerId(userO.getUserId());
        playerO.setPlayerName(userO.getUsername());
        game.setStatus(GameStatus.IN_PROGRESS);
        gameStorage.addGame(game);
        return game;
    }

    public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!gameStorage.containsGameById(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = gameStorage.getGameById(gamePlay.getGameId());
        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }

        String markType = gamePlay.getMarkType().getValue();
        Integer coordinateX = gamePlay.getCoordinateX();
        Integer coordinateY = gamePlay.getCoordinateY();
        GameBoard gameBoard = game.getGameBoard();

        if (coordinateX >= 0 && coordinateX < gameBoard.getSize() 
        && coordinateY >= 0 && coordinateY < gameBoard.getSize()
        && gameBoard.getBoard()[coordinateX][coordinateY] == null
        && markType == game.getTurn()) {
            logger.info("Position is null?: " + gameBoard.getBoard()[coordinateX][coordinateY]);
            logger.info("Turn?: " + game.getTurn());
            gameBoard.getBoard()[coordinateX][coordinateY] = markType;
        } else {
            logger.info("Position not null?: " + gameBoard.getBoard()[coordinateX][coordinateY]);
            return null;
        }

        Move move = new Move();
        move.setMarkType(markType);
        move.setPosition(new int[]{coordinateX, coordinateY});
        List<Move> allMoves = game.getGameHistory().getAllMoves();
        allMoves.add(move);

        if (game.getTurn() == "X") {
            game.setTurn("O");
        } else if (game.getTurn() == "O") {
            game.setTurn("X");
        }

        Boolean xWinner = game.checkWinner(gameBoard, TicTacToe.X);
        Boolean oWinner = game.checkWinner(gameBoard, TicTacToe.O);
        Boolean draw = game.checkDraw(gameBoard);
        if (xWinner) {
            endGame(TicTacToe.X, game);
        } else if (oWinner) {
            endGame(TicTacToe.O, game);
        } else if (draw) {
            endGame(TicTacToe.DRAW, game);
        }
        gameStorage.addGame(game);

        return game;
    }

    public Game surrender(String gameId, String playerSurrender) {
        Game game = gameStorage.getGameById(gameId);
        logger.info("Player Surrender?: " + playerSurrender);
        logger.info("Player Surrender is X: " + playerSurrender.equals("X"));
        logger.info("Player Surrender is O: " + playerSurrender.equals("O"));
        if (playerSurrender.equals("X")) {
            endGame(TicTacToe.O, game);
        } else if (playerSurrender.equals("O")) {
            endGame(TicTacToe.X, game);
        }
        gameStorage.addGame(game);
        return game;
    }

    public void endGame(TicTacToe ticTacToe, Game game) {
        User userX = userStorage.getUserById(game.getPlayerX().getPlayerId());
        User userO = userStorage.getUserById(game.getPlayerO().getPlayerId());
        game.setWinner(ticTacToe);
        game.getGameHistory().setEndTime(LocalDateTime.now());
        game.setStatus(GameStatus.FINISHED);
        userX.getGameHistories().add(game);
        userO.getGameHistories().add(game);
    }

    public Game terminateGame (String gameId) throws NotFoundException {
        Game game = gameStorage.getGameById(gameId);
        logger.info("Game Id Deleted: " + gameId);
        if (game == null) {
            throw new NotFoundException("Game not found");
        } else {
            game.setStatus(GameStatus.TERMINATED);
            gameStorage.deleteGameById(gameId);
        }
        return game;
    }
}
