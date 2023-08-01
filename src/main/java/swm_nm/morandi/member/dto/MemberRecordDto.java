package swm_nm.morandi.member.dto;

import lombok.*;
import swm_nm.morandi.test.dto.TestRatingDto;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRecordDto {
    List<GrassDto> grassDtos;
    List<GraphDto> graphDtos;
    List<TestRatingDto> testRatingDtos;
}
