package in.tech_camp.chat_app.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.chat_app.entity.UserEntity;
import lombok.Data;

/*（ログインの仕組みをカスタマイズするクラスCustomUserDetailを定義）
  SUserAuthenticationServiceクラスから渡されるuserEntityをSpring Securityが読める型に翻訳する、翻訳用のクラス*/

/*・SpringSecurityの初期設定が「ユーザー名」と「パスワード」でログインするようになっているので、
  　それを違うものに変えたい場合、SpringSecurityの動作を上書きする必要がある
  ・SpringSecurityはログインの仕組みをカスタマイズできるように、その元となるクラスUserDetailsを用意している。
  　このUserDetailsをベースにカスタマイズしていく。*/

/*Spring SecurityはUserEntityの中身を知らないので、下記クラスで「自作のユーザー情報（UserEntity）」を
  Spring Securityが理解できる「認証用ユーザー形式（UserDetails）」に翻訳する通訳者の役割をしている*/
@Data
public class CustomUserDetail implements UserDetails {
/*implements UserDetails:Spring Securityが定める「ログインユーザーならこのメソッドを持っていてね」という
  共通ルール（UserDetails：インターフェース）にCustomUserDetail（自作）は従うことを宣言している*/
  private final UserEntity user;

  public CustomUserDetail(UserEntity user) {
  // コンストラクタ。役割: このクラスを作る瞬間に、本物のユーザーデータ(Entity)を受け取って内部にセットする
    this.user = user;
  }
  // ここまでで通訳の準備完了


  /*UserDetailsクラスで用意されているメソッドは全て@Overrideを使用して上書きを行う。上書きというよりは、
    UserDetails側で決められた「パスワードを教えて」などの質問に対して「うちのEntityではこの項目がそれにあたる」と回答するイメージ*/
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }
  /*・Collection：「リスト（名簿）」のような複数のデータをまとめた塊のこと
    ・GrantedAuthority：Spring Security用語で「与えられた権限」という意味
    ・<? extends ...>: Javaのジェネリクスという文法で「GrantedAuthority、もしくはそれを継承したクラスなら何でもOKだよ」という受け入れ幅を広げるための書き方
    →全体としては、『権限』のデータがいくつか詰まったリストを返すという意味*/
  /*「このユーザーが持っている『権限（管理者か一般ユーザーかなど）』を教えて！」という質問に対し、
   「今は何も持っていません」と答えている状態*/

  @Override
  public String getPassword() {
    return user.getPassword();   //Entityのパスワードを返す
  }

  @Override
  public String getUsername() {
    return user.getEmail();   //今回は「メールアドレス」をユーザー名として扱う
  }

  // 自作メソッド（Spring Securityのルールにはない）ので@Overrideつけない
  public Integer getId() {
    return user.getId();
  }

  // 自作メソッド
  public String getName() {
    return user.getName();
  }

  @Override
  public boolean isAccountNonExpired() {  //アカウントの有効期限が切れてないかの確認
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {  //アカウントロックされていないかの確認
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {  //パスの期限切れていないかの確認
    return true;
  }

  @Override
  public boolean isEnabled() {  //有効なアカウントかを確認
    return true;
  }
}