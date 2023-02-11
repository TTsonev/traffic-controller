/**
 * @author Trayan Tsonev
 * @id 
 */

public class TrafficControllerSimple implements TrafficController {
    private TrafficRegistrar registrar;
    private boolean empty;

    public TrafficControllerSimple(TrafficRegistrar r) {
        this.registrar = r;
        this.empty = true;
    }

    public synchronized void enterRight(Vehicle v) {
        while(!empty) {
            try { 
                wait(); 
            }
            catch(InterruptedException ie) {}
        }
        registrar.registerRight(v);
        empty = false;
        notifyAll();
    }

    public synchronized void enterLeft(Vehicle v) {
        while(!empty) {
            try {
                wait();
            }
            catch(InterruptedException ie) {}
        }
        registrar.registerLeft(v);
        empty = false;
        notifyAll();
    }

    public synchronized void leaveLeft(Vehicle v) {
        while(empty) {
            try {
                wait();
            }
            catch(InterruptedException ie) {}
        }
        registrar.deregisterLeft(v);
        empty = true;
        notifyAll();
    }

    public synchronized void leaveRight(Vehicle v) {
        while(empty) {
            try {
                wait();
            }
            catch(InterruptedException ie) {}
        }
        registrar.deregisterRight(v);
        empty = true;
        notifyAll();
    }
}
