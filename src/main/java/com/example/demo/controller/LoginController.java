package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.HistoryDto;
import com.example.demo.model.LoginModel;

@Controller
public class LoginController {

	/** メッセージID「1」を指定して、messages.propertiesからメッセージを取得1 */
    private final MessageSource messageSource;

    public LoginController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
	

    /** ログイン画面を表示 */
    @GetMapping("/login") // RequestMappingアノテーション無しで単独でパス指定可能
    public String getLogin(
    		Model model, 
    		@ModelAttribute LoginModel loginModel,
    		@RequestParam(value = "error", required = false) String error
    		) {
    	
    	// エラーパラメータが存在する場合にのみ、エラーメッセージをModel格納
        if (error != null) {
            String errorMessage = messageSource.getMessage("1", null, null);
            model.addAttribute("errorMessage", errorMessage);
        }
   	

    	
    	
    	
    	// 画面に表示する履歴の作成
    	List<HistoryDto> historyDtoList = new ArrayList<HistoryDto>();
    	HistoryDto historyDto = new HistoryDto();
    	historyDto.setDateStr("2025/07/01");
    	historyDto.setHistoryText("新規作成");
    	historyDtoList.add(historyDto);
    	historyDto = new HistoryDto();
    	historyDto.setDateStr("2025/08/01");
    	historyDto.setHistoryText("「すぐに表示」ボタン作成");
    	historyDtoList.add(historyDto);
    	historyDto = new HistoryDto();
    	historyDto.setDateStr("2025/09/01");
    	historyDto.setHistoryText("ログインボタン作成");
    	historyDtoList.add(historyDto);
    	model.addAttribute("historyDtoList", historyDtoList);
        return "login";
    }

}   
  
