package org.opendaylight.ovsdb;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import org.opendaylight.ovsdb.lib.message.OvsdbRPC;
import org.opendaylight.ovsdb.lib.message.operations.Operation;
import org.opendaylight.ovsdb.lib.meta.ColumnSchema;
import org.opendaylight.ovsdb.lib.meta.DatabaseSchema;
import org.opendaylight.ovsdb.lib.meta.TableSchema;
import org.opendaylight.ovsdb.lib.notation.Condition;
import org.opendaylight.ovsdb.lib.notation.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import static org.opendaylight.ovsdb.OpenVswitch.Operations.op;

/**
 * @author araveendrann
 */
public class OpenVswitch {

    ExecutorService executorService;
    String schemaName;
    OvsdbRPC rpc;
    volatile DatabaseSchema schema;
    Queue<Throwable> exceptions;

    public OpenVswitch(OvsdbRPC rpc, ExecutorService executorService) {
        this.rpc = rpc;
        this.executorService = executorService;
    }

    public OpenVswitch() {
    }


    public void populateSchemaFromDevice() {
        final ListenableFuture<JsonNode> fOfSchema = rpc.get_schema(Lists.newArrayList(DatabaseSchema.OPEN_VSWITCH_SCHEMA_NAME));
        fOfSchema.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    JsonNode jsonNode = fOfSchema.get();
                    schema =  DatabaseSchema.fromJson(jsonNode);

                } catch (Exception e) {
                    exceptions.add(e);
                }
            }
        }, executorService);
    }

    public TransactBuilder transact(){
        return new TransactBuilder(this);
    }

    public void transact(List<Operation> operations) {

    }

    public boolean isReady(long timeout) {
        //todo implment timeout
        return null != schema;
    }

    public DatabaseSchema schema() {
        return schema;
    }


    public static class TransactBuilder {
        OpenVswitch ovs;
        ArrayList<Operation> operations = Lists.newArrayList();

        public TransactBuilder(OpenVswitch ovs) {
            this.ovs = ovs;
        }

        public TransactBuilder add(Operation operation) {
            operations.add(operation);
            return this;
        }

        public List<Operation> build() {
            return operations;
        }

        public void execute() {
            ovs.transact(operations);
        }
    }

    public static class Insert extends Operation {
        TableSchema schema;
        String uuid;
        private String uuidName;

        public Insert on(TableSchema schema){
            this.schema = schema;
            return this;
        }

        public Insert withId(String name) {
            this.uuidName = name;
            return this;
        }

        public Insert() {
        }

        public Insert(TableSchema schema) {
            this.schema = schema;
        }

        public Insert values(ColumnSchema columnSchema, Object value) {
            return null;
        }

    }


    public static class Update extends Operation {
        TableSchema schema;
        String uuid;
        private String uuidName;

        public Update() {
        }

        public Update(TableSchema schema) {
            this.schema = schema;
        }

        public Update on(TableSchema schema){
            this.schema = schema;
            return this;
        }

        public Update set(ColumnSchema columnSchema, Object value) {
            return this;
        }

        public WhereB where(Condition condition) {
            return new WhereB(this);
        }

    }


    public static class WhereB{

        Operation operation;
        List<Condition> conditions = Lists.newArrayList();

        public WhereB() { }  public WhereB(Operation operation) {
            this.operation = operation;
        }

        public WhereB condition(Condition condition) {
            conditions.add(condition);
            return this;
        }

        public WhereB condition(ColumnSchema column, Function function, Object value) {
            conditions.add(new Condition(column.getName(), function, value));
            return this;
        }

        public WhereB and(ColumnSchema column, Function function, Object value) {
            condition(column, function, value);
            return this;
        }

        public WhereB and(Condition blooh) {
           condition(blooh);
            return this;
        }

        public Operation operation() {
            return this.operation;
        }
    }


    public static class Operations {
        public static Operations op = new Operations();

        public Insert insert(TableSchema schema) {
            return new Insert(schema);
        }

        public Update udpate(TableSchema schema) {
            return new Update(schema);
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public OvsdbRPC getRpc() {
        return rpc;
    }

    public void setRpc(OvsdbRPC rpc) {
        this.rpc = rpc;
    }

    public DatabaseSchema getSchema() {
        return schema;
    }

    public void setSchema(DatabaseSchema schema) {
        this.schema = schema;
    }

    public Queue<Throwable> getExceptions() {
        return exceptions;
    }

    public void setExceptions(Queue<Throwable> exceptions) {
        this.exceptions = exceptions;
    }


}
