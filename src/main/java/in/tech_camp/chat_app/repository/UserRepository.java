package in.tech_camp.chat_app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.UserEntity;

@Mapper
public interface UserRepository {
  // 新規登録の情報をDBに保存するときに使用
  @Insert("INSERT INTO users (name, email, password) VALUES (#{name}, #{email}, #{password})")
  /*・＠Insert：データを新しく保存する
    ・中身：INSERT INTO テーブル名 (カラム名,...) VALUES (各カラムに入れる値)
    ・#{フィールド名}：渡されたオブジェクト（userForm）の中にあるフィールド名（GASの${}みたいな）*/ 
 @Options(useGeneratedKeys = true, keyProperty = "id")
  /*・@Options：入力されたデータの自動採番のIDをJava側で使いたいとき、java側に書き戻す設定などに使う
    ・useGeneratedKeys = true: DBが生成したキー（ID）を取得することを許可
    ・keyProperty = "id": 生成されたIDを、引数のオブジェクト（userForm）のどのフィールドに書き戻すかを指定。
    　これにより、メソッド実行後に userForm.getId() でIDが取得できるようになる。*/
  void insert(UserEntity user);
  /*・保存処理のメソッド。@Insert文と紐づき、Javaの窓口になっている。
    ・UserEntityを引数にしているので、@Insert文の中で#{}を書いたときに、
    　UserEntity.javaの中にあるフィールド名を直接見にいっているということ。
    ・「user」の部分は好きにつけていいらしい。このあとコントローラーで使う。
    ・インターフェース内ではpublicなどつけない*/

  /* ログインカスタマイズのときに使用
    （emailでユーザー情報を取得するメソッド：UserAuthenticationServiceファイルで使用）*/
  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmail(String email);
}