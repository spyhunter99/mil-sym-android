package armyc2.c2sd.renderer.test2;

import android.test.ActivityInstrumentationTestCase2;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	public void testActivity() {
		MainActivity activity = getActivity();
		assertNotNull(activity);
	}

}

