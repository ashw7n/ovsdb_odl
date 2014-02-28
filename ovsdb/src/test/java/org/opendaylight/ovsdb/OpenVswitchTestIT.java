package org.opendaylight.ovsdb;

import org.junit.Test;
import org.opendaylight.ovsdb.lib.message.OvsdbRPC;
import org.opendaylight.ovsdb.lib.message.operations.Operation;
import org.opendaylight.ovsdb.lib.meta.TableSchema;
import org.opendaylight.ovsdb.plugin.OvsdbTestBase;

import java.io.IOException;
import java.util.concurrent.Executors;

import static org.opendaylight.ovsdb.OpenVswitch.Operations.op;

/**
 * @author araveendrann
 */
public class OpenVswitchTestIT extends OvsdbTestBase {

    @Test
    public void test() throws IOException, InterruptedException {
        TestObjects testConnection = getTestConnection();
        OvsdbRPC rpc = testConnection.connectionService.getConnection(testConnection.node).getRpc();

        OpenVswitch ovs = new OpenVswitch(rpc, Executors.newSingleThreadExecutor());
        ovs.populateSchemaFromDevice();

        for (int i = 0; i < 100; i++) {
           if (ovs.isReady(0)) {
              break;
           }
           Thread.sleep(1000);
        }
        TableSchema bridge = ovs.schema().table("Bridge");

        ovs.transact()
                .add(op.insert(bridge).values(bridge.column("name"), "br-int"))
                .add(op.udpate(bridge)
                        .set(bridge.column("name"), "br-blah")
                        .where(bridge.column("name").opEqual("br-int"))
                            .and(bridge.column("name").opGreaterThan(4)).operation())
               .execute();

    }

}
