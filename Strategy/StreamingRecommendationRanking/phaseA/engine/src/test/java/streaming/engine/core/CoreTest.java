package streaming.engine.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import streaming.engine.admin.ChainManager;
import streaming.engine.data.User;
import streaming.engine.data.input.HandlerInfo;
import streaming.engine.data.output.Output;
import streaming.engine.service.Store;

@SpringBootTest
class CoreTest {
    private final Logger log = LoggerFactory.getLogger(CoreTest.class);

    @Autowired private Store store;

    @Test void test0() {
        System.out.println("-".repeat(75));
        System.out.println(" ".repeat(25)+"Core Testing phase");
        System.out.println("-".repeat(75));
        assertNull(null);
    }

    @Test void test1() {
        User vishu = User.builder().userName("vishu").activeDays(new ArrayList<>()).watchedIndex(new ArrayList<>()).build();
        vishu.addDays(Stream.builder().add("monday").add("wednesday").add("saturday").add("sunday").build().map(x -> (String)(x)).toList());
        vishu.addWatchAnimeIndices(Stream.builder().add("anime-1").add("anime-6").add("anime-3").build().map(x -> (String)x).toList());
        store.insert(vishu.getUserName(), vishu);
        assertEquals(true, store.exist("vishu"), "Test 1 fail: Store cannot store the <userId, user> references");
        log.info("Store working properly...");
    }

    @Autowired private ChainManager manager;

    @Test void test2() {
        HandlerInfo info = HandlerInfo.builder().handlerName("headNode").handlerRailType("comfort").build();
        manager.attachRoot(info);
        // fixed: always reassign, earlier updated but not reassigned
        info = info.toBuilder().handlerName("node1").handlerRailType("comfort").build();
        manager.attachNewHandler(info);
        info = info.toBuilder().handlerName("node2").handlerRailType("discovered").build();
        manager.attachNewHandler(info);
        info = info.toBuilder().handlerName("node3").handlerRailType("experiment").build();
        manager.attachNewHandler(info);
        info = info.toBuilder().handlerName("node4").handlerRailType("discovered").build();
        manager.attachNewHandler(info);
        assertEquals(5, manager.getChainSize(), "Test 2 fail: Manager not able to create chain correctly");
        log.info("Test Chain created correctly...");
    }

    @Test void test3() {
        manager.setHandlerCustomStrategy(1, "GenreStrategy-max");
        String s1 = manager.getStrategyOfHandler(1);
        manager.setHandlerCustomStrategy(1, "GenreStrategy-last");
        String s2 = manager.getStrategyOfHandler(1);
        assertEquals(false, s1.equals(s2), "Test 3 fail: The handlers are not getting customized");
        log.info("Test for Handler Customization successful...");
    }

    @Test void test4() {
        List<Output> output1 = manager.executeChain("vishu");
        manager.resetConfiguration();
        List<Output> output2 = manager.executeChain("vishu");
        assertEquals(false, output1.equals(output2), "Test 4 fail: After updating chain strategies, produces the same result");
        log.info("Test for Chain output with varying strategies completed...");
    }
}
