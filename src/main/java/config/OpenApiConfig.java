package com.topcv.form.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI formManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Form Management API")
                        .description("""
                                REST API cho hệ thống quản lý form đơn giản.
                                
                                Hệ thống cho phép:
                                - Admin tạo/sửa/xóa form và các field bên trong
                                - Nhân viên SW xem danh sách form active và submit dữ liệu
                                - Validate dữ liệu theo từng loại field (text, number, date, color, select)
                                
                                Đây là bài test cho vị trí Fullstack Developer.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Le Duc Minh")
                                .email("minhldforwork@gmail.com")
                                .url("https://github.com/edamame69/Form-Management"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}