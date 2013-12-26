package org.unividuell.android.mediacontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Receives actions from the media button of connected headsets.
 * 
 * @author Clemens
 */
public class RemoteControlReceiver extends BroadcastReceiver {
	
    private static final String TAG = RemoteControlReceiver.class.getSimpleName();
	/** a init value. */
    private static final long INIT_VALUE = -1;
    /** time between two events to be part of the same action. */
	private static final long TIME_NEW_CMD = 2000;
	/** the string to log the events. */
	private static String log = "";
	/** time last event was received. */
    private static long lastEventTime = INIT_VALUE;
    /** time of last event of the actual event. */
    private static long lastEventTimeForActualCmd = INIT_VALUE;
    
    /*
     * MUSIC CMDs
     * http://stackoverflow.com/a/18800243
     */
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDSTOP = "stop";

	@Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            long now = SystemClock.uptimeMillis();
            
            if (filterSameUpDownEvent(event)) {
            	// same event;
            	return;
            }
//            Log.i(
//            		TAG, 
//            		KeyEvent.keyCodeToString(event.getKeyCode()) + ": " 
//            				+ KeyEvent.keyCodeToString(event.getAction()) + "<> "
//            				+ "last: " + lastCmdTime + "<> "
//            				+ "Now: " + now + " = "
//            				+ (now - lastCmdTime) + "  "
//            				+ TimeUnit.MILLISECONDS.toSeconds((now - lastCmdTime)) + "s"
//				);
            
            switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: 
			case KeyEvent.KEYCODE_HEADSETHOOK:
				if ((now - lastEventTimeForActualCmd) < TIME_NEW_CMD) {
					// event belongs to current command
					log = ".";
				} else {
					// new command
					log = "\n.";
				}
				lastEventTimeForActualCmd = lastEventTime; 
				Log.i(TAG, log);
				MonitorActivity mainActivity = MonitorActivity.getInstance();
				if (mainActivity != null) {
					// update monitor view.
					mainActivity.updateView(log);
					// send event to OS music player
//					mainActivity.intentOsMusicPlayer(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
				}
				break;

			default:
				break;
			}
            
            if (isOrderedBroadcast()) {
            	abortBroadcast();
            }
        }
    }
	
	/**
	 * checks weather an event is the same as before.
	 * checks the event time; if same: true.
	 * @param e event to check.
	 * @return true: event is the same (say button down followed by button up). 
	 */
	private boolean filterSameUpDownEvent(KeyEvent e) {
		long crtTime = e.getEventTime();
		// init
		if (lastEventTime == INIT_VALUE) {
			lastEventTime = crtTime;
			lastEventTimeForActualCmd = crtTime;
			return false;
		}
		if (lastEventTime == crtTime) {
			return true;
		}
		lastEventTime = crtTime;
		return false;
	}
}