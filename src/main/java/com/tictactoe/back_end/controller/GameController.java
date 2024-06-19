package com.tictactoe.back_end.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.tictactoe.back_end.controller.dto.ConnectRandomRequest;
import com.tictactoe.back_end.controller.dto.ConnectRequest;
import com.tictactoe.back_end.controller.dto.StartRequest;
import com.tictactoe.back_end.controller.dto.SurrenderRequest;
import com.tictactoe.back_end.model.Game;
import com.tictactoe.back_end.model.GamePlay;
import com.tictactoe.back_end.service.GameService;

import com.tictactoe.back_end.exception.InvalidGameException;
import com.tictactoe.back_end.exception.InvalidParamException;
import com.tictactoe.back_end.exception.NotFoundException;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "https://tictactoe-react-javasb-ws-test.netlify.app")
public class GameController {
    private final GameService gameService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/all")
    public ResponseEntity<Collection<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        log.info("get game: {}", gameId);
        Game game = gameService.getGame(gameId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/create")
    public ResponseEntity<Game> create(@RequestBody StartRequest startRequest) throws InvalidParamException {
        log.info("create game request: {}", startRequest);
        Game game = gameService.createGame(startRequest.getUsernameX(), startRequest.getSize());
        sendGameProgress(game);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
        log.info("connect request: {}", request);
        Game game = gameService.connectToGame(request.getUsernameO(), request.getGameId());
        sendGameProgress(game);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/connect-random")
    public ResponseEntity<Game> connectRandom(@RequestBody ConnectRandomRequest request) throws InvalidParamException, NotFoundException {
        log.info("connect random: {}", request);
        Game game = gameService.connectToRandomGame(request.getUsernameO());
        sendGameProgress(game);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/play")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        log.info("game play: {}", gamePlay);
        Game game = gameService.gamePlay(gamePlay);
        sendGameProgress(game);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/surrender")
    public ResponseEntity<Game> surrender(@RequestBody SurrenderRequest request) throws NotFoundException, InvalidGameException {
        log.info("surrender: {}", request);
        Game game = gameService.surrender(request.getGameId(), request.getPlayerSurrender());
        sendGameProgress(game);
        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/terminate/{gameId}")
    public ResponseEntity<Game> terminate(@PathVariable String gameId) throws NotFoundException, InvalidGameException {
        log.info("terminate: {}", gameId);
        Game game = gameService.terminateGame(gameId);
        sendGameProgress(game);
        return ResponseEntity.ok(game);
    }

    private void sendGameProgress(Game game) {
        log.info("path: /topic/gamedata");
        log.info("game: {}", game);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/", game);
    }
}
