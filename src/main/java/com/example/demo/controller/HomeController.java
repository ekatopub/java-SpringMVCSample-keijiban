package com.example.demo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;// Modelをインポート
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.message.model.Message;
import com.example.demo.domain.message.service.MessageService;
import com.example.demo.domain.sendKind.service.SendKindService;
import com.example.demo.domain.sendKindMessage.model.SendKindMessage;
import com.example.demo.domain.sendKindMessage.service.SendKindMessageService;
import com.example.demo.model.GroupOrder;
import com.example.demo.model.MyModel;
import com.example.demo.service.PrintService;
import com.example.demo.util.SecuritySession; 

@Controller
@RequestMapping("/form") // 共通的なパス指定
public class HomeController {
	
	//@Autowiredを追加
	//@Autowired
	//@Qualifierを追加 Bean名を指定する　デフォルトでクラス名の最初を小文字にしたもの
	//@Qualifier("printServiceImplProduction")
	//PrintService printService;　//コンストラクタインジェクションでは使用しない

	//コンストラクタインジェクション
	private final PrintService printService;

	public HomeController(PrintService printService) {
		this.printService = printService;
	}

	@Autowired
	private SecuritySession securitySession;
	
	@Autowired //for Validation
	private MessageSource messageSource;
	
	@SuppressWarnings("unused")
	@Autowired //added for jpa
	private SendKindService sendKindService;
	
	@Autowired //added for Message Insert
	private MessageService messageService;
	

	@Autowired //added for JOINT
	private SendKindMessageService sendKindMessageService;
	

    // getメソッド（直接リンク含む）でアクセスされた場合の処理
    @GetMapping("") // 個別のパス指定
    public String readForm(Model model, @ModelAttribute MyModel myModel) {
    	
        //セッションからユーザーID取得しModel経由でViewに渡す
        model.addAttribute("loginUserId", securitySession.getUsername());
    	
        return "form"; // 表示するhtmlのパス指定（.html省略）。templatesの
                       // 下のパスを指定。
    
    
    }
 // postメソッドでアクセスされた場合の処理
    @PostMapping("/confirm") // 個別のパス指定
    public String confirm(Model model,
            @ModelAttribute
            @Validated(GroupOrder.class) MyModel myModel, // @Validatedでチェック対象
            BindingResult bindingResult, // バリデーションチェック結果を格納するオブジェクト
            Locale locale) // "en" (英語), "ja"　(日本語)等の地域情報を格納するオブジェクト
    {

    	//バリデーションエラーがある場合にtrue
    	//地域情報を格納するオブジェクト
    	if (bindingResult.hasErrors()) {
    		// Java側でエラーメッセージを取得する場合の記述方法
    		for (FieldError error : bindingResult.getFieldErrors()) {
 			String message = messageSource.getMessage(error,locale);
    		System.out.println("バリデーションエラー : " + message);
    		}

    		//Spring SecurityのセッションからのユーザーIDの再取得
    		model.addAttribute("loginUserId",securitySession.getUsername());

    		// バリデーションエラーの場合は"form"から先に遷移しない
    		return "form";
    	}
    	//AOP test
    	printService.print(myModel.getMessage());
    	
    	// O/Rマッパーのテスト
  /*  	SendKind sendKind = sendKindService.findByKindId("0001");
    	System.out.println(sendKind.getKindName());
    	//added for Message Insert	
    	String KIND_CD_MESSAGE = "0001";
    	Message message = new Message();
    	//message.setId(1); // 常にid=1をアップデート
    	System.out.println(messageService.deleteAll() + " 件削除 ");
    	message.setKindId(KIND_CD_MESSAGE);
    	message.setText(myModel.getMessage());

    	messageService.postText(message);*/
    	
    	String KIND_CD_MESSAGE = "0001";
    	int USE_ID = 1; // 常に1を使用

    	// メッセージの登録・更新
    	Message message = new Message();
    	message.setId(USE_ID);
    	message.setKindId(KIND_CD_MESSAGE);
    	message.setText(myModel.getMessage());
    	messageService.postText(message);

    	// 表示内容の取得
    	List<SendKindMessage> SendKindMessageList =
    	sendKindMessageService.findDisplayTextById(USE_ID);
    	String displayMessage = SendKindMessageList.get(0).getKindName() +
    	":" + SendKindMessageList.get(0).getText(); // 実際には1件取得なので添字0で固定

    	myModel.setMessage(displayMessage);
    	

    	
    	return "confirm"; // 表示するhtmlのパス指定。
    }
}