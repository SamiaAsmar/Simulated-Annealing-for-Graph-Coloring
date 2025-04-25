/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.ai;

/**
 *
 * @author samiaasmar
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
 class Node {
    int id;
    int x, y;
    Color color;

    Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = Color.WHITE; // Set default color to white
    }
    @Override
    public String toString() {
        return "Node ID: " + id + ", Position: (" + x + ", " + y + ")";
    }
    public int getId(){
        return id;
    }
    public void setColor(Color color){
        this.color = color;
    }

}

  class Edge {
    Node node1;
    Node node2;

    public Edge(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }
}


class NodePanel extends JPanel {
    private final List<Node> nodes = new ArrayList<>();
    final List<Edge> edges = new ArrayList<>();
    private int nodeCounter = 0;
    private Node dragStartNode = null;
    private Point dragCurrentPoint = null;

    public NodePanel() {
        setBackground(new Color(204, 204, 204));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Node clickedNode = getNodeAt(e.getPoint());
                if (clickedNode != null) {
                    dragStartNode = clickedNode;
                    dragCurrentPoint = e.getPoint();
                    repaint();
                } else {
                    addNode(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragStartNode != null) {
                    Node endNode = getNodeAt(e.getPoint());
                    if (endNode != null && endNode != dragStartNode) {
                        if (!edgeExists(dragStartNode, endNode)) {
                            addEdge(dragStartNode, endNode);
                        }
                    }
                    dragStartNode = null;
                    dragCurrentPoint = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartNode != null) {
                    dragCurrentPoint = e.getPoint();
                    repaint();
                }
            }
        });
    }
     private Node getNodeAt(Point point) {
        for (Node node : nodes) {
            if (Math.hypot(node.x - point.x, node.y - point.y) < 20) {
                return node;
            }
        }
        return null;
    }
     private boolean edgeExists(Node node1, Node node2) {
        for (Edge edge : edges) {
            if ((edge.node1 == node1 && edge.node2 == node2) ||
                (edge.node1 == node2 && edge.node2 == node1)) {
                return true;
            }
        }
        return false;
    }

    private void addNode(int x, int y) {
    // Check if the new node is too close to existing nodes
    for (Node node : nodes) {
        if (Math.hypot(node.x - x, node.y - y) < 40) { // Check for proximity (20 for radius + 20 for new node)
            JOptionPane.showMessageDialog(this, "Cannot add node too close to another node.");
            return; // Don't add the node if it's too close
        }
    }
    
    nodes.add(new Node(nodeCounter++, x, y));
    
    repaint(); 
}
    
    


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draw edge
        for (Edge edge : edges) {
        g.setColor(Color.BLACK); 
        g.drawLine(edge.node1.x, edge.node1.y, edge.node2.x, edge.node2.y); 
    }

        // Draw nodes
        for (Node node : nodes) {
            int radius = 20; // Radius of the circle
            g.setColor(node.color); // Set node color
            g.fillOval(node.x - radius, node.y - radius, radius * 2, radius * 2); // Draw filled circle
            g.setColor(Color.BLACK); // Set color for text and outline
            g.drawOval(node.x - radius, node.y - radius, radius * 2, radius * 2); // Draw circle outline
            g.drawString(String.valueOf(node.id), node.x - 5, node.y + 5); // Draw node id
        }
    }

    
    public List<Node> getNodes() {
        return nodes;
    }
    
    public List<Edge> getEdges(){
        return edges;
    }
    public int getnodeCounter(){
        return nodeCounter;
    }
    public void setnodeCounter(int nodeCounter){
        this.nodeCounter = nodeCounter;
    }
    public void colorNodesRandomly(int numberOfColors) {
        Random random = new Random();
        Color[] colors = new Color[numberOfColors];
        for(int i = 0 ; i < numberOfColors ; i++ ){
            colors[i] = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
        }
        for(Node node : nodes){
            node.color = colors[random.nextInt(numberOfColors)];
        }
        repaint();
    }
    
    public void addEdge(Node node1, Node node2) {
    Edge edge = new Edge(node1, node2); 
    edges.add(edge); 
}

    
    public boolean removeEdgesBetweenNodes(int nodeId1, int nodeId2) {
    boolean removedAny = false;

    for (int i = edges.size() - 1; i >= 0; i--) {
        Edge edge = edges.get(i);
        if ((edge.node1.id == nodeId1 && edge.node2.id == nodeId2) ||
            (edge.node1.id == nodeId2 && edge.node2.id == nodeId1)) {
            edges.remove(i);
            removedAny = true;
        }
    }
    repaint();

    return removedAny;
}


    
    


   
}

public class Frame extends javax.swing.JFrame {
    private final NodePanel nodePanel;
        
    public Double calcTemp(int iteration, Double tempInitial, Double coolingRate) {
    Double tempCurrent = tempInitial * Math.pow(coolingRate, iteration);
    return tempCurrent;
}

public int calculateError(List<Node> nodes, List<Edge> edges) {
    int error = 0;
    for (Edge edge : edges) {
        Node node1 = edge.getNode1();
        Node node2 = edge.getNode2();
        if (node1.color.equals(node2.color)) { 
            error++;
        }
    }
    return error;
}

private Color[] copyColors(List<Node> nodes) {
    Color[] colors = new Color[nodes.size()];
    for (int i = 0; i < nodes.size(); i++) {
        colors[i] = nodes.get(i).color;
    }
    return colors;
}

public Color getNewColorForNode(Node node, Color[] availableColors) {
    Random rand = new Random();

    List<Color> colorList = new ArrayList<>(Arrays.asList(availableColors));
    
   // colorList.remove(node.color);

    /*if (colorList.isEmpty()) {
        return node.color; 
    }*/

    return colorList.get(rand.nextInt(colorList.size()));
}


    
    public Node SimulatedAnnealing(Node nodeCurrent, Double initialTemperature, Double coolingRate, int maxIteration) {
    Random rand = new Random();
    double currentTemperature = initialTemperature;
    int currentError = calculateError(nodePanel.getNodes(), nodePanel.getEdges());
    Node nodeBest = nodeCurrent;

    Color[] availableColors = copyColors(nodePanel.getNodes());

    for (int j = 0; j < maxIteration && currentTemperature > 0.1; j++) {
        int randomIndex = rand.nextInt(nodePanel.getNodes().size());
        Node nodeNext = nodePanel.getNodes().get(randomIndex);
        Color oldColor = nodeNext.color;

        // Try a new color for the selected node
        Color newColor = getNewColorForNode(nodeNext, availableColors);
        nodeNext.color = newColor;

        int nextError = calculateError(nodePanel.getNodes(), nodePanel.getEdges());

        // Accept new color based on error and temperature
        if (nextError < currentError || (Math.exp((currentError - nextError) / currentTemperature) > rand.nextDouble())) {
            currentError = nextError;
            nodeCurrent = nodeNext;
        } else {
            nodeNext.color = oldColor; // Revert color if not accepted
        }

        // Stop the algorithm if we have zero conflicts
        if (currentError == 0) {
            System.out.println("Solution found with no conflicts.");
            jLabel3.setText("Total Conflicts: " + currentError); // Update conflict count display
            break;
        }

        // Update temperature and repaint for visualization
        currentTemperature = calcTemp(j, initialTemperature, coolingRate);
        jPanel3.repaint();

        // Add a delay for visualization
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Final display of total conflicts
    jLabel3.setText("Total Conflicts: " + currentError); // Update JLabel with the conflict count
    jPanel3.repaint();
    return nodeBest;
}

        
        
    /**
     * Creates new form Frame
     */
    public Frame() {
       initComponents();
       jPanel3.setPreferredSize(new Dimension(1162, 800)); // تعيين الحجم المفضل لـ jPanel1

        nodePanel = new NodePanel();
        
        jPanel3.setLayout(new BorderLayout()); // Set layout for jPanel1
        jPanel3.add(nodePanel); // Add NodePanel to jPanel1
        jPanel3.revalidate(); // Update layout
        jPanel3.repaint(); // Repaint to show changes

    }
    
    
    



     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jPopupMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPopupMenu1MouseReleased(evt);
            }
        });

        jMenuItem1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jMenuItem1.setText("Delete Edge");
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setText("ADD Edge ➕");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton2.setText("Clear screen 🚫");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton4.setText("Random Color 🎨");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton3.setText("Remove Node ⛔");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton5.setText("Lets Start ! 🖐");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton6.setText("Delete Edge");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(340, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1168, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 779, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("🌟 Start creating your graph! 🌟 ");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel2.setText("Click on the panel to add a node.");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(162, 162, 162))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (nodePanel.getNodes().isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "🚫 Oops! It seems there are no nodes yet!\n\n No Nodes Available to color");
         }
        else{
            String msg = JOptionPane.showInputDialog(this, "🎨 Let’s add some color! How many colors do you want to use to paint the nodes?\n" +
           "Just type in a number!");
            
            int count = Integer.parseInt(msg);
            nodePanel.colorNodesRandomly(count);
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (nodePanel.getNodes().size() < 2) {
        JOptionPane.showMessageDialog(this,
            "🚫 Oops! It seems there are no nodes yet!\n\nPlease create at least two nodes before adding an edge.");
    }
    else {
        JOptionPane.showMessageDialog(this,
            "To add an edge:\n\n" +
            "1. Click and hold on first node\n" +
            "2. Drag to second node\n" +
            "3. Release mouse button\n\n" +
            "Note: Make sure nodes are not too close together",
            "Edge Adding Instructions",
            JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        nodePanel.setnodeCounter(0);
        nodePanel.getNodes().clear();
        nodePanel.getEdges().clear();
        repaint();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
       if (nodePanel.getNodes().isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "🚫 Oops! It seems there are no nodes yet!\n\n No Nodes Available to remove");
    } else {
        String msg = JOptionPane.showInputDialog(this, "Enter id of node you want to delete");

        try {
            int idNodeDelete = Integer.parseInt(msg);
            
            Node nodeToRemove = null;
            for (Node node : nodePanel.getNodes()) {
                if (node.getId() == idNodeDelete) {
                    nodeToRemove = node;
                    break;
                }
            }
            if (nodeToRemove != null) {
                nodePanel.getNodes().remove(nodeToRemove);
                
                List<Edge> edgesToRemove = new ArrayList<>();
                for (Edge edge : nodePanel.getEdges()) {
                    if (edge.getNode1() == nodeToRemove || edge.getNode2() == nodeToRemove) {
                        edgesToRemove.add(edge);
                    }
                }
                
                nodePanel.getEdges().removeAll(edgesToRemove);
                
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Node ID not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid node ID.");
        }
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
         if (nodePanel.getNodes().isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "🚫 Oops! It seems there are no nodes yet!\n\n No Nodes Available");
    } else {
        String msg1 = JOptionPane.showInputDialog(this, "Enter initial temperature: ");
        String msg2 = JOptionPane.showInputDialog(this, "Enter cooling rate: ");
        String msg3 = JOptionPane.showInputDialog(this, "Enter the maximum number of iteration : ");

        try {
            Double T = Double.parseDouble(msg1);
            Double CR = Double.parseDouble(msg2);
            int max = Integer.parseInt(msg3);

            // Validate input values
            if (T <= 0 || CR <= 0 || CR >= 1) {
    JOptionPane.showMessageDialog(this,
        "🚫 Please enter valid values:\n- Initial temperature must be positive\n- Cooling rate must be in the range (0, 1)");
} else {
    // Define and execute the SwingWorker for running Simulated Annealing in the background
    SwingWorker<Void, Node> worker = new SwingWorker<Void, Node>() {
        @Override
        protected Void doInBackground() {
            for (Node node : nodePanel.getNodes()) {
                SimulatedAnnealing(node, T, CR, max);
                publish(node); // Publish the node after each simulated annealing run on it
            }
            return null;
        }

        @Override
        protected void process(java.util.List<Node> chunks) {
            // Refresh the graph panel to show changes after each step of the algorithm
            for (Node node : chunks) {
                // You may call node-specific updates here if needed
            }
            jPanel3.repaint(); // Redraw the panel to reflect color changes
        }

        @Override
        protected void done() {
            // Display a message once the simulated annealing process is complete
            JOptionPane.showMessageDialog(Frame.this, "Simulated Annealing completed.");
        }
    };

    worker.execute();
}

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "🚫 Invalid input! Please enter numeric values.");
        }
    }

        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jPopupMenu1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPopupMenu1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jPopupMenu1MouseReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if(nodePanel.getEdges().size() == 0){
    JOptionPane.showMessageDialog(this, "There are no edges to remove!!!");
} else {
    String in1 = JOptionPane.showInputDialog("Please enter the number of the FIRST node for which you want to remove the connected edges.");
    String in2 = JOptionPane.showInputDialog("Please enter the number of the SECOND node for which you want to remove the connected edges.");
    
    try {
        int i1 = Integer.parseInt(in1);
        int i2 = Integer.parseInt(in2);
        
        // Assuming you have a method in your nodePanel to remove edges
        boolean removed = nodePanel.removeEdgesBetweenNodes(i1, i2);
        
        if (removed) {
            JOptionPane.showMessageDialog(this, "Edges between node " + i1 + " and node " + i2 + " have been removed.");
        } else {
            JOptionPane.showMessageDialog(this, "No edges found between node " + i1 + " and node " + i2 + ".");
            System.out.println("Available edges:");
            for (Edge edge : nodePanel.getEdges()) {
    System.out.println("Edge between Node " + edge.node1.id + " and Node " + edge.node2.id);
}

        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter valid numeric values for the node numbers.");
    }
}

    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(() -> new Frame().setVisible(true));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    // End of variables declaration//GEN-END:variables
}
