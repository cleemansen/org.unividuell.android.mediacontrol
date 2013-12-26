package org.unividuell.android.mediacontrol;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

/**
 * Monitors the media buttons of a connected headset.
 * 
 * @author Clemens
 */
public class MonitorActivity extends Activity {
	
	/** me myself and i. */
	private static MonitorActivity instance;
	
	/** the audio manager. */
	private AudioManager mAudioManager;
	
	/** my remote control receiver. */
	private ComponentName mRemoteControlReceiver;
	
	public static MonitorActivity getInstance() {
		return instance;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        instance = this;
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	// Start listening for button presses
    	mAudioManager = (AudioManager) this
    			.getSystemService(Context.AUDIO_SERVICE);
    	mRemoteControlReceiver = new ComponentName(
    			getPackageName(), 
    			RemoteControlReceiver.class.getName());
    	mAudioManager.registerMediaButtonEventReceiver(mRemoteControlReceiver);
    	
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	// audio manager handles duplicates; so don't worry about adding it twice.
    	mAudioManager.registerMediaButtonEventReceiver(mRemoteControlReceiver);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	// Stop listening for button presses
    	mAudioManager.unregisterMediaButtonEventReceiver(mRemoteControlReceiver);
    	instance = null;
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    /**
     * appends the text to the main text view.
     * @param textToAppend text to append.
     */
    public void updateView(String textToAppend) {
		TextView main = (TextView) findViewById(R.id.mainTextView);
		main.append(textToAppend);
    }
    

    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    
    /**
     * intents the OS music player.
     * TODO: dosn't work, fix it.
     * 
     * @param cmd command to send to the OS music player.
     */
    public void intentOsMusicPlayer(int cmd) {
//    	if(am.isMusicActive()) {
//    	    Intent i = new Intent(SERVICECMD);
//    	    i.putExtra(CMDNAME , cmd );
//    	    sendBroadcast(i);
//    	    updateView(cmd);
//    	}
    	long eventtime = SystemClock.uptimeMillis();
//    	Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//    	KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, cmd, 0);
//    	downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
//    	sendOrderedBroadcast(downIntent, null);
    	
    	Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
    	KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
    	downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
    	sendOrderedBroadcast(downIntent, null);

    	Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
    	KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
    	upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
    	sendOrderedBroadcast(upIntent, null);
    	
    	updateView(KeyEvent.keyCodeToString(cmd));
    	
    	mAudioManager.registerMediaButtonEventReceiver(mRemoteControlReceiver);
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.monitor, menu);
        return true;
    }
    
}
