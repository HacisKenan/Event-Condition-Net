package gui;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;

public class testPanel extends JFrame {

    public testPanel() {
        super("Petri-Netz Beispiel");

        // Erstellen eines neuen Petri-Netzes
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        // Erstellen der Knoten
        Object place1 = graph.insertVertex(parent, null, "p1", 50, 50, 60, 60, "shape=ellipse");
        Object place2 = graph.insertVertex(parent, null, "p2", 250, 50, 60, 60, "shape=ellipse");
        Object transition1 = graph.insertVertex(parent, null, "T1", 150, 50, 40, 80, "shape=rectangle");
        Object transition2 = graph.insertVertex(parent, null, "T2", 150, 150, 40, 80, "shape=rectangle");

        // Erstellen der Kanten
        graph.insertEdge(parent, null, "", transition1, place1);
        graph.insertEdge(parent, null, "", place1, transition2);
        graph.insertEdge(parent, null, "", transition2, place2);

        // Anwenden eines Layouts
        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
        layout.execute(parent);

        // Erstellen der Swing-Komponente
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        // Einstellen der Größe und des Verhaltens des Fensters
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
        new testPanel();
    }
}
