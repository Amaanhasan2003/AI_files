public class TowerOfHanoiDFS {

    // DFS Recursive function
    public static void solveHanoi(int n, char fromPeg, char toPeg, char auxPeg) {
        if (n == 1) {
            System.out.println("Move disk 1 from " + fromPeg + " to " + toPeg);
            return;
        }

        // Move top n-1 disks from 'fromPeg' → 'auxPeg'
        solveHanoi(n - 1, fromPeg, auxPeg, toPeg);

        // Move the nth disk
        System.out.println("Move disk " + n + " from " + fromPeg + " to " + toPeg);

        // Move n-1 disks from 'auxPeg' → 'toPeg'
        solveHanoi(n - 1, auxPeg, toPeg, fromPeg);
    }

    public static void main(String[] args) {

        int n = 3; // Number of disks

        System.out.println("Tower of Hanoi DFS Solution for " + n + " disks:\n");
        solveHanoi(n, 'A', 'C', 'B'); // A = source, C = destination, B = auxiliary
    }
}
