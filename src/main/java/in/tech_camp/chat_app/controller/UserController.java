package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.LoginForm;
import in.tech_camp.chat_app.form.UserEditForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.service.UserService;
import in.tech_camp.chat_app.validation.ValidationOrder;
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
  public String createUser(@ModelAttribute("userForm") @Validated(ValidationOrder.class) UserForm userForm, BindingResult result, Model model){
  /*・引数内はフォームの情報。↓でこの情報をUserEntityに移し替えて、UserRepositoryでDBに挿入する
    ・@ModelAttribute("userForm") UserForm userForm：@GetMappingで用意していた「UserForm」という箱に、入力された内容が詰められて戻ってきた状況
    ・Model model：戻ってきたときに新しく用意された空の入れ物。エラーが起きて再度入力するときとかに必要なので。
    ・@Validated(ValidationOrder.class)、BindingResult result：バリデーションを実行するアノテーションと結果を受け取るBindingResultを追加 */
    userForm.validatePasswordConfirmation(result);  // userForm内で作ったメソッド

    /*リポジトリに追加したメアド重複チェックメソッドを使用し、メアドがすでに使用されていないかチェック。
      すでに存在している場合は、BindingResultオブジェクトにエラーを追加*/
    if (userRepository.existsByEmail(userForm.getEmail())) {
      result.rejectValue("email", "null", "Email already exists");
    }

    /*コントローラーでバリデーションエラーがあったらサインアップ画面に止まりエラー表示を行う処理。
      BindingResultを確認し、エラーがある場合はmodelに追加し、サインアップ画面を返す。
      UserFormのオブジェクトを渡すことで、再度サインアップ画面を表示させたときに、入力値が残るようになる*/
    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              //↑流れてきた複雑なエラー情報から「ユーザーに見せるためのメッセージ」だけを抜き出す作業
              .collect(Collectors.toList());
              //↑加工されたメッセージを再度1つの新しいリストList<String>にまとめ直す
      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("userForm", userForm);
      return "users/signUp";
    }

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
    /*@RequestParam(value = "error", required = false) String error：URLの情報
    ・value = "error"：URLに「?error」という文字が含まれていないかチェック
    ・required = false：errorがなくても怒らないで（エラーにしないで）という意味
    @ModelAttribute("loginForm") LoginForm loginForm：HTMLと「入力項目」を繋ぐ。フォームの情報
    ・ログイン画面（HTML）にあるメアドやパスの入力欄と、Javaのオブジェクトを結びつけるための空の箱
    Model model
    ・画面への運び屋。「ログインに失敗しました」というメッセージなどを入れて、HTML側に持ち帰るための道具*/
  }


  // ユーザー情報変更ボタンが押されたとき（今登録されている情報をDBから取って入力欄に下書きとして表示させる）
  @GetMapping("/users/{userId}/edit")
  //↑URLの一部を変数として扱うための書き方
  /*↓@PathVariable("userId")：URLの{userId}の部分をJavaの変数userIdとして取り出す。
    これで誰の情報を編集したいのかを特定できる。*/
  public String editUserForm(@PathVariable("userId") Integer userId, Model model) {
    UserEntity user = userRepository.findById(userId);  //userRepositoryの中のfindByIdメソッド

    /*編集画面専用の「空の箱（フォームオブジェクト）」を新しく作り、
      user（DBから持ってきた生データ）の中身を、userForm（画面表示用の箱）へ詰め替えていく*/
    UserEditForm userForm = new UserEditForm();
    userForm.setId(user.getId());
    userForm.setName(user.getName());
    userForm.setEmail(user.getEmail());

    // バケツ（Model）に入れて画面に渡す
    model.addAttribute("user", userForm);
    return "users/edit";
  }


  // ユーザー情報編集して更新されるとき
  @PostMapping("/users/{userId}")
  public String updateUser(@PathVariable("userId") Integer userId, @ModelAttribute("user") @Validated(ValidationOrder.class) UserEditForm userEditForm, BindingResult result, Model model) {
    String newEmail = userEditForm.getEmail();
    if (userRepository.existsByEmailExcludingCurrent(newEmail, userId)) {
      result.rejectValue("email", "error.user", "Email already exists");
    }
    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("user", userEditForm);
      return "users/edit";
    }

    UserEntity user = userRepository.findById(userId);
    user.setName(userEditForm.getName());
    user.setEmail(userEditForm.getEmail());

    try {
      userRepository.update(user);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      model.addAttribute("user", userEditForm);
      return "users/edit";
    }
    return "redirect:/";
  }
}