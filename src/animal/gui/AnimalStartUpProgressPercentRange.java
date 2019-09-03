package animal.gui;

public class AnimalStartUpProgressPercentRange{
    	
    	private final int PERCENT_START;
    	private final int PERCENT_END;
    	
    	public AnimalStartUpProgressPercentRange(int percentStart, int percentEnd){
    		PERCENT_START = percentStart;
    		PERCENT_END = percentEnd;
    	}

		public int getPERCENT_START() {
			return PERCENT_START;
		}

		public int getPERCENT_END() {
			return PERCENT_END;
		}
		
		public int getPERCENT_FromStates(double totalStates, double currentState){
			double percentInThisRange = (double) (PERCENT_END-PERCENT_START);
			if(totalStates>0){
				double percentStates = percentInThisRange/totalStates*currentState;
				return (int)Math.round(PERCENT_START+percentStates);
			}else{
				return (int)Math.round(PERCENT_START);
			}
		}
		
		public int getPERCENT_FromStates(int totalStates, int currentState){
			return getPERCENT_FromStates((double)totalStates, (double)currentState);
		}
		
    	
}