package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;// CORS対策
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // CORS対策

import com.example.demo.model.Board;
import com.example.demo.service.BoardService;
import com.example.demo.util.SecuritySession;

//@Controller
//@RequestMapping("/board")
@RestController // このクラスをRestControllerに変更
@RequestMapping("/api/board") // エンドポイントを /api/board に変更
@CrossOrigin(origins = "http://localhost:8080") // CORS対策
public class BoardApiController {

    private final BoardService boardService;
    private final SecuritySession securitySession;

    @Autowired //DI
    public BoardApiController(BoardService boardService, SecuritySession securitySession) {
        this.boardService = boardService;
        this.securitySession = securitySession;
    }

    /**
     * 新しい投稿を作成
     */
    @PostMapping("/post")
    public Board postBoard(@RequestBody Board board) {
        board.setUserId(securitySession.getUsername());
        board.setRegisterDate(LocalDateTime.now());
        boardService.postBoard(board);
        return board;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") int id) {//HTTPステータスコード（HttpStatus）とレスポンスボディを自由に設定できる
        String currentUserId = securitySession.getUsername();
        boardService.deleteBoardById(id, currentUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // SecurityExceptionが発生した場合にこのメソッドが呼び出される
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);//403 Forbiddenを返す
    }

}