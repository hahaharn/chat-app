package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.RoomUserEntity;

@Mapper
public interface RoomUserRepository {
  @Insert("INSERT INTO room_users(user_id, room_id) VALUES(#{user.id}, #{room.id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(RoomUserEntity userRoomEntity);

  // 特定のユーザーが参加しているチャットルームの情報を、部屋のデータごと丸ごと持ってくる
  @Select("SELECT * FROM room_users WHERE user_id = #{userId}")
  @Result(property = "room", column = "room_id",
          one = @One(select = "in.tech_camp.chat_app.repository.RoomRepository.findById"))
  /*・property="room":取得したデータをRoomUserEntityの中にあるroomというフィールドに入れてという指示
    ・column="room_id":room_usersテーブルにあるroom_idの値を、次の処理（@One）のヒントとして使ってという意味
    ・@One:「1つのデータ（部屋情報）」を紐付けるために使う
    ・select="...RoomRepository.findById"：
    「さっきの room_id を使って、RoomRepositoryのfindByIdメソッドを実行してきて！」という命令
    →これにより部屋の名前などの詳細データが取得される*/
  List<RoomUserEntity> findByUserId(Integer userId);
}