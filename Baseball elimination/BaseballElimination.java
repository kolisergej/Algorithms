import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
    private HashMap<String, Integer> mStringIdTeam;
    private HashMap<Integer, String> mIdStringTeam;
    private int[][] mDivision;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int count = in.readInt();
        mStringIdTeam = new HashMap<>();
        mIdStringTeam = new HashMap<>();

        mDivision = new int[count][3 + count];

        for (int teamNumber = 0; teamNumber < count; teamNumber++) {
            String team = in.readString();
            mStringIdTeam.put(team, teamNumber);
            mIdStringTeam.put(teamNumber, team);

            mDivision[teamNumber][0] = in.readInt();
            mDivision[teamNumber][1] = in.readInt();
            mDivision[teamNumber][2] = in.readInt();
            for (int i = 3; i < 3 + count; i++) {
                mDivision[teamNumber][i] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return mStringIdTeam.size();
    }

    public Iterable<String> teams() {
        return mStringIdTeam.keySet();
    }

    public int wins(String team) {
        if (!mStringIdTeam.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        int teamNumber = mStringIdTeam.get(team);
        return mDivision[teamNumber][0];
    }

    public int losses(String team) {
        if (!mStringIdTeam.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        int teamNumber = mStringIdTeam.get(team);
        return mDivision[teamNumber][1];
    }

    public int remaining(String team) {
        if (!mStringIdTeam.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        int teamNumber = mStringIdTeam.get(team);
        return mDivision[teamNumber][2];
    }

    public int against(String team1, String team2) {
        if (!mStringIdTeam.containsKey(team1) || !mStringIdTeam.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        int teamNumber1 = mStringIdTeam.get(team1);
        int teamNumber2 = mStringIdTeam.get(team2);
        return mDivision[teamNumber1][3 + teamNumber2];
    }

    public boolean isEliminated(String team) {        
        return (certificateOfElimination(team) != null);
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!mStringIdTeam.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        Set<String> set = new TreeSet<>();
        int teamNumber = mStringIdTeam.get(team);
        for (int i = 0; i < mDivision.length; i++) {
            if (i != teamNumber && mDivision[i][0] > mDivision[teamNumber][0] + mDivision[teamNumber][2]) {
                set.add(mIdStringTeam.get(i));
            }
        }

        if (set.isEmpty()) {
            int sizeOfVertices = (mDivision.length * (mDivision.length + 1)) / 2 + 2;
            FlowNetwork flowNetwork = new FlowNetwork(sizeOfVertices);

            int startGameVertexForBuild = mDivision.length;
            for (int i = 0; i < mDivision.length; i++) {
                if (i != teamNumber) {
                    // connect i team with sink (sizeOfVertices - 2)
                    flowNetwork.addEdge(new FlowEdge(i, sizeOfVertices - 2, wins(team) + remaining(team) - mDivision[i][0]));
                }
                
                // source (sizeOfVertices - 1)                
                // build
                if (i != teamNumber) {
                    int counter = mDivision.length;
                    for (int j = i + 4; j < mDivision[i].length; j++) {   
                        if ((counter % mDivision.length) + 1 + i != teamNumber)
                        {
                            flowNetwork.addEdge(new FlowEdge(sizeOfVertices - 1, startGameVertexForBuild, mDivision[i][j]));
                            flowNetwork.addEdge(new FlowEdge(startGameVertexForBuild, i, Double.POSITIVE_INFINITY));
                            flowNetwork.addEdge(new FlowEdge(startGameVertexForBuild, (counter % mDivision.length) + 1 + i, Double.POSITIVE_INFINITY));
                        }
                        ++startGameVertexForBuild;
                        ++counter;
                    }
                }
                else {
                    startGameVertexForBuild += mDivision[i].length - 4 - i;
                }
            }
            
            FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, sizeOfVertices - 1, sizeOfVertices - 2);
            boolean needToCheck = false;
            for (FlowEdge e : flowNetwork.adj(sizeOfVertices - 1)) {
                if (e.capacity() != e.flow()) {
                    needToCheck = true;
                }
            }
            
            if (needToCheck) {
                for (int i = 0; i < mDivision.length; i++) {
                    if (fordFulkerson.inCut(i)) {
                        set.add(mIdStringTeam.get(i));
                    }
                }
            }
        }
        if (set.isEmpty()) {
            set = null;
        }
        return set;
    }

//    public static void main(String[] args) {
//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
//    }
}