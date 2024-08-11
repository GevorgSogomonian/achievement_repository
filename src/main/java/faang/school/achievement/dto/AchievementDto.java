package faang.school.achievement.dto;


import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementDto {

    private Long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long points;
    private long progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
