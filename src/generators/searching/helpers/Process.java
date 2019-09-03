package generators.searching.helpers;


public class Process {

  private int      coordinator;
  private Process  successor;
  private int      number;
  private boolean  online, initiate;
  private Observer observer;

  public Process(int coordinator, Process successor, int number,
      Observer observer) {
    this.coordinator = coordinator;
    this.successor = successor;
    this.number = number;
    this.online = true;
    this.initiate = false;
    this.observer = observer;
  }

  public void crash() {
    online = false;
  }

  public int getNumber() {
    return number;
  }

  public boolean iSonline() {
    return online;
  }

  public Process getSuccessor() {
    return successor;
  }

  public void setSuccessor(Process successor) {
    this.successor = successor;
  }

  public int getCoordinator() {
    return coordinator;
  }

  public void receiveMessage(Message msg) {
    // Bearbeite nur die Nachricht wenn du online bist
    if (online) {
      // Handelt es sich um eine ELECTION-Nachricht?
      if (msg.getType().equals(Message.ELECTION)) {
        // Ist die Nachricht bereits zirkuliert?
        if (msg.containsNumber(number)) {
          observer.statusMessage(Observer.STATUS.INITIATOR_RECEIVE);
          coordinator = msg.getHighestNumber();
          msg.clear();
          msg.changeType(Message.COORDINATOR);
          msg.addNumer(coordinator);
        } else {
          msg.addNumer(number);
        }
        // Weiterleiten ...
        sendMessage(msg, successor);
      }
      // Handelt es sich um eine COORDINATOR-Nachricht?
      else if (msg.getType().equals(Message.COORDINATOR)) {
        coordinator = msg.getHighestNumber();
        // Nur wenn ich selber nicht der Initiator bin
        if (!initiate) {
          sendMessage(msg, successor);
        } else
          observer.statusMessage(Observer.STATUS.COORDINATOR_CIRCULATE);
      }
    }
  }

  public void sendMessage(Message msg, Process successor) {
    // Hier sendet er wirklich die Nachricht an seinen Nachfolger
    if (msg.getType().equals(Message.ELECTION)) {
      if (successor.iSonline())
        observer.statusMessage(Observer.STATUS.SUCCESSOR_ONLINE);
      else
        observer.statusMessage(Observer.STATUS.SUCCESSOR_OFFLINE);
    }
    if (!(msg.getType().equals(Message.COORDINATOR) && !successor.iSonline())) {
      observer.fireMessageChange(this, successor, msg);
      successor.receiveMessage(msg);
    }

    // Ist der Nachfolger offline dann sende an den Nachfolger vom
    // Nachfolger
    if (!successor.iSonline()) {
      sendMessage(msg, successor.getSuccessor());
    }
  }

  public void initiate() {
    initiate = true;
    // Initiere eine ELECTION
    Message msg = new Message(Message.ELECTION);
    // Füge dich selber hinzu
    msg.addNumer(number);
    // Sende die Nachricht an deinen Nachfolger
    sendMessage(msg, successor);
  }

}
