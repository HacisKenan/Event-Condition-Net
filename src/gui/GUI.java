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
    /** attributes
     *  textfields for places/events/edges to input ECNet in set notation
     *  textarea to output traces
     *  button to start simulation
     *  button to reset input
     *  mxGC to illustrate ECNet on GUI
     *  Panel for illustration of ECNet **/
    private JTextField places;
    private JTextField events;
    private JTextField edges;
    private JTextArea tracesF;
    private JButton button;
    private JButton reset;
    private mxGraphComponent mxGC;
    private JPanel scrollPaneContent = new JPanel();

    public GUI() {
        /** set up the frame **/
        setTitle("Event-Condition Nets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /** set up input fields **/
        places = new JTextField(15);
        events = new JTextField(15);
        edges = new JTextField(15);

        /** test purposes only to save time, typing in all of the inputs **/
        places.setText("p0,p1,p2,p3,p4,p5");
        events.setText("T0,T2,T3,T4");
        edges.setText("(p0;T0),(T0;p1),(T0;p2),(p1;T2),(p2;T3),(T3;p3),(T2;p4),(p4;T4),(p3;T4),(T4;p5)");

        /** set up output area **/
        tracesF = new JTextArea();
        tracesF.setEditable(false);

        /** set up reset button to erase all content from every input field and output area **/
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tracesF.setText("");
                places.setText("");
                events.setText("");
                edges.setText("");
            }
        });

        /** set up simulation button to start simulation **/
        button = new JButton("start simulation");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tracesF.setText("");

                /** check for invalid input **/
                if(places.getText().length()<=0 || events.getText().length()<=0 || edges.getText().length()<0)
                {
                    tracesF.setText("invalid input");
                }

                /** extract input from inputfields and initialize ECNet **/
                ECNet ecNet = new ECNet(edges.getText(),places.getText(),events.getText());
                BlockingQueue<ECNet.Event> trace = new LinkedBlockingQueue<>();
                Thread test = new Thread(new RunnableECNet(ecNet.getFirstPlace(),trace));
                BlockingQueue<BlockingQueue<ECNet.Event>> traces = new LinkedBlockingQueue<>();

                /** run simulation 100 times to get all possible outputs **/
                for(int i=0;i<100;i++)
                {
                    trace.clear();
                    test.run();
                    try {
                        test.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    /** check if trace is already in traces queue **/
                    if(traces.stream().allMatch(x->{
                        List<ECNet.Event> list = trace.stream().toList();
                        List<ECNet.Event> xList = x.stream().toList();
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

                /** output text in textarea **/
                int k=0;
                String out ="";
                for(BlockingQueue<ECNet.Event> t :traces)
                {
                    out+="Trace " + k++ +": " +t.toString();
                    out+=System.lineSeparator();
                }
                tracesF.setText(out);

                /** get mxGraph and add it to the according panel **/
                mxGC=getMxGraphComp(ecNet);
                scrollPaneContent.add(mxGC);
            }
        });

        /** set up the layout **/
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JScrollPane j = new JScrollPane();
        scrollPaneContent.setPreferredSize(new Dimension(500,500));
        j.setViewportView(scrollPaneContent);

        /** input fields **/
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("places:"), c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("events:"), c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("edges:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        places.setMaximumSize(new Dimension(Integer.MAX_VALUE, places.getPreferredSize().height));
        panel.add(places, c);

        c.gridx = 1;
        c.gridy = 1;
        events.setMaximumSize(new Dimension(Integer.MAX_VALUE, events.getPreferredSize().height));
        panel.add(events, c);

        c.gridx = 1;
        c.gridy = 2;
        edges.setMaximumSize(new Dimension(Integer.MAX_VALUE, edges.getPreferredSize().height));
        panel.add(edges, c);

        /** output field **/
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

        /** buttons **/
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

        /** set up background and foreground colors **/
        Color background = new Color(250, 250, 250);
        Color foreground = new Color(50, 50, 50);
        Color inputBackground = new Color(230, 230, 230);
        Color buttonBackground = new Color(200, 200, 200);

        panel.setBackground(background);
        places.setBackground(inputBackground);
        events.setBackground(inputBackground);
        edges.setBackground(inputBackground);
        tracesF.setBackground(inputBackground);
        tracesF.setForeground(foreground);
        button.setBackground(buttonBackground);
        button.setForeground(foreground);
        reset.setBackground(inputBackground);
        reset.setForeground(foreground);

        /** add scrollpane j to main panel **/
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(j, gbc);

        /** add panel with input, output and buttons to main panel **/
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        content.add(panel, gbc);

        /** add main panel to frame **/
        add(content, BorderLayout.CENTER);

        /** set size of frame and turn it visible **/
        setSize(800, 500);
        setVisible(true);

    }

    /** this method maps the ECNet to an mxGraph to make use of its visualization features **/
    public mxGraphComponent getMxGraphComp(ECNet ecNet)
    {
        mxGraph mxG = new mxGraph();
        Object parent = mxG.getDefaultParent();
        List<Object> places = new ArrayList<>(ecNet.getPlaces());
        List<Object> events = new ArrayList<>(ecNet.getEvents());

        /** map places and events to Objects and insert vertex to mxGraph accordingly **/
        places=places.stream().map(p -> mxG.insertVertex(parent,null,
                "p"+((ECNet.Place)p).getId(),50, 50, 60, 60,"shape=ellipse")).toList();
        events=events.stream().map(t-> mxG.insertVertex(parent, null,
                "T"+((ECNet.Event)t).getId(), 150, 50, 40, 80, "shape=rectangle")).toList();

        /** iterate over places, extract neighbours by id and insert edges to mxGraph **/
        for(int i=0;i<places.size();i++)
        {
            Object p = places.get(i);
            for(ECNet.Event t : ecNet.getPlaces().stream().filter(k -> k.getId()==Integer.parseInt((String)mxG.
                            getModel().getValue(p).toString().substring(1))).findFirst().get().getNeighbors())
            {
                Object tran = events.stream().filter(k->Integer.parseInt(mxG.getModel().getValue(k).
                        toString().substring(1))==t.getId()).findFirst().get();
                mxG.insertEdge(parent, null, "", p, tran);
            }
        }

        /** iterate over events, extract post places by id and insert edges to mxGraph **/
        for(int i=0;i<events.size();i++)
        {
            Object t = events.get(i);
            for(ECNet.Place p : ecNet.getEvents().stream().filter(k -> k.getId()==Integer.parseInt((String)mxG.getModel().getValue(t).toString().substring(1)))
                    .findFirst().get().getPostCondition())
            {
                Object plac = places.stream().filter(k->Integer.parseInt(mxG.getModel().getValue(k).toString().substring(1))==p.getId()).findFirst().get();
                mxG.insertEdge(parent, null, "", t, plac);
            }
        }

        /** create mxCompactTreeLayout to execute parent and create mxGraphComponent **/
        mxCompactTreeLayout layout = new mxCompactTreeLayout(mxG);
        layout.execute(parent);
        mxGraphComponent mxGC = new mxGraphComponent(mxG);

        return mxGC;
    }
}
