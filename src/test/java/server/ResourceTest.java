package server;

import org.junit.jupiter.api.Test;
import server.exceptions.InvalidResourceTypeException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    @Test
    public void getDataTest() throws Exception {
        Resource r = new Resource("text", "hi bruih");
        assertEquals(r.getData(), "hi bruih");
        r = new Resource("image", "src/test/resources/test-img.jpeg");
        System.out.println(r.getData());
//        assertEquals();

    }

    @Test
    public void testResourceTypeException() {
        try {
            Resource r= new Resource("yeahh","src/test/resources/test-img.jpeg");
            fail(); //remember this line, else 'may' false positive
        } catch (InvalidResourceTypeException e) {
            assertThat(e.getMessage(), is("Invalid Resource Type Exception"));
            //assert others
        }
    }

    @Test
    public void testInvalidConditionalException() {
        try {
            Resource r= new Resource("conditional","src/test/resources/test-img.jpeg");
            fail(); //remember this line, else 'may' false positive
        } catch (InvalidResourceTypeException e) {
            assertThat(e.getMessage(), is("Conditional placeholders can only have true or false values"));
            //assert others
        }
    }

}