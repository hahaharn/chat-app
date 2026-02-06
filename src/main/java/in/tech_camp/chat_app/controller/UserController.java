package in.tech_camp.chat_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.LoginForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.service.UserService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor

public class UserController {
  private final UserRepository userRepository;
  private final UserService userService;

  // 新規登録画面にアクセスしたとき
  @GetMapping("/users/sign_up")
  // 入力を受け付けるための空の入れ物を準備する
  public String showSignUp(Model model){
    model.addAttribute("userForm", new UserForm());
    //「new UserForm()」はUserForm.javaのインスタンスを作成している
    // ()内の前側：ビュー側で呼び出すときの名前、後ろ側：値を格納
    // ()内の前側：SignUp.htmlが<form~ th:object="${userForm}">となっているので呼び出し名を"userForm"としている
    // ()内の後ろ側：UserForm(LoginForm.javaに記載)という型の空のインスタンスを作り、ユーザーが入力する情報を入れるための箱を1つ作り出している
    // ユーザーが情報を入力すると値が入ることになる
    return "users/signUp";  //ここはファイル名（先頭に/なし）
    // users/sign_upを表示させて＋Modelに格納した情報をこのHTMLファイルに届けてという意味
  }

  // 新規登録画面で送信ボタンが押されたとき
  @PostMapping("/user")
  /* @PostMapping：フォーム送信などの「保存・投稿」のアクセスがあったときに使う。
   （）内のURLに送られてきたデータを受け取る窓口*/
  public String createUser(@ModelAttribute("userForm") UserForm userForm, Model model){
  /*・引数内はフォームの情報。すぐ下でこの情報をUserEntityに移し替えて、UserRepositoryでDBに挿入する
    ・@ModelAttribute：フォームの入力データをJavaのオブジェクトに自動で詰め込んだり、画面にデータを渡したりする
    ・@ModelAttribute()の()内は、ビューで呼び出すときの名称
    ・@ModelAttribute("userForm") UserForm userForm：@GetMappingで用意していた「UserForm」という箱に、入力された内容が詰められて戻ってきた状況
    ・Model model：戻ってきたときに新しく用意された空の入れ物。エラーが起きて再度入力するときとかに必要なので。
  */
    // 画面用の箱(UserForm）から取り出したデータを保存用の箱（UserEntity）に移し替える
    UserEntity userEntity = new UserEntity();
    userEntity.setName(userForm.getName());
    userEntity.setEmail(userForm.getEmail());
    userEntity.setPassword(userForm.getPassword());

    try {
      userService.createUserWithEncryptedPassword(userEntity);
      //UserService.javaで作ったメソッド：パスワードを暗号化してDBに保存する
    } catch (Exception e) {
      //新規登録失敗したら、入力した内容（userForm）をModelに入れ直して、もう一度登録画面を表示
      System.out.println("エラー：" + e);
      model.addAttribute("userForm", userForm);
      return "users/signUp";
    }

    return "redirect:/";
  }


  // ログイン画面にアクセスされたとき
  @GetMapping("/users/login")
  // ↓入力を受付けるための空の入れ物を用意
  public String loginForm(Model model){
    model.addAttribute("loginForm", new LoginForm());
    //（）内前：HTMLで呼び出すときの名前、後：LoginForm.javaに記載のクラスをもとにインスタンス（空の入れ物）を作成
    return "users/login";
  }


  // ログインに失敗したとき
  @GetMapping("/login")
  public String login(@RequestParam(value = "error", required = false) String error, @ModelAttribute("loginForm") LoginForm loginForm, Model model) {
    if (error != null) {
      model.addAttribute("loginError", "メールアドレスかパスワードが間違っています。");
    }
    return "users/login";
  }
}