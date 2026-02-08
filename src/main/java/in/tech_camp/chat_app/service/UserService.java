package in.tech_camp.chat_app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

// 保存対象のUserEntityを受け取り、パスワードを暗号化した上で保存する

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void createUserWithEncryptedPassword(UserEntity userEntity) {
  /*createUserWithEncryptedPassword：保存したいUserEntityを引数で受け取り、
    パスワードを暗号化した上で保存し、リポジトリ経由で保存するメソッド*/ 
    String encodedPassword = encodePassword(userEntity.getPassword());
    userEntity.setPassword(encodedPassword);
    userRepository.insert(userEntity);
    /*・リポジトリのinsertメソッドを呼び出す前に、下のencodePasswordメソッドを呼び出してる
      ・encodePasswordメソッド：PasswordEncoderという暗号化をするクラスを使用し、引数で渡された文字列を暗号化する*/
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
    // パスワードの暗号化にはSpringSecurityが提供するpasswordEncoderを使用する
  }
}