# Simulated Annealing for Graph Coloring 🎨🔥

This project implements a **Simulated Annealing algorithm** to solve the **Graph Coloring Problem** through an interactive GUI. The goal is to color the vertices of a graph such that no two adjacent vertices share the same color — using the minimal number of conflicts.

## 📌 Features

- **Graph Representation**:  
  - Users can **interactively build any graph** using a graphical user interface (GUI).  
  - Internally, the graph is represented using an **adjacency list**.

- **Initial Random Solution**:  
  - Vertices are initially assigned random colors from a user-defined set.  
  - Users can **select the number of colors** used for the coloring process.

- **Simulated Annealing Implementation**:  
  - Core algorithm includes **temperature**, **cooling rate**, and random neighbor selection.  
  - Users can **tune parameters** such as initial temperature and cooling schedule.  
  - Real-time **visualization of coloring changes** and conflict minimization is provided.

- **Output**:  
  - Displays the **best solution** found with:
    - Color assigned to each vertex.
    - **Number of conflicts** (adjacent vertices with same color).
    - **Total iterations** and final state.

---

## 🛠️ How It Works

Simulated Annealing is a probabilistic technique that explores the solution space and accepts worse solutions with decreasing probability to avoid local minima.

**Steps:**
1. Start with a random coloring.
2. Randomly select a vertex and try changing its color.
3. Accept the new state based on energy difference and temperature.
4. Gradually cool down the system.
5. Repeat until convergence or stopping condition.
