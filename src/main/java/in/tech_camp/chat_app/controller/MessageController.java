// メッセージ一覧画面のコントローラーを作成

package in.tech_camp.chat_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// ルートパスにアクセスすると「messages/index.html」を表示させる
public class MessageController {
  @GetMapping("/")
  public String showMessages() {
    return "messages/index";
  }
}