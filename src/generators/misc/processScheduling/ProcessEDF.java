package generators.misc.processScheduling;

import java.util.ArrayList;

public class ProcessEDF {
	
	private String name;
	private int ankunftszeit;
	private int bedienzeit;
	private int rythmus;
	private int doneExecution;
	
	private int nextCall;
	private int needToExecuteProcesses = 0;

	private int info_RemainingTime;
	private int info_RestBedienzeit;
	
	public ProcessEDF(String name, int ankunftszeit, int bedienzeit, int rythmus){
		this.name = name;
		this.ankunftszeit = ankunftszeit;
		this.bedienzeit = bedienzeit;
		this.rythmus = rythmus;
		this.doneExecution = 0;
		
		this.nextCall = ankunftszeit;
	}

	public String getName() {
		return name;
	}
	
	public int getRemainingTimeToDeadline(int runMS){
		return nextCall-runMS;
	}
	
	public Boolean needToExecute(int runMS){
		int remTime = getRemainingTimeToDeadline(runMS);
		if(remTime==0){
			if(isFinished()){
				needToExecuteProcesses++;
				nextCall += rythmus;
			}else{
				System.out.println("ERROR1! -> New Process arived, but old not finished : "+name);
				return null;
			}
		}else if(remTime<0){
			System.out.println("ERROR2! -> New Process arived, but old not finished : "+name);
			return null;
		}
		return needToExecuteProcesses>0;
	}
	
	public boolean isFinished(){
		return needToExecuteProcesses==0;
	}
	
	
	
	public int getAnkunftszeit() {
		return ankunftszeit;
	}

	public int getBedienzeit() {
		return bedienzeit;
	}

	public int getRythmus() {
		return rythmus;
	}

	public int getDoneExecution() {
		return doneExecution;
	}

	public int getRestBedienzeit() {
		return bedienzeit-doneExecution;
	}

	public void do_one_execution() {
		if(!isFinished()){
			doneExecution++;
			if(getRestBedienzeit()==0){
				needToExecuteProcesses--;
				doneExecution = 0;
			}
		}else{
			System.out.println("ERROR!");
		}
	}
	
	public void updateInfos(int runMS){
		if(nextCall==runMS){
			needToExecuteProcesses++;
			nextCall += rythmus;
		}
	}
	
	public Integer[] getAllNewProcessTimes(int maxTime){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=ankunftszeit ; i<maxTime ; i=i+rythmus){
			list.add(i);
		}
		return list.toArray(new Integer[0]);
	}
	
	public String[] getCurrentInfos(int runMS){
		info_RemainingTime = getRemainingTimeToDeadline(runMS);
		info_RestBedienzeit = needToExecuteProcesses>0 ? getRestBedienzeit() : 0;
		
		String[] infos = new String[7];
		infos[0] = ""+name;						//Name
		infos[1] = ""+ankunftszeit;				//Ankunftszeit
		infos[2] = ""+bedienzeit;				//Bearbeitungszeit
		infos[3] = ""+rythmus;					//Rythmus
		infos[4] = ""+needToExecuteProcesses;	//needToExecuteProcesses
		if(needToExecuteProcesses<=1){
			infos[5] = ""+info_RestBedienzeit;	//RestBedienzeit
			infos[6] = ""+info_RemainingTime;	//Deadline
		}else{		//ERROR
			infos[5] = "E";
			infos[6] = "E";
		}
		return infos;
	}
	
	public ProcessEDF clone(){
		return new ProcessEDF(name, ankunftszeit, bedienzeit, rythmus);
	}
}
