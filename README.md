# Catan Simulation Engine
**team25_2AA4_Assignments-2026W** </br>
**Course:** SFWRENG 2AA4  </br>
**Project:** Assignment 1 - System Architecture & Implementation </br>

## Project Overview
This project is a discrete-event simulator for the board game **Settlers of Catan**. The system models a 19-tile hexagonal grid, four autonomous agents, and a resource distribution engine. The simulation runs for a maximum of 8,192 rounds or until an agent achieves 10 victory points.

### Resource Production & Distribution
The simulation uses a "Tile-to-Node" navigation strategy:
1. The `Dice` roll a value between 2 and 12.
2. `GamePlay` iterates through the `Board` to find all `Tiles` matching that roll value.
3. Each active `Tile` queries its `AdjacentNodes()` to find surrounding intersections.
4. The `Board` checks if those nodes contain a `Building`. If so, it identifies the `Agent` (owner) and distributes the appropriate resource type.

---

## How to Run
1. Ensure all `.java` files are located within the `Assignment1` package directory.
2. Compile the project:
   ```bash
   javac Assignment1/*.java
   
[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=sharmaankita3387_team25_2AA4_Assignments_2026W) 
