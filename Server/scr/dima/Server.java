package dima;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

public class Server implements Runnable {

    private Thread t;
    private ServerSocket soc;
    protected static String userName;
    protected static int transactionId;
    protected static int balanceChange;
    protected static int balanceMapTwo;
    protected static int X;
    protected static int Y;
    private static SortedSet<Long> queryStatistics = new TreeSet<Long>();
    private static List<Integer> queryNumber = new ArrayList<>();
    protected static Map<String, Integer> users = new HashMap<>();
    protected static ArrayList<String> onlineUsers = new ArrayList<String>();

    private static final Logger LOG = Logger.getLogger(Server.class);

    public Server(ServerSocket soc) {
        this.soc = soc;
    }

    public Server() {
        super();
    }

    public synchronized void addQueriesNumber(int number) {
        queryNumber.add(number);
    }

    @Override
    public void run() {
        Database db = new Database();
        long tstart = System.currentTimeMillis();

        try {
            Socket incoming = soc.accept();
            InputStream sin = incoming.getInputStream();
            OutputStream sout = incoming.getOutputStream();
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String l = in.readUTF();
            LOG.info("IN " + l);
            String[] xy = l.trim().split("[,;:.!?\\s]+");
            X = Integer.valueOf(xy[0]);
            Y = Integer.valueOf(xy[1]);

            out.writeUTF("Please, enter next data: Username, Transaction id, Balance change");

            boolean exit = true;
            for (; exit; ) {
                String line = in.readUTF();
                ServiceCommands.checkQueriesNumber(out, line, queryNumber, X);
                LOG.info("IN  " + line);
                String[] test = line.trim().split("[,;:.!?\\s]+");
                if (test.length == 3) {
                    userName = test[0];
                    transactionId = Integer.valueOf(test[1]);
                    balanceChange = Integer.valueOf(test[2]);
                    onlineUsers.add(test[0]);
                    Integer balanceMap = users.get(userName);

                    if (balanceChange > 0) {
                        balanceMapTwo = balanceMap + balanceChange;
                    }
                    if (balanceChange <= 0) {
                        balanceMapTwo = balanceMap - balanceChange;
                    }
                    users.put(userName, balanceMapTwo);
                    db.updateDb(userName, users.get(userName));

                    String answer = "Server answer: Transaction id: " + transactionId + "  Error code: " + 1
                            + "  Balance version: "
                            + 1 + "  Balance change: " + balanceChange + "  Balance after change: "
                            + balanceMapTwo;
                    out.writeUTF(answer);
                    LOG.info("OUT " + answer);
                }
                addQueriesNumber(1);
                long tends = System.currentTimeMillis();
                long queryTime = tends - tstart;
                queryStatistics.add(queryTime);

                try {
                    t.sleep(Y);
                } catch (InterruptedException e) {
                    LOG.error("FAIL:", e);
                    e.printStackTrace();
                }
                ServiceCommands.checkCommand(out, line, queryStatistics, queryNumber, onlineUsers);
                if (line.equals("shutdown")) {
                    out.writeUTF("All active connections are closed");
                    LOG.info("OUT " + "All active connections are closed");
                    exit = false;
                    soc.close();
                }
            }

        } catch (IOException e) {
            LOG.error("FAIL:", e);
            e.printStackTrace();
        }
    }
}
