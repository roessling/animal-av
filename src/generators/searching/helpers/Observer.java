package generators.searching.helpers;


public interface Observer {

  public static enum STATUS {
    SUCCESSOR_ONLINE, SUCCESSOR_OFFLINE, INITIATOR_RECEIVE, COORDINATOR_CIRCULATE
  };

  public void fireMessageChange(Process from, Process to, Message msg);

  public void statusMessage(STATUS status);

}
