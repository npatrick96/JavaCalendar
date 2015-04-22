package Network;

/**
 * Created by John on 4/22/15.
 */
public class NetworkManager {
    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private NetworkManager() {
    }
}
