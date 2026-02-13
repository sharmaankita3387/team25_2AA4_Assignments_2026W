package Assignment1;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main class serves as the entry point for the Catan simulator.
 * It initializes the game board, agents, and dice, then passes them
 * to the GamePlay controller to run the simulation.
 */
public class Main {
    
    /**
     * Entry point of the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // 1. Initialize the Dice
        // Using the Composite Pattern via MultiDice to hold two 6-sided dice.
        MultiDice gameDice = new MultiDice();
        gameDice.addDice(new RegularDice(6)); 
        gameDice.addDice(new RegularDice(6));

        // 2. Initialize the 4 Agents (Requirement R1.2)
        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent(0, "Agent_Alpha"));
        agents.add(new Agent(0, "Agent_Beta"));
        agents.add(new Agent(0, "Agent_Gamma"));
        agents.add(new Agent(0, "Agent_Delta"));

        // 3. Setup the Board Infrastructure (Requirement R1.1)
        // These lists represent the hard-wired map pieces (19 Tiles, 54 Nodes, 72 Edges).
        List<Tile> tiles = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();

        // Create 2 Nodes (Intersections)
        Node n1 = new Node(0);
        Node n2 = new Node(1);
        nodes.add(n1);
        nodes.add(n2);

        // Create an Edge (Road spot) connecting them
        Edge e1 = new Edge(n1, n2);
        edges.add(e1);

        // Create a Wheat Tile with a roll value of 10
        // We pass e1 so the Tile knows which edges (and nodes) surround it
        List<Edge> tileEdges = new ArrayList<>();
        tileEdges.add(e1);
        Tile wheatTile = new Tile(Resources.WHEAT, 10, 0, tileEdges);
        tiles.add(wheatTile);
        
        // Initialize the Board with the map components
        Board catanBoard = new Board(tiles, edges, nodes);

        // 4. Give Agents starting buildings so they can earn resources 
        // Place a settlement for Agent_Alpha on Node n1
        catanBoard.placeSettlement(n1, new Settlement(agents.get(0), n1));

        // 5. Initialize the GamePlay Controller
        int maxRounds = 8192; 
        GamePlay controller = new GamePlay(agents, catanBoard, gameDice, maxRounds);

        // 6. Run the Simulation
        System.out.println("--- Starting Catan Simulation ---");
        controller.runSimulation();
        
        // 7. Final Status Output
        System.out.println("--- Simulation Complete ---");
        System.out.println("Total Turns Elapsed: " + controller.getTurnNumber());
    }
}//ends class main
