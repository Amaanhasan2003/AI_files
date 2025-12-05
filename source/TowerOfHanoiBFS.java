import java.util.*;

public class TowerOfHanoiBFS {

    // Represents a puzzle state: pos[i] = peg number (0,1,2) of disk i
    static class State {
        int[] pos;

        State(int[] p) {
            pos = Arrays.copyOf(p, p.length);
        }

        String key() {
            return Arrays.toString(pos);
        }

        boolean isGoal(int goalPeg) {
            for (int peg : pos)
                if (peg != goalPeg) return false;
            return true;
        }

        // Generate valid next states
        List<State> nextStates() {
            List<State> next = new ArrayList<>();
            int n = pos.length;

            // For each disk, try moving it if it's the top on its peg
            for (int d = 0; d < n; d++) {
                int from = pos[d];

                // Check if d is top disk on its peg
                boolean isTop = true;
                for (int smaller = 0; smaller < d; smaller++) {
                    if (pos[smaller] == from) {
                        isTop = false;
                        break;
                    }
                }
                if (!isTop) continue;

                // Try moving to every other peg
                for (int to = 0; to < 3; to++) {
                    if (to == from) continue;

                    // Check if move valid: no smaller disk on 'to' peg
                    boolean canMove = true;
                    for (int smaller = 0; smaller < d; smaller++) {
                        if (pos[smaller] == to) {
                            canMove = false;
                            break;
                        }
                    }
                    if (!canMove) continue;

                    // Create next state
                    State s = new State(pos);
                    s.pos[d] = to;
                    next.add(s);
                }
            }
            return next;
        }
    }

    // BFS shortest path
    public static List<String> bfs(int n) {
        int[] start = new int[n];
        State startState = new State(start);

        Queue<State> q = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, String> moveDesc = new HashMap<>();

        q.add(startState);
        parent.put(startState.key(), null);

        while (!q.isEmpty()) {
            State cur = q.poll();

            if (cur.isGoal(2)) {
                return buildPath(cur.key(), parent, moveDesc);
            }

            for (State nxt : cur.nextStates()) {
                String k = nxt.key();
                if (!parent.containsKey(k)) {
                    parent.put(k, cur.key());
                    moveDesc.put(k, describeMove(cur, nxt));
                    q.add(nxt);
                }
            }
        }
        return null;
    }

    // Describe move from one state to another
    private static String describeMove(State a, State b) {
        for (int i = 0; i < a.pos.length; i++) {
            if (a.pos[i] != b.pos[i]) {
                return "Move disk " + i + " from peg " + a.pos[i] + " to peg " + b.pos[i];
            }
        }
        return "";
    }

    // Build path using parent map
    private static List<String> buildPath(String goal, Map<String, String> parent, Map<String, String> moveDesc) {
        List<String> path = new ArrayList<>();
        String cur = goal;
        while (parent.get(cur) != null) {
            path.add(moveDesc.get(cur));
            cur = parent.get(cur);
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        int n = 3;  // number of disks to solve

        List<String> result = bfs(n);

        System.out.println("\nTower of Hanoi BFS Solution (" + result.size() + " moves):\n");
        for (String move : result) {
            System.out.println(move);
        }
    }
}
