package gui;

import simulation.RunnableECNet;
import Implementation.ECNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;


public class GUI extends JFrame {
    private JTextField stellen;
    private JTextField transitionen;
    private JTextField kanten;
    private JTextArea tracesF;
    private JButton button;
    private JButton reset;
    private mxGraphComponent mxGC;
    private JPanel scrollPaneContent = new JPanel();
    public GUI() {
        // Set up the frame
        setTitle("Event-Condition Nets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up the input fields
        stellen = new JTextField(15);
        transitionen = new JTextField(15);
        kanten = new JTextField(15);

        stellen.setText("p0,p1,p2,p3,p4,p5");
        transitionen.setText("T0,T2,T3,T4");
        kanten.setText("(p0;T0),(T0;p1),(T0;p2),(p1;T2),(p2;T3),(T3;p3),(T2;p4),(p4;T4),(p3;T4),(T4;p5)");

        // Set up the output field
        tracesF = new JTextArea();
        tracesF.setEditable(false);

        // Set up the button
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tracesF.setText("");
                stellen.setText("");
                transitionen.setText("");
                kanten.setText("");
            }
        });

        button = new JButton("Simulation starten");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tracesF.setText("");
                if(stellen.getText().length()<=0 || transitionen.getText().length()<=0 || kanten.getText().length()<0)
                {
                    tracesF.setText("Eingabe unvollständig");
                }
                ECNet ecNet = new ECNet(kanten.getText(),stellen.getText(),transitionen.getText());
                BlockingQueue<ECNet.Transition> trace = new LinkedBlockingQueue<>();
                Thread test = new Thread(new RunnableECNet(ecNet.getFirstPlace(),trace));
                BlockingQueue<BlockingQueue<ECNet.Transition>> traces = new LinkedBlockingQueue<>();

                for(int i=0;i<100;i++)
                {
                    trace.clear();
                    test.run();
                    try {
                        test.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    if(traces.stream().allMatch(x->{
                        List<ECNet.Transition> list = trace.stream().toList();
                        List<ECNet.Transition> xList = x.stream().toList();
                        if(list.size()!=xList.size()) return true;

                        for(int k=0;k<list.size();k++)
                        {
                            if(list.get(k)!= xList.get(k))
                            {
                                return true;
                            }
                        }
                        return false;
                    }))
                    {
                        if(trace.stream().toList().get(trace.size()-1).getId()!=trace.stream().toList().get(trace.size()-2).getId())
                        {
                            traces.add(new LinkedBlockingQueue<>(trace));
                        }
                    }
                }
                int k=0;
                String out ="";
                for(BlockingQueue<ECNet.Transition> t :traces)
                {
                    out+="Trace " + k++ +": " +t.toString();
                    out+=System.lineSeparator();
                }
                tracesF.setText(out);
                mxGC=getMxGraphComp(ecNet);
                scrollPaneContent.add(mxGC);
            }
        });

        // Set up the layout
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JScrollPane j = new JScrollPane();


        scrollPaneContent.setPreferredSize(new Dimension(500,500));

        j.setViewportView(scrollPaneContent);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("Stellen:"), c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Transitionen:"), c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Kanten:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        stellen.setMaximumSize(new Dimension(Integer.MAX_VALUE, stellen.getPreferredSize().height));
        panel.add(stellen, c);

        c.gridx = 1;
        c.gridy = 1;
        transitionen.setMaximumSize(new Dimension(Integer.MAX_VALUE, transitionen.getPreferredSize().height));
        panel.add(transitionen, c);

        c.gridx = 1;
        c.gridy = 2;
        kanten.setMaximumSize(new Dimension(Integer.MAX_VALUE, kanten.getPreferredSize().height));
        panel.add(kanten, c);

        // Output field
        c.gridx = 0;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth=3;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 3.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 10, 5, 5);
        tracesF.setMaximumSize(new Dimension(Integer.MAX_VALUE, tracesF.getPreferredSize().height));
        panel.add(tracesF, c);

        // Buttons
        c.gridx = 1;
        c.gridy = 4;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(10, 5, 5, 5);
        panel.add(button, c);

        c.gridx = 2;
        c.gridy = 4;
        panel.add(reset, c);

// Set up colors
        Color background = new Color(250, 250, 250);
        Color foreground = new Color(50, 50, 50);
        Color inputBackground = new Color(230, 230, 230);
        Color buttonBackground = new Color(200, 200, 200);

        panel.setBackground(background);
        stellen.setBackground(inputBackground);
        transitionen.setBackground(inputBackground);
        kanten.setBackground(inputBackground);
        tracesF.setBackground(inputBackground);
        tracesF.setForeground(foreground);
        button.setBackground(buttonBackground);
        button.setForeground(foreground);
        reset.setBackground(inputBackground);
        reset.setForeground(foreground);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        content.add(j, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        content.add(panel, gbc);
        add(content, BorderLayout.CENTER);


        setSize(800, 500);
        setVisible(true);

    }


    public mxGraphComponent getMxGraphComp(ECNet ecNet)
    {
        mxGraph mxG = new mxGraph();
        Object parent = mxG.getDefaultParent();
        List<Object> places = new ArrayList<>(ecNet.getPlaces());
        List<Object> transitions = new ArrayList<>(ecNet.getTransitions());

        places=places.stream().map(p -> mxG.insertVertex(parent,null,"p"+((ECNet.Place)p).id,50, 50, 60, 60,"shape=ellipse")).toList();
        transitions=transitions.stream().map(t-> mxG.insertVertex(parent, null, "T"+((ECNet.Transition)t).getId(), 150, 50, 40, 80, "shape=rectangle")).toList();

        for(int i=0;i<places.size();i++)
        {
            Object p = places.get(i);
            for(ECNet.Transition t : ecNet.getPlaces().stream().filter(k -> k.id==Integer.parseInt((String)mxG.getModel().getValue(p).toString().substring(1)))
                    .findFirst().get().neighbors)
            {
                Object tran = transitions.stream().filter(k->Integer.parseInt(mxG.getModel().getValue(k).toString().substring(1))==t.getId()).findFirst().get();
                mxG.insertEdge(parent, null, "", p, tran);
            }
        }

        for(int i=0;i<transitions.size();i++)
        {
            Object t = transitions.get(i);
            for(ECNet.Place p : ecNet.getTransitions().stream().filter(k -> k.getId()==Integer.parseInt((String)mxG.getModel().getValue(t).toString().substring(1)))
                    .findFirst().get().getPostPlaces())
            {
                Object plac = places.stream().filter(k->Integer.parseInt(mxG.getModel().getValue(k).toString().substring(1))==p.id).findFirst().get();
                mxG.insertEdge(parent, null, "", t, plac);
            }
        }

        mxCompactTreeLayout layout = new mxCompactTreeLayout(mxG);
        layout.execute(parent);
        mxGraphComponent mxGC = new mxGraphComponent(mxG);
        return mxGC;
    }
}
