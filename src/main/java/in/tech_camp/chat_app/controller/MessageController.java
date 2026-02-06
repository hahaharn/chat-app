// メッセージ一覧画面のコントローラーを作成

package in.tech_camp.chat_app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MessageController {
  
  private final UserRepository userRepository;
  
  // ルートパスにアクセスすると「messages/index.html」を表示させる
  @GetMapping("/")
  public String showMessages(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
  // @AuthenticationPrincipal：認証済みのユーザーの情報を取得するために使用
    UserEntity user = userRepository.findById(currentUser.getId());
    model.addAttribute("user",user);
    return "messages/index";
  }
}