package in.tech_camp.chat_app.entity;

import java.util.List;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private String name;
  private String email;
  private String password;
  // ↓ユーザー情報からroom_usersの情報が取得できるように追加
  // ↓1つのUserEntityから複数のRoomUserEntityが紐づくためリストのフィールドを追加
  private List<RoomUserEntity> roomUsers;
  private List<MessageEntity> messages;
}
