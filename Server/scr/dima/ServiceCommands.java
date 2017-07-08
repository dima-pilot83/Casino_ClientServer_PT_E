package dima;

import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class ServiceCommands {

    private static final Logger LOG = Logger.getLogger(ServiceCommands.class);

    protected static void checkQueriesNumber(DataOutputStream out, String line, List<Integer> list, int X)
            throws IOException {

        if (list.size() == X || line.equals("close connection")) {
            out.writeUTF("close connection");
            LOG.info("OUT " + "close connection");
        }
    }

    protected static void checkCommand(DataOutputStream out, String line, SortedSet<Long> queryStatistics,
            List<Integer> list, ArrayList<String> array) throws IOException {

        if (line.equals("show statistics")) {
            out.writeUTF("Statistics: Number of processed queries - :" + list.size() + "  Min query time - : "
                    + queryStatistics.first() + " Max query time - : " + queryStatistics.last()
                    + " Average query time - : " + (queryStatistics.last() - queryStatistics.first()) / 2);
            LOG.info("OUT " + "Statistics: Number of processed queries - :" + list.size() + "  Min query time - : "
                    + queryStatistics.first() + " Max query time - : " + queryStatistics.last()
                    + " Average query time - : " + (queryStatistics.last() - queryStatistics.first()) / 2);
        }

        if (line.equals("kick player")) {

        }
        if (line.equals("show clients")) {
            out.writeUTF(array.toString());
            LOG.info("OUT " + array.toString());
        }

    }

}
