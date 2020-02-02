import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private static final int ID = 0;
    private static final int W = 1;
    private static final int L = 2;
    private static final int R = 3;

    private final ST<String, int[]> teamStatsST;
    private final String[] teams;
    private final int[][] games;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        teamStatsST = new ST<>();

        In in = new In(filename);
        int n = Integer.parseInt(in.readLine());
        teams = new String[n];
        games = new int[n][n];

        int id = 0;
        while (in.hasNextLine()) {
            String line = in.readLine().trim();
            String[] token = line.split("\\s+");

            String name = token[0];
            teams[id] = name;

            int[] stats = new int[4];
            stats[ID] = id;
            stats[W] = Integer.parseInt(token[1]);
            stats[L] = Integer.parseInt(token[2]);
            stats[R] = Integer.parseInt(token[3]);

            teamStatsST.put(name, stats);

            for (int j = 0; j < n; j++)
                games[id][j] = Integer.parseInt(token[j + 4]);

            id++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamStatsST.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teamStatsST.keys();
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        return teamStatsST.get(team)[W];
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        return teamStatsST.get(team)[L];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        return teamStatsST.get(team)[R];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);

        int team1Id = teamStatsST.get(team1)[ID];
        int team2Id = teamStatsST.get(team2)[ID];
        return games[team1Id][team2Id];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);

        if (isTriviallyEliminated(team)) return true;

        int remainingGames = ((numberOfTeams() - 1) * (numberOfTeams() - 1)
                - (numberOfTeams() - 1)) / 2;
        int teamCount = numberOfTeams() - 1;
        int s = remainingGames + teamCount;
        int t = remainingGames + teamCount + 1;

        int id = teamStatsST.get(team)[ID];

        ST<Integer, Integer> nodeToId = new ST<>();
        for (int k = 0; k < numberOfTeams(); k++) {
            if (k == id) continue;
            if (id < k) nodeToId.put(k - 1, k);
            else nodeToId.put(k, k);
        }

        FlowNetwork gameNetwork = constructFlowNetwork(team, nodeToId);
        FordFulkerson ff = new FordFulkerson(gameNetwork, s, t);

        boolean eliminated = false;
        for (FlowEdge edge : gameNetwork.adj(s)) {
            if (!(edge.capacity() == edge.flow()))
                eliminated = true;
        }

        return eliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);

        Bag<String> certificate = new Bag<>();

        if (isTriviallyEliminated(team)) {
            for (String tm : teamStatsST) {
                if (teamStatsST.get(team)[W] + teamStatsST.get(team)[R] < teamStatsST.get(tm)[W])
                    certificate.add(tm);
            }

            return certificate;
        }

        int id = teamStatsST.get(team)[ID];
        int remainingGames = ((numberOfTeams() - 1) * (numberOfTeams() - 1)
                - (numberOfTeams() - 1)) / 2;
        int teamCount = numberOfTeams() - 1;
        int s = remainingGames + teamCount;
        int t = remainingGames + teamCount + 1;

        ST<Integer, Integer> nodeToId = new ST<>();
        for (int k = 0; k < numberOfTeams(); k++) {
            if (k == id) continue;
            if (id < k) nodeToId.put(k - 1, k);
            else nodeToId.put(k, k);
        }

        FlowNetwork gameNetwork = constructFlowNetwork(team, nodeToId);
        FordFulkerson ff = new FordFulkerson(gameNetwork, s, t);

        for (int i = 0; i < numberOfTeams() - 1; i++) {
            if (ff.inCut(i))
                certificate.add(teams[nodeToId.get(i)]);
        }

        if (certificate.isEmpty()) return null;

        return certificate;
    }

    private boolean isTriviallyEliminated(String team) {
        int maxPossibleWins = teamStatsST.get(team)[W] + teamStatsST.get(team)[R];
        for (String ts : teamStatsST) {
            if (teamStatsST.get(ts)[W] > maxPossibleWins)
                return true;
        }
        return false;
    }

    private FlowNetwork constructFlowNetwork(String team, ST<Integer, Integer> nodeToId) {
        int remainingGames = ((numberOfTeams() - 1) * (numberOfTeams() - 1)
                - (numberOfTeams() - 1)) / 2;
        int teamCount = numberOfTeams() - 1;

        int w = teamStatsST.get(team)[W];
        int r = teamStatsST.get(team)[R];

        FlowNetwork flowNetwork = new FlowNetwork(remainingGames + teamCount + 2);

        int s = remainingGames + teamCount;
        int t = remainingGames + teamCount + 1;

        int matchNode = numberOfTeams() - 1;

        for (int i = 0; i < numberOfTeams() - 1; i++) {

            int wi = teamStatsST.get(teams[nodeToId.get(i)])[W];
            FlowEdge e4 = new FlowEdge(i, t, w + r - wi);
            flowNetwork.addEdge(e4);

            for (int j = i + 1; j < numberOfTeams() - 1; j++) {
                if (i == j) continue;

                int g = games[nodeToId.get(i)][nodeToId.get(j)];

                FlowEdge e1 = new FlowEdge(s, matchNode, g);
                flowNetwork.addEdge(e1);

                FlowEdge e2 = new FlowEdge(matchNode, i, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(e2);

                FlowEdge e3 = new FlowEdge(matchNode, j, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(e3);

                matchNode++;
            }
        }

        return flowNetwork;
    }

    private void validateTeam(String team) {
        if (!teamStatsST.contains(team))
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination elimination = new BaseballElimination(args[0]);
        StdOut.println("Teams: " + elimination.numberOfTeams());
        for (String team : elimination.teams())
            StdOut.println(
                    elimination.teamStatsST.get(team)[ID] + " " + team + ", " + elimination
                            .wins(team) + ", "
                            + elimination.losses(team));
        StdOut.println(" ");


        String tm = "Montreal";
        StdOut.println("Is " + tm + " eliminated?");
        StdOut.println(elimination.isEliminated(tm));

        for (int[] g : elimination.games) {
            StdOut.println(" ");
            for (int i : g)
                StdOut.print(i + ", ");
        }
        StdOut.println(" ");

        for (String team : elimination.certificateOfElimination(tm))
            StdOut.print(team + " ");

    }
}
