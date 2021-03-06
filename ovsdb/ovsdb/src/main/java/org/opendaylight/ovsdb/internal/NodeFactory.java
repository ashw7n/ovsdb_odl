package org.opendaylight.ovsdb.internal;

import org.opendaylight.controller.sal.core.ConstructionException;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.utils.INodeFactory;

public class NodeFactory implements INodeFactory {
    void init() {
    }

    /**
     * Function called by the dependency manager when at least one dependency
     * become unsatisfied or when the component is shutting down because for
     * example bundle is being stopped.
     */
    void destroy() {
    }

    /**
     * Function called by dependency manager after "init ()" is called and after
     * the services provided by the class are registered in the service registry
     */
    void start() {
    }

    /**
     * Function called by the dependency manager before the services exported by
     * the component are unregistered, this will be followed by a "destroy ()"
     * calls
     */
    void stop() {
    }

    public Node fromString(String nodeType, String nodeId) {
        if (nodeType.equals("OVS")) try {
            return new Node("OVS", nodeId);
        } catch (ConstructionException e) {
            return null;
        }
        return null;
    }
}
