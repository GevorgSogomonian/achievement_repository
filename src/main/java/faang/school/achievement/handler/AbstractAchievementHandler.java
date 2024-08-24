package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAchievementHandler {
    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    protected void processAchievementEvent(String achievementTitle, long userId) {
        Achievement achievement = getAchievement(achievementTitle);
        long achievementId = achievement.getId();
        long incrementCurrentPoints = getIncrementCurrentPoints(userId, achievementId);

        if (getAchievementPoints(userId, achievementId) == incrementCurrentPoints) {
            giveAchievement(userId, achievementId);
        }
    }

    protected Achievement getAchievement(String achievementTitle) {
        return achievementCache.getAchievementByTitle(achievementTitle)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Achievement with title %s not found", achievementTitle);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    protected long getIncrementCurrentPoints(long userId, long achievementId) {
        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
        }

        AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
        achievementProgress.increment();
        achievementService.saveProgress(achievementProgress);
        return achievementProgress.getCurrentPoints();
    }

    protected long getAchievementPoints(long userId, long achievementId) {
        return achievementService.getProgress(userId, achievementId).getAchievement().getPoints();
    }

    protected void giveAchievement(long userId, long achievementId) {
        achievementService.giveAchievement(userId, achievementId);
    }
}
