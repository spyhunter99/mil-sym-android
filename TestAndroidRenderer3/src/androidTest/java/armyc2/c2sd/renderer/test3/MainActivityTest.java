package armyc2.c2sd.renderer.test3;

import android.test.ActivityInstrumentationTestCase2;

import armyc2.c2sd.renderer.test3.MainActivity;
import armyc2.c2sd.renderer.test3.R;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	public void testActivity() {
		MainActivity activity = getActivity();
		assertNotNull(activity);
	}

}

