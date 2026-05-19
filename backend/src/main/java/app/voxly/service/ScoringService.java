package app.voxly.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.*;

@Service
public class ScoringService {

    private static final List<String> FILLER_WORDS =
            List.of("um", "uh", "like", "basically", "you know", "lah", "lor", "literally", "right");

    private static final double[] WEIGHTS = {0.35, 0.25, 0.25, 0.15};

    public int calculatePronunciationScore(String transcript, String expected) {
        if (expected == null || expected.isBlank()) return 100;
        String t = normalize(transcript);
        String e = normalize(expected);
        int distance = levenshtein(t, e);
        int maxLen = Math.max(t.length(), e.length());
        if (maxLen == 0) return 100;
        double similarity = 1.0 - (double) distance / maxLen;
        return Math.max(0, Math.min(100, (int) Math.round(similarity * 100)));
    }

    public int calculateFluencyScore(double durationSeconds, int wordCount) {
        if (durationSeconds <= 0 || wordCount <= 0) return 0;
        double wpm = (wordCount / durationSeconds) * 60.0;
        int score;
        if (wpm < 80) score = 40;
        else if (wpm < 110) score = 40 + (int) ((wpm - 80) / 30 * 35);
        else if (wpm <= 150) score = Math.max(75, (int) (100 - Math.abs(wpm - 130) / 20.0 * 25));
        else if (wpm <= 180) score = (int) (75 - (wpm - 150) / 30 * 25);
        else score = 50;
        return Math.max(0, Math.min(100, score));
    }

    public int calculateGrammarScore(String text) {
        int score = 100;
        String[][] patterns = {
            {"\\bi\\s+is\\b", "15"}, {"\\bthey\\s+is\\b", "10"},
            {"\\bhe\\s+don't\\b", "10"}, {"\\bmore\\s+\\w+er\\b", "5"},
            {"\\ba\\s+[aeiou]\\w+", "3"}, {"\\bthe\\s+the\\b", "5"}
        };
        for (String[] p : patterns) {
            Pattern pat = Pattern.compile(p[0], Pattern.CASE_INSENSITIVE);
            long count = pat.matcher(text).results().count();
            score -= count * Integer.parseInt(p[1]);
        }
        return Math.max(0, Math.min(100, score));
    }

    public List<String> detectFillerWords(String text) {
        List<String> found = new ArrayList<>();
        String lower = text.toLowerCase();
        for (String filler : FILLER_WORDS) {
            if (lower.contains(filler)) found.add(filler);
        }
        return found;
    }

    public int calculateOverallScore(int pronunciation, int fluency, int grammar, int vocabulary) {
        double raw = pronunciation * WEIGHTS[0] + fluency * WEIGHTS[1]
                + grammar * WEIGHTS[2] + vocabulary * WEIGHTS[3];
        return Math.max(0, Math.min(100, (int) Math.round(raw)));
    }

    private String normalize(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9\\s]", "").trim();
    }

    private int levenshtein(String s1, String s2) {
        int[] prev = new int[s2.length() + 1];
        for (int j = 0; j <= s2.length(); j++) prev[j] = j;
        for (int i = 1; i <= s1.length(); i++) {
            int[] curr = new int[s2.length() + 1];
            curr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                curr[j] = Math.min(
                    Math.min(prev[j] + 1, curr[j - 1] + 1),
                    prev[j - 1] + (s1.charAt(i - 1) != s2.charAt(j - 1) ? 1 : 0)
                );
            }
            prev = curr;
        }
        return prev[s2.length()];
    }
}
