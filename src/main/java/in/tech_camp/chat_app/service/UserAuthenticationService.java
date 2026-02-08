package in.tech_camp.chat_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

/*・ログイン画面で入力されたメアドを頼りにDBからユーザーを探し（リポジトリ）、
  　Spring Securityが使える形に変換し（CustomUserDetail）Spring Securityへ渡す「橋渡し」の役職
  ・「DBから持ってきた生データ」を「セキュリティシステムが扱えるデータ」へ翻訳させて渡す役割
*/

@Service
@AllArgsConstructor
// emailを受け取ってDBを検索しCustomUserDetail（別ファイル）のインスタンスとして返す
public class UserAuthenticationService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(email);
    /*↑UserRepositoryクラスにあるfindByEmilメソッド。リポジトリを使って、
      DBにそのメアドのユーザーがいるか探しに行き、結果をuserEntityというオブジェクト（箱）に入れる*/
    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }
    return new CustomUserDetail(userEntity);
    /*・DBから見つかったuserEntity（自作の型）を
      　作成したCustomUserDetail（Spring Securityが読める型）の中に詰め込んでSpring Securityに返す
      ・newするのは、CustomUserDetailという通訳用のクラスで、
      　このクラスのゴールは、見つかったユーザー情報をCustomUserDetailという形式の封筒に入れて、
      　Spring Securityに返却することなので、Spring Securityに渡す際に
      　新しい封筒を作って中身を入れないといけないため
      （newするなら「型 変数名 = new クラス名」という書き方を短縮させていいらしい）
    */
  }
}