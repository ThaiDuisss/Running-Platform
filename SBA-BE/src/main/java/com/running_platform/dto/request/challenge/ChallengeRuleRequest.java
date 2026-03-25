package com.running_platform.dto.request.challenge;

import com.running_platform.enums.ChallengeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChallengeRuleRequest {

    @NotNull
    ChallengeType type;

    //Giá trị mục tiêu của thử thách, có thể là khoảng cách (km), thời gian (phút), hoặc lượng calo (kcal) tùy thuộc vào loại thử thách
    @PositiveOrZero
    Double targetValue;

    //thời gian diễn ra thử thách, tính bằng ngày
    @PositiveOrZero
    Integer durationDays;

    //Mỗi ngày người tham gia phải đạt được mục tiêu này để hoàn thành thử thách
    @PositiveOrZero
    Double dailyTarget;

    //Tốc độ tối thiểu để hoàn thành thử thách, tính bằng km/h
    @PositiveOrZero
    Double minSpeed;
}
