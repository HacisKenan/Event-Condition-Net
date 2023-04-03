package simulation;

import Implementation.ECNet;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class RunnableECNet implements Runnable{

    private ECNet.Place startPlace;
    private BlockingQueue<ECNet.Transition> trace;

    public RunnableECNet(ECNet.Place p, BlockingQueue<ECNet.Transition> trace)
    {
        startPlace=p;
        this.trace = trace;
    }

    @Override
    public void run() {
        startPlace.setTokenPlaced();
        if(startPlace.neighbors.isEmpty()) return;
        synchronized(this)
        {
            while(startPlace.neighbors.stream().filter(t -> t.readyToFire()).findAny()== null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ECNet.Transition transition= startPlace.neighbors.stream().filter(t -> t.readyToFire()).findAny().get();
            try {
                if(trace.isEmpty())
                {
                    trace.put(transition);
                }
                else if(!(trace.peek().getId()==transition.getId()))
                {
                    trace.put(transition);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<ECNet.Place> places = transition.getPostPlaces();
            Collections.shuffle(places);

            List<Thread> threads = places.stream().map(p-> new Thread(new RunnableECNet(p,trace))).toList();

            threads.stream().forEach(t->t.start());
            notifyAll();
            startPlace.setTokenPlacedNot();
            threads.stream().forEach(t-> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
