package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        // BCryptPasswordEncoderのインスタンスを作成
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // ハッシュ化したいパスワード（平文）
        String rawPassword = "mikunegi";
        //String rawPassword = "password";hogefuga　mikunegi

        // パスワードをハッシュ化
        String hashedPassword = encoder.encode(rawPassword);

        // ハッシュ化されたパスワードをコンソールに出力
        System.out.println("元のパスワード: " + rawPassword);
        System.out.println("ハッシュ化されたパスワード: " + hashedPassword);
        
        
        /* 今回は手動でハッシュ化されたパスワードをDBに入れる
        INSERT INTO user (user_id, password, user_name)
        VALUES ('ユーザーID', 'ハッシュ化されたパスワードをここに貼り付け', 'ユーザー名');
        */
    }
}