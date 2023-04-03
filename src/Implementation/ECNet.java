package Implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ECNet {

    public class Transition {
        private List<Place> preCondition = new ArrayList<>();
        private List<Place> postCondition = new ArrayList<>();

        private int id;

        public Transition(int id) {
            this.id = id;
        }

        public void addPreCondition(Place place) {
            preCondition.add(place);
        }

        public void addPostCondition(Place place) {
            postCondition.add(place);
        }

        public synchronized boolean readyToFire() {
            for (Place place : preCondition) {
                if (place.tokenPlaced != true) return false;
            }
            return true;
        }

        public List<Place> getPostPlaces() {
            return postCondition;
        }

        @Override
        public String toString() {
            return "T" + id;
        }

        public int getId() {
            return id;
        }
    }

    public class Place {
        public boolean tokenPlaced;
        public int id;
        public List<Transition> neighbors = new ArrayList<>();

        private Place(int id) {
            tokenPlaced = false;
            this.id = id;
        }

        public synchronized void setTokenPlaced() {
            tokenPlaced = true;
        }

        public synchronized void setTokenPlacedNot(){tokenPlaced=false;}


        @Override
        public String toString() {
            return "Place{" +
                    "id=" + id +
                    '}';
        }
    }

    private List<Transition> transitions = new ArrayList<>();
    private List<Place> places = new ArrayList<>();

    public ECNet(String path, String places, String transitions) {
        String[] placesArrStr = places.split(",");
        Arrays.stream(placesArrStr).forEach(line -> this.places.add(new Place(Integer.parseInt(line.substring(1)))));

        String[] transitionArr = transitions.split(",");
        Arrays.stream(transitionArr).forEach(line -> this.transitions.add(new Transition(Integer.parseInt(line.substring(1)))));

        String[] pathArr = path.split(",");
        Arrays.stream(pathArr).map(line -> line.replace("(", "")).map(line -> line.replace(")", ""))
                .forEach(line -> {
                    String[] arr = line.split(";");
                    int id1 = Integer.parseInt(arr[0].substring(1));
                    int id2 = Integer.parseInt(arr[1].substring(1));
                    if (arr[0].startsWith("p")) {
                        this.transitions.stream().filter(x -> x.id == id2).findFirst().get().addPreCondition(this.places.stream().filter(x -> x.id == id1).findFirst().get());
                        this.places.stream().filter(x -> x.id == id1).findFirst().get().neighbors.add(this.transitions.stream().filter(x -> x.id == id2).findFirst().get());
                    } else {
                        this.transitions.stream().filter(x -> x.id == id1).findFirst().get().addPostCondition(this.places.stream().filter(x -> x.id == id2).findFirst().get());
                    }
                });
    }

    public Place getFirstPlace() {
        return places.get(0);
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Place> getPlaces() {
        return places;
    }
}
