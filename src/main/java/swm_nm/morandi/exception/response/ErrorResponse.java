package swm_nm.morandi.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // null이 아닌 필드만 직렬화 하도록 설정 (null인 필드는 직렬화 하지 않음)
    private final List<ValidationError> errors; // 여러개의 에러를 담을 수 있도록 List로 설정

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {

        private final String field;// 에러가 발생한 필드
        private final String message;// 에러 메시지

        // FieldError 객체에서 ValidationError 객체를 생성
        public static ValidationError of(final FieldError fieldError) {
            //FieldError는 @Valid를 붙여준 객체에 대한 검증을 통과하지 못하면 FieldError 객체를 생성한다.
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}