# Form Management API

Hệ thống REST API cho việc quản lý form đơn giản, được xây dựng bằng Spring Boot.

Hệ thống hỗ trợ 2 vai trò:
- **Admin**: tạo/sửa/xóa form và các field bên trong
- **Nhân viên SW**: xem danh sách form active và submit dữ liệu

> Đây là bài test cho vị trí Fullstack Developer (Young Talent).

---

## 1. Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 24 |
| Framework | Spring Boot 4.0.6 |
| Persistence | Spring Data JPA + Hibernate |
| Database | H2 (in-memory) |
| Validation | Jakarta Validation + custom validator module |
| Documentation | Springdoc OpenAPI (Swagger UI) |
| Build tool | Maven |
| Testing | JUnit 5 |

---

## 2. Cài đặt & Chạy

### Yêu cầu
- JDK 17+ (project dùng Java 24, có thể chạy với Java 17+)
- Maven 3.6+ (hoặc dùng Maven wrapper `./mvnw` có sẵn trong project)

### Các bước

```bash
# 1. Clone repo
git clone https://github.com/edamame69/Form-Management.git
cd Form-Management

# 2. Build
./mvnw clean install

# 3. Chạy app
./mvnw spring-boot:run
```

App sẽ chạy ở port **8081** (đã cấu hình trong `application.yml`).

### Các đường dẫn quan trọng

| URL | Mô tả |
|---|---|
| http://localhost:8081/swagger-ui/index.html | Swagger UI để test API |
| http://localhost:8081/api-docs | OpenAPI JSON spec |
| http://localhost:8081/h2-console | H2 Database console |

**Kết nối H2 Console:**
- JDBC URL: `jdbc:h2:mem:formdb`
- User: `sa`
- Password: (để trống)

---

## 3. Cấu trúc project

```
src/main/java/com/topcv/form/
├── FormApplication.java        # Entry point
├── config/                     # OpenAPI config
├── controller/                 # REST controllers (3 file)
├── service/                    # Business logic
│   └── mapper/                 # Entity ↔ DTO mapping
├── repository/                 # JPA repositories
├── entity/                     # JPA entities
├── dto/
│   ├── request/                # Request DTOs (record)
│   └── response/               # Response DTOs (record)
├── validator/                  # Validator module (Phase 4)
│   ├── FieldValidator          # Interface
│   ├── TextFieldValidator      # 1 file/loại field
│   ├── NumberFieldValidator
│   ├── DateFieldValidator
│   ├── ColorFieldValidator
│   ├── SelectFieldValidator
│   ├── FieldValidatorFactory   # Phân phối validator theo type
│   └── SubmissionValidator     # Lớp tổng hợp validate
├── exception/                  # Custom exceptions + global handler
└── enums/                      # FieldType, FormStatus
```

---

## 4. Database Schema

### `forms`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK |
| title | VARCHAR(255) | NOT NULL |
| description | TEXT | |
| display_order | INT | NOT NULL, dùng để sort form cho nhân viên |
| status | VARCHAR(20) | ACTIVE / DRAFT |
| created_at, updated_at | TIMESTAMP | Tự động set bởi Hibernate |

### `fields`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK |
| form_id | BIGINT | FK → forms, CASCADE DELETE |
| label | VARCHAR(255) | NOT NULL |
| field_type | VARCHAR(20) | TEXT / NUMBER / DATE / COLOR / SELECT |
| field_order | INT | Sort field trong form |
| required | BOOLEAN | |
| options | TEXT | JSON array (chỉ dùng cho SELECT) |
| min_value, max_value | DOUBLE | (cho NUMBER) |
| max_length | INT | (cho TEXT) |

### `submissions`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK |
| form_id | BIGINT | FK → forms |
| submission_data | TEXT | JSON: `{"fieldId": "value", ...}` |
| submitted_at | TIMESTAMP | |

---

## 5. API Endpoints

### Form Management (Admin)
| Method | Path | Mô tả |
|---|---|---|
| GET | `/api/forms` | Danh sách tất cả form |
| GET | `/api/forms/{id}` | Chi tiết form (kèm fields) |
| POST | `/api/forms` | Tạo form mới |
| PUT | `/api/forms/{id}` | Cập nhật form |
| DELETE | `/api/forms/{id}` | Xóa form (cascade fields) |

### Field Management (Admin)
| Method | Path | Mô tả |
|---|---|---|
| POST | `/api/forms/{id}/fields` | Thêm field vào form |
| PUT | `/api/forms/{id}/fields/{fid}` | Cập nhật field |
| DELETE | `/api/forms/{id}/fields/{fid}` | Xóa field |

### Submission (Nhân viên SW)
| Method | Path | Mô tả |
|---|---|---|
| GET | `/api/forms/active` | Danh sách form active (kèm fields) |
| POST | `/api/forms/{id}/submit` | Submit form |
| GET | `/api/submissions` | Lịch sử submission |

Chi tiết request/response body xem trong Swagger UI.

---

## 6. Validation Rules

Mỗi loại field có rule validation riêng (tách module trong `validator/`):

