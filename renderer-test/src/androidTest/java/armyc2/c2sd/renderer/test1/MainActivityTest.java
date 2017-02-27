package armyc2.c2sd.renderer.test1;

import android.test.ActivityInstrumentationTestCase2;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	public void testActivity() {
		MainActivity activity = getActivity();
		assertNotNull(activity);
	}

	public void testDraw(){
		final MainActivity activity = getActivity();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.findViewById(R.id.btnDraw).performClick();
			}
		});

	}
}

