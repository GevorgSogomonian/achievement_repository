package faang.school.achievement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name="achievement")
public class Achievement {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true, length = 128)
    private String title;

    @Column(name = "description", nullable = false, unique = true, length = 1024)
    private String description;

    @Column(name = "rarity", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Rarity rarity;

    @OneToMany(mappedBy = "achievement")
    @JsonManagedReference
    private List<UserAchievement> userAchievements;

    @OneToMany(mappedBy = "achievement")
    @JsonManagedReference
    private List<AchievementProgress> progresses;

    @Column(name = "points", nullable = false)
    private long points;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}