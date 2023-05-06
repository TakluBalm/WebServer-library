package server;

import org.junit.jupiter.api.Test;

import server.JsonBuilder;

public class JsonBuilderTest {
    @Test
    public void testAdd() {
        JsonBuilder builder = new JsonBuilder();
        builder.add("name", "Roberto");
        String[] arr = {"hi", "good"};
        builder.add("ages", arr);

        System.out.println(builder.build()); 
    }

    @Test
    public void testRemove(){

    }

    @Test
    public void testBuild() {

    }
}
