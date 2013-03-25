import junit.framework.TestCase;


public class FailTest extends TestCase {

	public FailTest(String name) {
		super(name);
	}

	public void testApp() {
        assertTrue(false);
    }
	
}