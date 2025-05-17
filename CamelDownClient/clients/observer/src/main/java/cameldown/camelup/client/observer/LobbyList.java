package cameldown.camelup.client.observer;

public class LobbyList {
    private int lobbyId;
    private String name;
    private Object gameState;
    private Object[] players;
    private int[] observerIds;
    private boolean isFull;

    public boolean getIsFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public int[] getObserverIds() {
        return observerIds;
    }

    public void setObserverIds(int[] observerIds) {
        this.observerIds = observerIds;
    }

    public Object[] getPlayers() {
        return players;
    }

    public void setPlayers(Object[] players) {
        this.players = players;
    }

    public Object getGameState() {
        return gameState;
    }

    public void setGameState(Object gameState) {
        this.gameState = gameState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }
}
