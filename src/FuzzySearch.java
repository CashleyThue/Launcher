import java.util.List;

public class FuzzySearch {

    public static int score(String query, String target) {

        query = query.toLowerCase();
        target = target.toLowerCase();

        int score = 0;

        int qi = 0;
        int lastMatch = -1;

        if (target.startsWith(query)) {
            score += 1000;
        }

        for (int ti = 0; ti < target.length(); ti++) {

            if (qi < query.length() &&
                query.charAt(qi) == target.charAt(ti)) {

                score += 20;

                if (lastMatch == ti - 1) {
                    score += 15;
                }

                score += Math.max(0, 10 - ti);

                if (lastMatch != -1) {
                    score -= (ti - lastMatch - 1);
                }

                lastMatch = ti;

                qi++;
            }
        }

        if (qi != query.length()) {
            return Integer.MIN_VALUE;
        }

        score -= target.length() * 3;

        return score;
    }

    public static App check(String query, List<App> apps) {

        App best = null;

        int bestScore = Integer.MIN_VALUE;

        for (App app : apps) {
            if (app.name == null) {
                continue;
            }

            int score = score(query, app.name);

            if (score > bestScore) {
                bestScore = score;
                best = app;
            }
        }

        return best;
    }
}
