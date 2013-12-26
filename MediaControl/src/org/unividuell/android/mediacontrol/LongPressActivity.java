package org.unividuell.android.mediacontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LongPressActivity extends Activity {
	
	private static final String TAG = LongPressActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
		
		// Get intent, action and MIME type
	    Intent intent = getIntent();
	    String action = intent.getAction();
	    String type = intent.getType();
	    
	    Log.i(TAG, intent.toString());
	    
	    TextView textView = (TextView)findViewById(R.id.mainTextView);
	    textView.append("LongPress!");
	    
	}

}
