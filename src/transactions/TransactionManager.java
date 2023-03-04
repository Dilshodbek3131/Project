package transactions;

import java.util.*;

public class TransactionManager {

    Map<String, Region> regionMap = new HashMap<>();
    Map<String, Carrier> carrierMap = new HashMap<>();
    Map<String, Place> placeMap = new HashMap<>();
    Map<String, Request> requestsMap = new HashMap<>();
    Map<String, Offer> offersMap = new HashMap<>();
    Map<String, Transactions> transactionsMap = new HashMap<>();

    //R1
    public List<String> addRegion(String regionName, String... placeNames) {
        Set<String> regionPlaces = new TreeSet<>();
        Region region = new Region(regionName);
        regionMap.put(regionName, region);
        for (String placeName : placeNames) {
            Place place = new Place(placeName);
            if (!placeMap.containsKey(placeName)) {
                regionPlaces.add(placeName);
            }
            placeMap.put(placeName, place);
        }

        return new ArrayList<>(regionPlaces);

    }

    public List<String> addCarrier(String carrierName, String... regionNames) {
        Carrier carrier = new Carrier(carrierName);
        for (String regionName : regionNames) {
            if (regionMap.containsKey(regionName)) {
                carrier.addRegion(regionMap.get(regionName));
            }
        }
        List<String> carrierRegions = new LinkedList<>();
        for (Region region : carrier.getRegions()) {
            carrierRegions.add(region.getName());

        }

        return carrierRegions;
    }

    public List<String> getCarriersForRegion(String regionName) {
        Region region = regionMap.get(regionName);
        List<String> carrierNames = new ArrayList<>(region.getCarriers().size());
        for (Carrier carrier : region.getCarriers()) {
            carrierNames.add(carrier.getName());
        }
        return carrierNames;
    }

    //R2
    public void addRequest(String requestId, String placeName, String productId) throws TMException {
        Place place = new Place(placeName);
        if (!placeMap.containsKey(placeName)) throw new TMException();
        if (requestsMap.containsKey(requestId)) throw new TMException();
        Request request = new Request(requestId, productId, place);
        requestsMap.put(requestId, request);
    }

    public void addOffer(String offerId, String placeName, String productId) throws TMException {
        Place place = new Place(placeName);
        if (!placeMap.containsKey(placeName)) throw new TMException();
        if (offersMap.containsKey(offerId)) throw new TMException();

        Offer offer = new Offer(offerId, productId, place);
        offersMap.put(offerId, offer);
    }

    //R3
    public void addTransaction(String transactionId, String carrierName, String requestId, String offerId) throws TMException {

        Carrier carrier = new Carrier(carrierName);
        Request request = requestsMap.get(requestId);
        Offer offer = offersMap.get(offerId);

        for (Transactions transactions : transactionsMap.values())
            if (transactions.getRequest().getProductId().equals(requestId) || transactions.getOffer().getRequestId().equals(offerId))
                throw new TMException();
        // TODO: 24.02.2023 check later
        if (!request.getProductId().equals(offer.getProductId())) throw new TMException();
        if (carrier.getRegions().contains(request.getPlace().getRegion())) throw new TMException();
        if (carrier.getRegions().contains(offer.getPlace().getRegion())) throw new TMException();
        Transactions transactions = new Transactions(transactionId, carrier, request, offer);
        transactionsMap.put(transactionId, transactions);

    }

    public boolean evaluateTransaction(String transactionId, int score) {
        if (score < 1 || score > 10) return false;
        Transactions transactions = transactionsMap.get(transactionId);
        transactions.setScore(score);
        return true;
    }
    // TODO: 01.03.2023  

    //R4
    public SortedMap<Long, List<String>> deliveryRegionsPerNT() {
        Map<String, Long> map = new HashMap<>();
        for (Transactions transactions : transactionsMap.values()) {
            String regionName = transactions.getRequest().getPlace().getRegion().getName();
            if (!map.containsKey(regionName)) {
                map.put(regionName, 1L);
            } else {
                map.put(regionName, map.get(regionName) + 1L);
            }

        }
        System.out.println(map);


        return new TreeMap<Long, List<String>>();
    }

    public SortedMap<String, Integer> scorePerCarrier(int minimumScore) {
        return new TreeMap<String, Integer>();
    }

    public SortedMap<String, Long> nTPerProduct() {
        return new TreeMap<String, Long>();
    }
}