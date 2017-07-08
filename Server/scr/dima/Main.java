package dima;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {

        Server.users.put("Dima2", 100);
        Server.users.put("Mike2", 250);
        Database db = new Database();

        Set<Map.Entry<String, Integer>> hms = Server.users.entrySet();
        for (Map.Entry<String, Integer> hmse : hms) {
            db.addToDb(hmse.getKey(), 1, hmse.getValue());
        }
        db.readDb();

        for (int i = 0; i <= 100; i++) {
            Thread thr = new Thread(new Server(new ServerSocket(20001 + i)), "" + i + "thr");
            thr.start();
        }
    }
}
