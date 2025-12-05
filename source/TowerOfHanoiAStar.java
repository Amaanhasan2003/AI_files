import java.util.*;

public class TowerOfHanoiAStar {

    // Represents a state of the puzzle: pos[i] = peg of disk i
    static class State {
        int[] pos;
        int g;   // cost so far
        int f;   // g + h
        State parent;
        String move;

        State(int[] p) {
            pos = Arrays.copyOf(p, p.length);
        }

        String key() {
            return Arrays.toString(pos);
        }

        // Check goal: all disks on peg 2
        boolean isGoal() {
            for (int peg : pos)
                if (peg != 2) return false;
            return true;
        }

        // Manhattan-style heuristic:
        // h = number of disks not on target peg
        int heuristic() {
            int h = 0;
            for (int peg : pos)
                if (peg != 2) h++;
            return h;
        }

        // Generate neighboring states
        List<State> nextStates() {
            List<State> next = new ArrayList<>();
            int n = pos.length;

            for (int d = 0; d < n; d++) {
                int from = pos[d];

                // Check if disk d is top disk on its peg
                boolean isTop = true;
                for (int smaller = 0; smaller < d; smaller++) {
                    if (pos[smaller] == from) {
                        isTop = false;
                        break;
                    }
                }
                if (!isTop) continue;

                for (int to = 0; to < 3; to++) {
                    if (to == from) continue;

                    // Can't move onto a smaller disk
                    boolean valid = true;
                    for (int smaller = 0; smaller < d; smaller++) {
                        if (pos[smaller] == to) {
                            valid = false;
                            break;
                        }
                    }
                    if (!valid) continue;

                    // Create new state
                    State s = new State(pos);
                    s.pos[d] = to;
                    s.move = "Move disk " + d + " from peg " + from + " to peg " + to;
                    next.add(s);
                }
            }
            return next;
        }
    }

    // A* search
    public static List<String> aStar(int n) {
        int[] start = new int[n];
        State startState = new State(start);
        startState.g = 0;
        startState.f = startState.heuristic();

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.f));
        pq.add(startState);

        Map<String, Integer> visited = new HashMap<>();
        visited.put(startState.key(), 0);

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (cur.isGoal()) {
                return extractPath(cur);
            }

            for (State nxt : cur.nextStates()) {
                nxt.g = cur.g + 1;
                nxt.f = nxt.g + nxt.heuristic();
                nxt.parent = cur;

                String key = nxt.key();
                if (!visited.containsKey(key) || nxt.g < visited.get(key)) {
                    visited.put(key, nxt.g);
                    pq.add(nxt);
                }
            }
        }

        return null;
    }

    // Build solution path
    private static List<String> extractPath(State goal) {
        List<String> path = new ArrayList<>();
        State cur = goal;
        while (cur.parent != null) {
            path.add(cur.move);
            cur = cur.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        int n = 3; // number of disks

        List<String> result = aStar(n);
        System.out.println("A* Solution (" + result.size() + " moves):\n");

        for (String move : result) {
            System.out.println(move);
        }
    }
}