| Field Type | Validation |
|---|---|
| TEXT | Required check + maxLength (default 200) |
| NUMBER | Required + parse được số + trong range [minValue, maxValue] |
| DATE | Required + ISO format (yyyy-MM-dd) + không cho past date |
| COLOR | Required + HEX hợp lệ (#RRGGBB) |
| SELECT | Required + value phải nằm trong options |

**Đặc điểm:** Khi submit form, hệ thống **collect toàn bộ lỗi** thay vì dừng ở lỗi đầu tiên — nhân viên thấy hết lỗi và sửa 1 lần.

Ví dụ response lỗi:
```json
{
  "timestamp": "2026-05-12T10:30:00",
  "status": 400,
  "error": "Submission Validation Failed",
  "message": "Submission validation failed",
  "path": "/api/forms/1/submit",
  "fieldErrors": {
    "1": "This field is required",
    "2": "Must be <= 30.0",
    "3": "Date must not be in the past"
  }
}
```

---

## 7. Design Decisions

Đây là các quyết định thiết kế chính và lý do:

### 7.1. Lưu submission data dưới dạng JSON string
Thay vì tạo bảng `submission_value` riêng (1 row = 1 field value), chọn lưu cả submission thành 1 JSON string trong cột `submission_data`.

**Lý do:**
- Yêu cầu hiện tại chỉ cần lưu và xem lại — không cần query theo field
- Đơn giản, ít bảng hơn, code gọn
- Nếu sau này cần analytics (đếm bao nhiêu người chọn option X), có thể migrate sau

### 7.2. Tách validator thành module riêng
Mỗi loại field có 1 class validator riêng, thay vì viết switch-case trong service.

**Lý do:**
- Đề bài gợi ý cụ thể tách logic validate
- Dễ test (unit test từng validator độc lập)
- Mở rộng dễ: thêm `FieldType.FILE` chỉ cần tạo 1 file + thêm 1 case trong factory
- `switch expression` trong `FieldValidatorFactory` đảm bảo exhaustive — quên handle FieldType mới sẽ không compile

### 7.3. Dùng `record` cho DTO, `class` cho Entity
- **Entity** (Form, Field, Submission): dùng `class` + Lombok vì JPA cần no-arg constructor và setter để hydrate
- **DTO** (Request, Response, ErrorResponse): dùng `record` vì immutable, code gọn, không cần Lombok

### 7.4. Submission data dùng `Map<fieldId, value>` thay vì `Map<fieldLabel, value>`
Field ID ổn định và duy nhất, trong khi label có thể trùng hoặc bị sửa. Đây cũng là cách các form builder thực tế (Google Forms, Typeform) dùng.

### 7.5. Validator trả về `String` (error message) thay vì throw exception
Để collect được toàn bộ lỗi trong 1 submission. Nếu throw exception thì chỉ thấy lỗi đầu tiên — user khó chịu khi sửa từng cái một.

### 7.6. `findFieldInForm` kiểm tra field thuộc về form
Khi xóa/sửa field, kiểm tra field thực sự thuộc về form trong path. Tránh trường hợp gọi `DELETE /api/forms/1/fields/99` mà field 99 thuộc form khác.

---

## 8. Những thứ chưa hoàn thiện (Future Work)

Đây là các điểm có thể cải thiện nếu có thêm thời gian:

- **Pagination** cho `GET /api/forms` và `GET /api/submissions` (đề bài có nhắc là điểm cộng)
- **Authentication & Authorization** — hiện tại chưa phân biệt admin vs nhân viên, mọi endpoint đều public
- **Unit test coverage cao hơn** — hiện tại chỉ có test cho `TextFieldValidator` và `ColorFieldValidator`
- **Drag & drop reorder field** — đề bài gợi ý là điểm cộng
- **Docker Compose** — để chạy local dễ hơn (không cần cài Java)
- **Migration script (Flyway/Liquibase)** — hiện tại dùng `ddl-auto=update` của Hibernate, không phù hợp production

---

## 9. Quick Test Scenario

Test nhanh end-to-end qua Swagger UI:

**1. Tạo form active**
```http
POST /api/forms
{
  "title": "Đăng ký nghỉ phép",
  "description": "Form xin nghỉ",
  "displayOrder": 1,
  "status": "ACTIVE"
}
```

**2. Thêm các field** (giả sử form ID = 1)
```http
POST /api/forms/1/fields
{
  "label": "Họ tên",
  "fieldType": "TEXT",
  "fieldOrder": 1,
  "required": true,
  "maxLength": 100
}
```

Tương tự với NUMBER (số ngày nghỉ, min=1, max=30), DATE (ngày bắt đầu), SELECT (lý do, options).

**3. Lấy form active**
```http
GET /api/forms/active
```

**4. Submit thành công**
```http
POST /api/forms/1/submit
{
  "data": {
    "1": "Nguyễn Văn A",
    "2": "5",
    "3": "2026-12-25",
    "4": "Cá nhân"
  }
}
```

**5. Submit lỗi để test validation**
```http
POST /api/forms/1/submit
{
  "data": {
    "1": "",
    "2": "100",
    "3": "2020-01-01"
  }
}
```
→ Sẽ thấy `fieldErrors` chứa lỗi của các field.

---

## 10. Contact

- **GitHub:** [edamame69](https://github.com/edamame69)
- **Email:** *minhldforwork@gmail.com*