package in.tech_camp.chat_app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:src/main/resources/static/uploads/");
            /*src/main/resources/static/uploads/の前にfile:をつけることで、
            　指定するパスをサーバー内で保持しているものではなく、
            　ファイルシステム上の特定ディレクトリから直接読み込むようになる */
  }
}