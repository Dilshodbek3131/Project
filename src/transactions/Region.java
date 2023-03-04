package transactions;

import java.util.*;

public class Region {
    private String name;
    private List<Place> places = new ArrayList<>();
    private Set<Carrier> carriers = new HashSet<>();

    public Region(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Carrier> getCarriers() {
        return carriers;
    }

    public void addCarrier(Carrier carrier) {
        carriers.add(carrier);
    }

    public void addPlace(Place place){
        places.add(place);
    }

    public Region(List<Place> places) {
        this.places = places;
    }
}
