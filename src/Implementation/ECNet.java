package Implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ECNet {
    /** inner class for events **/
    public class Event {
        private int id;
        private List<Place> preCondition = new ArrayList<>();   /** places before the event **/
        private List<Place> postCondition = new ArrayList<>();  /** places after the event **/

        public Event(int id) {
            this.id = id;
        }

        public void addPreCondition(Place place) {preCondition.add(place);}
        public void addPostCondition(Place place) {postCondition.add(place);}

        /** this method checks if all of the places before the event have an token placed **/
        public synchronized boolean readyToFire() {
            for (Place place : preCondition) {
                if (place.tokenPlaced != true) return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "T" + id;
        }

        /** getters **/
        public List<Place> getPreCondition() {return preCondition;}
        public List<Place> getPostCondition() {return postCondition;}
        public int getId() {return id;}
    }

    /** inner class for places **/
    public class Place {
        private boolean tokenPlaced;
        private int id;
        private List<Event> neighbors = new ArrayList<>();

        private Place(int id) {
            tokenPlaced = false;
            this.id = id;
        }

        /** places a token **/
        public synchronized void setTokenPlaced() {
            tokenPlaced = true;
        }

        /** removes a token **/
        public synchronized void setTokenPlacedNot() {
            tokenPlaced = false;
        }

        @Override
        public String toString() {
            return "p" + id;
        }

        /** getters **/
        public int getId() {return id;}
        public boolean isTokenPlaced() {return tokenPlaced;}
        public List<Event> getNeighbors() {return neighbors;}
    }

    /** attributes for main class ECNet **/
    private List<Event> events = new ArrayList<>();
    private List<Place> places = new ArrayList<>();
    private List<Place> startPlaces = new ArrayList<>();

    /** the ECNet gets his informations for places, edges and events from Strings that are in set notation **/
    public ECNet(String path, String places, String events,String startPlaces) {
        String[] placesArrStr = places.split(",");  /** store each place into an element of an array **/
        Arrays.stream(placesArrStr).forEach(line -> this.places.add(new Place(Integer.parseInt(line.substring(1)))));   /** extract id from string and add new place to places attribute **/

        String[] EventArr = events.split(",");
        Arrays.stream(EventArr).forEach(line -> this.events.add(new Event(Integer.parseInt(line.substring(1)))));

        String[] pathArr = path.split(",");

        /** iterate over each edge, extract ids, get event/place according to id and
         *  build the ECNet by adding pre/post-conditions to each event and neighbors to each place **/
        Arrays.stream(pathArr).map(line -> line.replace("(", "")).map(line -> line.replace(")", ""))
                .forEach(line -> {
                    String[] arr = line.split(";");
                    int id1 = Integer.parseInt(arr[0].substring(1));
                    int id2 = Integer.parseInt(arr[1].substring(1));

                    if (arr[0].startsWith("p")) {
                        this.events.stream().filter(x -> x.id == id2).findFirst().get().
                                addPreCondition(this.places.stream().filter(x -> x.id == id1).findFirst().get());
                        this.places.stream().filter(x -> x.id == id1).findFirst().get().
                                getNeighbors().add(this.events.stream().filter(x -> x.id == id2).findFirst().get());
                    } else {
                        this.events.stream().filter(x -> x.id == id1).findFirst().get().
                                addPostCondition(this.places.stream().filter(x -> x.id == id2).findFirst().get());
                    }
                });

        /** extract starting places from string and store them in startPlaces attribute **/
        String[] startingP = startPlaces.split(",");
        Arrays.stream(startingP).forEach(p->this.startPlaces.add(this.places.stream()
                .filter(k -> Integer.parseInt(p.substring(1))==k.getId()).findFirst().get()));
    }

    /** getters **/
    public List<Place> getStartPlaces() {return startPlaces;}
    public List<Event> getEvents() {return events;}
    public List<Place> getPlaces() {return places;}
}
