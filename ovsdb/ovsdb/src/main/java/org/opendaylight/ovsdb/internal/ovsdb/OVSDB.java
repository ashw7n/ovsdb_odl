package org.opendaylight.ovsdb.internal.ovsdb;

import com.google.common.util.concurrent.ListenableFuture;
import org.opendaylight.ovsdb.database.DatabaseSchema;
import org.opendaylight.ovsdb.obj.EchoResponse;
import org.opendaylight.ovsdb.obj.MonitorRequestBuilder;
import org.opendaylight.ovsdb.obj.MonitorResponse;


public interface OVSDB {

    public ListenableFuture<DatabaseSchema> list_dbs();

    public ListenableFuture<EchoResponse> echo();

    public ListenableFuture<MonitorResponse> monitor(MonitorRequestBuilder request);

}
