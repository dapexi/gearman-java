package org.gearman.example;

import org.apache.commons.lang3.ArrayUtils;
import org.gearman.client.GearmanFunction;
import org.gearman.client.GearmanWorkerPool;
import org.gearman.common.Job;
import org.gearman.net.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerPoolDemo {
    private static Logger LOG = LoggerFactory.getLogger(WorkerPoolDemo.class);

    static class ReverseFunction implements GearmanFunction
    {
        @Override
        public byte[] process(Job job) {
            byte[] data = job.getData();
            String function = job.getFunctionName();
            LOG.debug("Got data for function " + function);
            ArrayUtils.reverse(data);
            return data;
        }
    }

    public static void main(String... args)
    {
        try {
            byte data[] = "This is a test".getBytes();
            GearmanWorkerPool workerPool = new GearmanWorkerPool.Builder()
                                        .threads(2)
                                        .withConnection(new Connection("localhost", 4730))
                                        .build();

            workerPool.registerCallback("reverse", new ReverseFunction());

            workerPool.doWork();
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
    }
}
