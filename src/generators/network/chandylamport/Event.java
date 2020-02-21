package generators.network.chandylamport;

public class Event {
	public enum EventType{
		START_SNAPSHOT("START_SNAPSHOT"), RECEIVE_MESSAGE("RECEIVE_MESSAGE"),RECEIVE_MARKER("RECEIVE_MARKER");
	
		private String eventType;
		 
		EventType(String eventType) {
	        this.eventType = eventType;
	    }
	 
	    public String getEventType() {
	        return eventType;
	    }
	}
	
	public enum Case{
		THIS_MARKER_RECEIVED("THIS_MARKER_RECEIVED"),THIS_MARKER_NOT_RECEIVED("THIS_MARKER_NOT_RECEIVED"),NO_MARKER_RECEIVED("NO_MARKER_RECEIVED");
		
		private String case_;
		 
		Case(String Case) {
	        this.case_ = Case;
	    }
	 
	    public String getCase() {
	        return case_;
	    }
	}
	
	public enum Reaction{
		START_SNAPSHOT,RECORD_MESSAGE,DONT_RECORD_MESSAGE,RECORD_STATE_AND_SEND_MARKER,STOPP_THIS_RECORD
	}
	
	private EventType eventType;
	private Case case_;
	private Reaction reaction;
	private int process_id;
	private int fromProcess_id;
	private int number_message;
	private int timeMessageWasSent;
	private int eventTime;

	public Event(EventType eventType, int process_id, int fromProcess_id, int number_message, int timeMessageWasSent, int eventTime) {
		this.eventType = eventType;
		this.process_id = process_id;
		this.fromProcess_id = fromProcess_id;
		this.number_message = number_message;
		this.timeMessageWasSent = timeMessageWasSent;
		this.eventTime = eventTime;
	}
	
	public Reaction getReaction() {
		return reaction;
	}

	public void setReaction(Reaction reaction) {
		this.reaction = reaction;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public void setCase_(Case case_) {
		this.case_ = case_;
	}

	public void setProcess_id(int process_id) {
		this.process_id = process_id;
	}

	public void setFromProcess_id(int fromProcess_id) {
		this.fromProcess_id = fromProcess_id;
	}

	public void setNumber_message(int number_message) {
		this.number_message = number_message;
	}

	public void setTimeMessageWasSent(int timeMessageWasSent) {
		this.timeMessageWasSent = timeMessageWasSent;
	}

	public void setEventTime(int eventTime) {
		this.eventTime = eventTime;
	}

	public EventType getEventType() {
		return eventType;
	}

	public Case getCase_() {
		return case_;
	}

	public int getProcess_id() {
		return process_id;
	}

	public int getFromProcess_id() {
		return fromProcess_id;
	}

	public int getNumber_message() {
		return number_message;
	}

	public int getTimeMessageWasSent() {
		return timeMessageWasSent;
	}

	public int getEventTime() {
		return eventTime;
	}
	
	@Override
	public String toString() {
		return "Event [eventType=" + eventType + ", case_=" + case_ + ", reaction=" + reaction +", process_id=" + process_id + ", fromProcess_id="
				+ fromProcess_id + ", number_message=" + number_message + ", timeMessageWasSent=" + timeMessageWasSent
				+ ", eventTime=" + eventTime + "]";
	}
	
}
