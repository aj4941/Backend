package swm_nm.morandi.domain.customTest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomTestResponses {
    List<CustomTestResponse> customTests;
}
