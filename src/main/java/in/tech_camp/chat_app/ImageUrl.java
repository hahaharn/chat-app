package in.tech_camp.chat_app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ImageUrl {
  /*@Valueを使用し、application.propertiesのimage.urlの値をフィールドにマッピング
  　これによりgetImageUrl()を使用すれば、画像ファイルの格納場所を取得できる*/
  @Value("${image.url}")
  private String url;

  public String getImageUrl(){
    return url;
  }
}
