/**
 * @author Trayan Tsonev
 * @id
 */

import java.util.concurrent.locks.*;

public class TrafficControllerFair implements TrafficController {
    private TrafficRegistrar registrar;
    private boolean empty;

    Lock lock = new ReentrantLock(true);
    Condition bridgeEmpty = lock.newCondition();

    public TrafficControllerFair(TrafficRegistrar r) {
        this.registrar = r;
        this.empty = true;
    }

    public void enterRight(Vehicle v) {
        lock.lock();
        try {
            while(!empty) {
                bridgeEmpty.await();
            }
            empty = false;
            registrar.registerRight(v);
            bridgeEmpty.signalAll();
        }
        catch(InterruptedException ie) {}
        finally { lock.unlock(); }

    }

    public void enterLeft(Vehicle v) {
        lock.lock();
        try {
            while(!empty) {
                bridgeEmpty.await();
            }
            empty = false;
            registrar.registerLeft(v);
            bridgeEmpty.signalAll();
        }
        catch(InterruptedException ie) {}
        finally { lock.unlock(); }
    }

    public void leaveLeft(Vehicle v) {
        lock.lock();
        try {
            while(empty) {
                bridgeEmpty.await();
            }
            empty = true;
            registrar.deregisterLeft(v);
            bridgeEmpty.signalAll();
        }
        catch(InterruptedException ie) {}
        finally { lock.unlock(); }
    }

    public void leaveRight(Vehicle v) {
        lock.lock();
        try {
            while(empty) {
                bridgeEmpty.await();
            }
            empty = true;
            registrar.deregisterRight(v);
            bridgeEmpty.signalAll();
        }
        catch(InterruptedException ie) {}
        finally { lock.unlock(); }
    }
}
