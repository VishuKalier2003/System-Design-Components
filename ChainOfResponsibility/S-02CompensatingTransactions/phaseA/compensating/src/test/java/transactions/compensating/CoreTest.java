package transactions.compensating;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import transactions.compensating.admin.ExecutorEngine;
import transactions.compensating.data.User;
import transactions.compensating.data.input.Input;
import transactions.compensating.data.output.Output;
import transactions.compensating.database.Database;
import transactions.compensating.enums.Bank;

@SpringBootTest
class CoreTest {
    @Autowired private ExecutorEngine ee;
    @Autowired private Database db;

    @Test void test1() {
        System.out.println("----------------TEST 1--------------------");
        User user1 = User.builder().username("Vishu").amount(2000).bank(Bank.SBI).build();
        User user2 = User.builder().username("Adam").amount(1700).bank(Bank.CANARA).build();
        Input inp = Input.builder().amount(450).transferFrom(user1).transferTo(user2).build();
        ee.register(user1);
        ee.register(user2);
        try {
        db.show(user1);
        int a1 = db.getCurrentAmount(user1);
        db.show(user2);
        Output output = ee.executeChain(inp);
        output.show();
        db.show(user1);
        int a2 = db.getCurrentAmount(user1);
        db.show(user2);
        assertEquals(a1, a2+450, "Test 1 fail : Transaction is not atomic");
        } catch(Exception e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Test 1 Pass : Transaction is Atomic");
    }

    @Test void test2() {
        System.out.println("----------------TEST 2--------------------");
        User user1 = User.builder().username("Vishu").amount(2000).bank(Bank.SBI).build();
        User user2 = User.builder().username("Adam").amount(1700).bank(Bank.CANARA).build();
        Input inp = Input.builder().amount(1000).transferFrom(user1).transferTo(user2).build();
        try {
        db.show(user1);
        int a1 = db.getCurrentAmount(user1);
        db.show(user2);
        Output output = ee.executeChain(inp);
        output.show();
        db.show(user1);
        int a2 = db.getCurrentAmount(user1);
        db.show(user2);
        assertEquals(a1, a2, "Test 2 fail : Transaction is not atomic");
        } catch(Exception e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Test 2 Pass : Transaction rollback and retry mechanism successful");
    }
}
