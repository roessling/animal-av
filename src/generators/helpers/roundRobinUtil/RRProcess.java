package generators.helpers.roundRobinUtil;

public class RRProcess {
	
	public final int ID, startTime, duration;
	public String[] line;
	
	private int processedTime = 0;
	
	public RRProcess(int id, int startTime, int duration) {
		this.ID = id;
		this.startTime = startTime;
		this.duration = duration;
	}
	
	public boolean isFinished() {
		return this.duration == this.processedTime;
	}
	
	public void process(int time, boolean waiting) {
		if(waiting && !isFinished()) {
			this.line[time] = "-";
		} else if(!isFinished()) {
			this.processedTime++;
			this.line[time] = "x";
		} else {
			this.line[time] = "there's something wrong!";
		}
	}
	
	public void init(int maxTime) {
		this.line = new String[maxTime];
		for (int i = 0; i < line.length; i++) {
			line[i] = "  ";
		}
	}
	
}
