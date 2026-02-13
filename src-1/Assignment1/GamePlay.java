/**
 *Student Name: Ankita Sharma
*/

package Assignment1;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The GamePlay class serves as the central controller for the Catan simulation.
 * It manages the iterative game loop, coordinated resource production between the board
 * and agents, and monitors victory conditions. 
*/
public class GamePlay {
	
	private int roundNumber = 0;
    private int turnNumber = 0;
    private final List<Agent> agents;
    private final Board board;
    private final Dice dice;
    private final int maxRounds;
    private final Random random = new Random();

	/**
	 * Initializes the simulation controller with necessary game components.
	 * @param agents		The array of 4 agents participating agents.
	 * @param board			The game board contaning tiles, nodes, and edges.
	 * @param die			The multi-dice component for rolling.
	 * @param maxRounds		The limit of rounds to simulate before termination.
	*/
	public GamePlay(List<Agent> agents, Board board, Dice dice, int maxRounds){
		this.agents = agents;
		this.board = board;
		this.dice = dice;
		this.maxRounds = maxRounds;
	}

	/** 
	 * Executes the main simulation loop. The process continues until an agent 
	 * reaches 10 victory points or the maximum round limit is reached.
	*/
	public void runSimulation() {
        boolean victoryAchieved = false;
        
        while (roundNumber < maxRounds && !victoryAchieved) {
            roundNumber++;
            for (Agent activeAgent : agents) {
                turnNumber++;
                executeTurn(activeAgent);

                if (activeAgent.getVictoryPoints() >= 10){
                    victoryAchieved = true;
                    break;
                }
            }
            printRoundSummary();
        }
    }

	/**
	 * Handles the logic for an individual agent's turn, including resource 
	 * production and mandatory building actions if resources exceed limits.
	 * @param agent	The agent currently taking their turn.
	 */
	private void executeTurn(Agent agent){
        int roll = dice.roll();

        if (roll != 7) {
            distributeResources(roll);
        } else {
            handleSevenRoll();
        }

        if (agent.getHandSize() > 7) {
            performRandomBuildAction(agent);
        }
    }

	/**
	 * Matches the current dice roll against tile values on the board and distributes 
	 * resources to agents with adjacent buildings.
	 * @param roll	The integer value resulting from the dice roll.
	 */
	private void distributeResources(int roll){
        for (int i = 0; i < 19; i++){
            Tile currentTile = board.getTile(i);

            if (currentTile.getRollValue() == roll){
                Resources producedResource = currentTile.getResource();

                for (Node adjacentNode : currentTile.getAdjacentNodes()) {
                    Building building = board.getBuildingAtNode(adjacentNode);

                    if (building != null){
                        Agent owner = building.getAgent();
                        int amount = (building instanceof City) ? 2 : 1; 

                        for (int j = 0; j < amount; j++){
                            owner.addResource(producedResource);
                        }
                    }
                }
            } 
        } 
    }

	/**
	 * Implements a simple linear  heck of all valid actions and picks one
	 * randomly to satisfy the overflow requirements. 
	 * @param agent		The agent performing a build action.
	 */
	private void performRandomBuildAction(Agent agent){
        List<Runnable> possibleActions = new ArrayList<>();

        // Check for valid Settlement placements
        for (int i = 0; i < 54; i++) {
            Node node = board.getNode(i);
            if (canPlaceSettlement(agent, node)) {
                possibleActions.add(() -> board.placeSettlement(node, new Settlement(agent, node)));
            }
        }

        // Check for valid City upgrades
        for (int i = 0; i < 54; i++) {
            Node node = board.getNode(i);
            if (canPlaceCity(agent, node)) {
                possibleActions.add(() -> board.placeCity(node, new City(agent, node), agent));
            }
        }

        if (!possibleActions.isEmpty()) {
            possibleActions.get(random.nextInt(possibleActions.size())).run();
        }
    }

	/**
	 *Enforces the Distance Rule: a settlement may only be built if all 3 adjacent   
	 *intersections are vacant
	*/
	private boolean canPlaceSettlement(Agent agent, Node node){
        if (board.getBuildingAtNode(node) != null) return false;
        for (Node neighbor : node.getAdjacentNodes()) {
            if (board.getBuildingAtNode(neighbor) != null) return false;
        }
        return true;
    }

	public String placeRoad(Agent agent, Edge edge){
        if(board.getRoadAtEdge(edge) != null){
            return "There's already a road placed here.";
        }

        for(Node node: edge.getNodes()){
            Building building = board.getBuildingAtNode(node);
            if (building != null && building.getAgent().equals(agent)) {
                return "Road placed.";
            }

            // Corrected to use board's edgeRoads getter
            for (Map.Entry<Edge, Road> entry: board.getEdgeRoads().entrySet()){
                Edge existingEdge = entry.getKey();
                Road road = entry.getValue();

                if (!road.getAgent().equals(agent)) {
                    continue;
                }

                for(Node adjacentNode: existingEdge.getNodes()){
                    if(adjacentNode.equals(node)){
                        // Assuming board has a put method or access for roads
                        return "Road placed.";
                    }
                }
            }
        }
        return "Cannot place road here.";
    }

	
	/**
     * Enforces the upgrade rule: cities must replace existing settlements
     */
	private boolean canPlaceCity(Agent agent, Node node) {
        Building b = board.getBuildingAtNode(node);
        return b instanceof Settlement && b.getAgent().equals(agent);
    }

	/**
     * Implements the discard rule for a 7 roll: every player with more than 7 
     * cards must discard half (rounded down)
     */
    private void handleSevenRoll() {
        for (Agent a : agents) {
            int handSize = a.getHandSize();
            if (handSize > 7) {
                int discardCount = handSize / 2; 
                for (int i = 0; i < discardCount; i++) {
                    Resources r = a.getRandomResourceFromHand();
                    a.removeResource(r);
                }
            }
        }
    }

	/**
     * Outputs the current victory points at the end of each round.
     * Format: [RoundNumber / [PlayerID]: [Action]
     */
    private void printRoundSummary() {
        System.out.println("--- End of Round " + roundNumber + " ---");
        for (Agent a : agents) {
            System.out.println("[" + roundNumber + " / " + a.getName() + "]: Status Check - " + a.getVictoryPoints() + " VPs");
        }
    }

	/**
     * Returns the total number of turns elapsed.
     * @return current turnNumber
     */
	public int getTurnNumber() {
        return this.turnNumber;
    }

}//ends class GamePlay
