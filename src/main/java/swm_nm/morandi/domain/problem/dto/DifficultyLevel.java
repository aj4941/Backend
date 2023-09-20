package swm_nm.morandi.domain.problem.dto;

import lombok.Getter;

@Getter
public enum DifficultyLevel {
    B5("b5", "Bronze 5"), B4("b4", "Bronze 4"), B3("b3", "Bronze 3"),
    B2("b2", "Bronze 2"), B1("b1", "Bronze 1"),
    S5("s5", "Silver 5"), S4("s4", "Silver 4"), S3("s3", "Silver 3"),
    S2("s2", "Silver 2"), S1("s1", "Silver 1"),
    G5("g5", "Gold 5"), G4("g4", "Gold 4"), G3("g3", "Gold 3"),
    G2("g2", "Gold 2"), G1("g1", "Gold 1"),
    P5("p5", "Platinum 5"), P4("p4", "Platinum 4"), P3("p3", "Platinum 3");
    private final String shortName;
    private final String fullName;

    DifficultyLevel(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public static String getValueByLevel(int level) {
        int index = level - 1;
        if (index >= 0 && index < DifficultyLevel.values().length) {
            return DifficultyLevel.values()[index].getFullName();
        }
        return "Invalid Level";
    }

    public static int getLevelByValue(DifficultyLevel level) {
        for (int i = 0; i < DifficultyLevel.values().length; i++) {
            if (DifficultyLevel.values()[i] == level) {
                return i + 1;
            }
        }
        return -1;
    }

    public static long getRatingByValue(DifficultyLevel level) {
        int levelByValue = getLevelByValue(level);
        if (levelByValue <= 4) return 60;
        if (levelByValue <= 7) return 90;
        if (levelByValue <= 10) return 130;
        if (levelByValue <= 12) return 200;
        if (levelByValue <= 14) return 260;
        if (levelByValue <= 16) return 320;
        return -1;
    }
}
