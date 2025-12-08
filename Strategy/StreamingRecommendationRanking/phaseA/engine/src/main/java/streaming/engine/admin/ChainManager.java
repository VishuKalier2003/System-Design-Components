package streaming.engine.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.Setter;
import streaming.engine.core.handler.ConcreteHandler;
import streaming.engine.data.input.HandlerInfo;
import streaming.engine.data.input.Request;
import streaming.engine.data.output.Output;
import streaming.engine.enums.data.RailType;
import streaming.engine.model.Strategy;
import streaming.engine.service.Store;
import streaming.engine.utils.HandlerStore;

@Setter
@Service
public class ChainManager {
    @Autowired private HandlerStore handlerStore;
    @Autowired private ChainEngine engine;
    @Autowired private Store store;
    @Autowired @Qualifier("RegisteredStrategy") private Map<String, Strategy> config;

    private final List<ConcreteHandler> chain = new ArrayList<>();
    private final Set<String> userDefined = new HashSet<>();
    private int chainLength = 0;

    // detail: creates via factory when respective handlers are passed
    public void attachRoot(HandlerInfo info) {
        if(chainLength == 0) {
            chain.add(handlerStore.createHandler(info.getHandlerName(), info.getHandlerRailType()));
            chainLength++;
        }
    }

    public void setHandlerCustomStrategy(int handlerIndex, String strategy) {
        ConcreteHandler handler = chain.get(handlerIndex);
        userDefined.add(handler.getName());
        handler.setCurrentStrategy(config.get(strategy));
    }

    public void resetConfiguration() {userDefined.clear();}

    public void deleteHandler(int index) {
        chain.remove(index);
        chainLength--;
    }

    public void attachNewHandler(HandlerInfo info) {
        ConcreteHandler handler = handlerStore.createHandler(info.getHandlerName(), info.getHandlerRailType());
        chain.add(handler);
        chainLength++;
    }

    public synchronized void swap(int index1, int index2) {
        ConcreteHandler temp = chain.get(index1);
        chain.set(index1, chain.get(index2));
        chain.set(index2, temp);
    }

    public int getChainSize() {return this.chainLength;}

    public List<Output> executeChain(String userID) {    // fetches from the userStore
        Request req = store.getFromStore(userID);
        if(req == null)     // fixed: If wrong userId passed, safe type checking
            return new ArrayList<>();
        final Map<RailType, List<Output>> map = new LinkedHashMap<>();
        fill(map);
        for(int i = 0; i < chainLength; i++) {
            ConcreteHandler handler = chain.get(i);
            // If the Strategy is user-defined then use that and skip the switch computation
            if(userDefined.contains(handler.getName())) {
                map.get(handler.getCurrentRailType()).addAll(handler.executeStrategy(req));
                continue;
            }
            Strategy strategy;
            // detail: just a safe check, but the rail type will never be null, since it will be provided by the factory
            if(null == handler.getCurrentRailType()) {
                strategy = engine.getRandomTechnique(req);
                handler.setRailType("EXPERIMENT");
            }
            else {
                // Clearly define how the strategies will be chosen
                switch (handler.getCurrentRailType()) {
    // technique: two layer strategy - domain strategy (railType based) chosen here, the finer one is chosen by Engine separately
                    case COMFORT -> {
                        if(Math.random() < 0.5) {    // detail: currently for testing only (using as random)
                            strategy = engine.getGenreTechnique(req);
                        } else {
                            strategy = engine.getDayTechnique(req);
                        }
                    }
                    case DISCOVERED -> strategy = engine.getActivityTechnique(req);
                    case EXPERIMENT -> strategy = engine.getRandomTechnique(req);
                    default -> strategy = engine.getRandomTechnique(req);
                }
            }
            handler.setCurrentStrategy(strategy);
            List<Output> output = handler.executeStrategy(req);
            map.get(handler.getCurrentRailType()).addAll(output);
        }
        return filterTheBest(map);
    }

    private List<Output> filterTheBest(Map<RailType, List<Output>> map) {
        List<Output> outputs = new ArrayList<>();
        PriorityQueue<Output> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b.getAnime().getRating(), a.getAnime().getRating()));
        for(List<Output> list : map.values()) {
            maxHeap.addAll(list);
            int k = 5;
            List<Output> temp = new ArrayList<>();
            while(!maxHeap.isEmpty() && k > 0) {
                Output output = maxHeap.poll();
                double weight = switch(output.getRailType()) {
                    case COMFORT -> 1.0;
                    case DISCOVERED -> 1.3;
                    case EXPERIMENT -> 1.5;
                };
                output.setScore(output.getRating() * weight / (k+1.0));
                temp.add(output);
                k--;
            }
            // Sort and shuffle by both rail type and score (similar to how Netflix works)
            Collections.sort(temp, (a, b) -> Double.compare(b.getScore(), a.getScore()));
            outputs.addAll(temp);
    // detail: when clearing, just reset its pointer, sometimes the clearing might expand to O(n), in which case reset of pointer is fastest
            maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b.getAnime().getRating(), a.getAnime().getRating()));
        }
        return outputs;
    }

    private void fill(Map<RailType, List<Output>> map) {
        map.put(RailType.COMFORT, new ArrayList<>());
        map.put(RailType.DISCOVERED, new ArrayList<>());
        map.put(RailType.EXPERIMENT, new ArrayList<>());
    }

    // Metric functions
    public List<String> getChainDetails() {
        return chain.stream().map(x -> x.getName() + "-" + x.getCurrentRailType()).toList();
    }

    public String getStrategyOfHandler(int index) {
        return chain.get(index).getCurrentStrategy().toString();
    }
}
