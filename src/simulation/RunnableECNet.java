package simulation;

import Implementation.ECNet;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class RunnableECNet implements Runnable{

    /** startPlace is the place where the thread starts and activates the token
     *  trace is the accumulation of places where the tokens have been activated **/
    private ECNet.Place startPlace;
    private BlockingQueue<ECNet.Event> trace;

    public RunnableECNet(ECNet.Place p, BlockingQueue<ECNet.Event> trace) {
        startPlace=p;
        this.trace = trace;
    }

    @Override
    public void run() {
        startPlace.setTokenPlaced();    /** activate token **/

        if(startPlace.getNeighbors().isEmpty()) return;     /** check if we are at last token **/

        synchronized(this)
        {
            /** check if any event is ready to fire, if not wait until notified by other thread **/
            while(startPlace.getNeighbors().stream().filter(t -> t.readyToFire()).findAny()== null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /** find any event that is ready to fire **/
            ECNet.Event event = startPlace.getNeighbors().stream().filter(t -> t.readyToFire()).findAny().get();

            try {
                if(trace.isEmpty())
                {
                    trace.put(event);
                }
                else if(!(trace.peek().getId()==event.getId())) /** avoid putting two identical events in a row **/
                {
                    trace.put(event);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /** extract post places from chosen event and shuffle to simulate randomization **/
            List<ECNet.Place> places = event.getPostCondition();
            Collections.shuffle(places);

            /** get all possible threads for the post places in a list **/
            List<Thread> threads = places.stream().map(p-> new Thread(new RunnableECNet(p,trace))).toList();

            /** start all threads for every post place and notify all other threads **/
            threads.stream().forEach(t->t.start());
            notifyAll();

            /** consume token by removing it **/
            startPlace.setTokenPlacedNot();

            /** wait until every thread for every post place is done, so one doesn't start immediately again to avoid
             *  inconsistencies **/
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
