package Implementation;

import simulation.RunnableECNet;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class testMain {
    public static void main(String[] args) throws InterruptedException {
        ECNet ecNet = new ECNet("(p0;T0),(T0;p1),(T0;p2),(p1;T2),(p2;T3),(T3;p3),(T2;p4),(p4;T4),(p3;T4),(T4;p5)","p0,p1,p2,p3,p4,p5","T0,T1,T2,T3,T4");
        BlockingQueue<ECNet.Transition> trace = new LinkedBlockingQueue<>();
        Thread test = new Thread(new RunnableECNet(ecNet.getFirstPlace(),trace));
        BlockingQueue<BlockingQueue<ECNet.Transition>> traces = new LinkedBlockingQueue<>();

        for(int i=0;i<5;i++)
        {
            trace.clear();
            test.run();
            test.join();
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
        String out ="";
        for(BlockingQueue<ECNet.Transition> t :traces)
        {
            out+=t.toString();
            out+=System.lineSeparator();
        }
        System.out.println(out);
        //traces.stream().forEach(x->x.stream().forEach(System.out::println));
       // trace.stream().forEach(System.out::println);
    }


}
