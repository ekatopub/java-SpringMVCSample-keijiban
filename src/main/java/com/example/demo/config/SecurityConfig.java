package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** セキュリティの対象外を設定 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // セキュリティを適用しない
        // htmlのscriptタグで取り込む外部ライブラリー、データベースのコンソール
        // ページ、等は適用除外にする必要あり。
        web
            .ignoring()
            .antMatchers("/webjars/**")
            .antMatchers("/css/**")
            .antMatchers("/js/**");
    }

    /** セキュリティの各種設定 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ログイン不要ページの設定
        http
            .authorizeRequests()
                .antMatchers("/login").permitAll() // 直接リンクOK
                .anyRequest().authenticated(); // それ以外は直接リンクNG

        // ログイン処理
        http
            .formLogin()
                .loginProcessingUrl("/login") // ログイン処理のURL
                .loginPage("/login") // ログインページの指定
                .failureUrl("/login?error") // ログイン失敗時の遷移先（?以降はGETメソッドのクエリパラメータ）
                .usernameParameter("userId") // ログインページのユーザーID(name属性で指定)
                .passwordParameter("password") // ログインページのパスワード(name属性で指定)
           //   .defaultSuccessUrl("/form", true); // 成功後の遷移先 DB使用前
                .defaultSuccessUrl("/board/", true); // 成功後の遷移先

        // ログアウト処理
        http
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout");
}

/** 認証の設定 */
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    
    PasswordEncoder encoder = passwordEncoder();

    // ユーザーデータ認証
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(encoder);
}
}
