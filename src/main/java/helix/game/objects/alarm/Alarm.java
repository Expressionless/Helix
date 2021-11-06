package helix.game.objects.alarm;

/**
 * Timer that triggers a callback method after a set amount of time (seconds)
 * @author Sly
 * 
 * @see {@link Event}
 */
public final class Alarm {
	/**
	 * Max number of alarms
	 */
	public static final int ALARM_COUNT = 10;
	/**
	 * Default "unset" alarm value
	 */
	public static final int NO_ALARM = -1;

	/**
	 * Event Callback to be triggered when the alarm timer is 0
	 */
	private Event action;
	/**
	 * Timer of the alarm. Decremented by 1 every tick
	 */
	private int timer = NO_ALARM;
	/**
	 * Time that alarm was initially set
	 */
	private int startTime;
	
	/**
	 * Synchronization variable
	 */
	private float ms = 0;
	
	/**
	 * Create a new blank alarm
	 */
	public Alarm() {
		this.action = null;
	}

	/**
	 * Create a new alarm with a set {@link Alarm#action} and {@link Alarm#timer}
	 * @param action - Callback method to trigger when timer reaches 0
	 * @param timer - timer (seconds)
	 */
	public Alarm(Event action, int timer) {
		this.action = action;
		this.setTimer(timer);
	}
	
	/**
	 * Update the alarm
	 * @param delta - time since last frame (in seconds)
	 */
	public void update(float delta) {
		if(ms >= 1000f) {
			if(timer > 0)
				timer--;
			if (timer == 0) {
				action.event();
				timer = NO_ALARM;
			}
			ms = 0;
		} else ms += 1000f * delta;
	}

	public void cancel() {
		this.timer = -1;
		this.action = null;
	}
	
	/**
	 * @return - return percent complete of the timer (0 < x < 1)
	 */
	public float percent() {
		if(isActive() && startTime != 0)
			return (1f - ((float)timer / (float)startTime));
		else return 0f;
	}
	
	/**
	 * Set the {@link Alarm#action} and {@link Alarm#timer} of the alarm
	 * @param action - Callback method to trigger when timer reaches 0
	 * @param timer - timer (seconds)
	 */
	public void setAlarm(int timer, Event action) {
		this.action = action;
		this.setTimer(timer);
	}
	
	/**
	 * Set the {@link Alarm#timer} of the alarm
	 * @param timer - time in seconds
	 */
	public void setTimer(int timer) {
		this.timer = timer;
		this.startTime = timer;
	}
	
	/**
	 * Get time remaining on the alarm
	 * @return - time in seconds
	 */
	public int getTimer() {
		return timer;
	}
	
	public int getStartTime() {
		return this.startTime;
	}
	
	/** <b>strictly</b> when {@link Alarm#timer} == {@link Alarm#NO_ALARM}
	 * @return - whether or not alarm is currently ticking down
	 */
	public boolean isActive() {
		return (timer != NO_ALARM);
	}
	
	public String toString() {
		return "Alarm [timer=" + this.getTimer() + 
				".init_timer=" + this.startTime +
				",event=" + this.action
				+ "]";
	}
	
}
