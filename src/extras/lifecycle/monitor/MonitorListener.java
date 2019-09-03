/**
 * 
 */
package extras.lifecycle.monitor;

import extras.lifecycle.checkpoint.Checkpoint;
import extras.lifecycle.common.AbstractListener;

/**
 * @author Mihail Mihaylov
 *
 */
public interface MonitorListener extends AbstractListener {
    
    /**
     * Invoked when a checkpoint occurs.
     */
    public void onCheckpoint(Checkpoint checkpoint);

}
