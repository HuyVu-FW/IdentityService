package huyvu.identityservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class IdentityServiceApplicationTests {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        list.forEach(n -> System.out.println(n+"hi"));

        List<String> list2=list.stream().limit(2).toList();
    }

    @Test
    void contextLoads() {
    }

}
