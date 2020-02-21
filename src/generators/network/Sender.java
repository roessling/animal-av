package generators.network;


public class Sender {
    private int data;
    private int currentCwnd;

    // Used to check in which phase the sender is
    public Phase phase;
    // Used for received message in each iteration
    public Message received;

    // Variables used for Modus 2
    private String userInput = "aaaaaaaaaaa3taaaaaaaaaaaaaaaaaaaaaaa";
    int i;
    int stringLength;


    // Constructor for Modus 1: Random Congestion
    public Sender(int data){
        this.data = data;
        phase = Phase.SlowStart;
        currentCwnd = 1;

        data = 10000;
    }

    // Constructor for Modus 2: User Input Congestion
    public Sender(int data, String userInput){
        this.data = data;
        this.userInput = userInput;
        stringLength = userInput.length();
        phase = Phase.SlowStart;
        currentCwnd = 1;
    }

    /*
        Return the data which the sender currently received
     */
    public int getData(){
        return this.data;
    }

    /*
        Set the senders congestion window to the current one
        @cwnd: The current congestion window
     */
    public void request(int cwnd) {
        currentCwnd = cwnd;
    }

    /*
        Method simulates the congestion of the network.
        Sets the senders received message randomly.
        The received message can be either a TIMEOUT,a TRIPLEACK or a ACK.
     */
    public void determineCongestion(){
        data = data - currentCwnd;
        int congestion = (int) this.congestionRate();

        switch (congestion){
            case 0: received = Message.TIMEOUT; break;
            case 1: received = Message.TRIPLEACK; break;
            default: received = Message.ACK; break;
        }
    }

    /*
    Calculates the congestion of the network randomly.
    Herefore the method uses the Math.random()-method
    */
    private double congestionRate(){
        double congestion;
        if (currentCwnd < 2000){
            return 5;
        }else
            congestion = (Math.random() * (10));
        //System.out.println("Congestion Nummer ist : " + congestion);
        return congestion;
    }

    //  Checks if the data was completly received.
    public boolean notReceivedCompleteData(){
        return (data > 0);
    }

    /*
        Calculates the next message the sender received based on the user input.
        If next character is a '3' then a TRIPPLEACK is receiverd, with a 't' a TIMEOUT
        and with an 'a' an ACK. When the String reached its limit, the data is set to 0
        and the transfer simulation is finished.
     */
    public void waitForResponse(char c){
        data = data - currentCwnd;

        if (i >= stringLength)
            c = 'f';
        else
            c = userInput.charAt(i);

        switch (c){
                case '3': i++; this.received = Message.TRIPLEACK; break;
                case 't': i++; this.received = Message.TIMEOUT; break;
                case 'a': i++; this.received = Message.ACK;
                case 'f': data = 0;
        }
    }

    enum Message {
        ACK,
        TRIPLEACK,
        TIMEOUT
    }

    enum Phase {
        CongestionAvoidance,
        SlowStart
    }
}
