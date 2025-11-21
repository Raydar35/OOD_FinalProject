package com.wizbiz.wizard_card_game;

public class GameController {

    private static GameController instance;

    private BattleState currentState;
    private Player player;
    private Enemy enemy;

    private Deck deck;
    private DeckIterator deckIterator;

    private GameUI ui;

    private StringBuilder actionLog = new StringBuilder();

    private GameController() {}

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    // UI must call this before startGame so UI can be updated by controller
    public void setUI(GameUI ui) {
        this.ui = ui;
    }

    public GameUI getUI() { return ui; }

    public void startGame() {
        // initialize model
        player = new Player();
        enemy = new Enemy();

        deck = new Deck();                 // shared deck
        deckIterator = deck.iterator();    // single iterator fro whole game

        // initial draws
        player.drawCards(deckIterator, 5);
        enemy.drawCards(deckIterator, 5);

        logAction("Game started.");
        logAction("Player and Enemy drew initial hands.");

        // initial state
        changeState(new PlayerTurnState());
    }

    public DeckIterator getDeckIterator() { return deckIterator; }


    //============================================
    // Methods used by UI
    //============================================
    public void castSpell(String spellName) {
        currentState.castSpell(spellName);
    }

    public void endTurn() {
        if (currentState != null) currentState.nextState();
    }

    // Getters for UI
    public Player getPlayer() { return player; }
    public Enemy getEnemy() { return enemy; }

    public void logAction(String text) {
        actionLog.append(text).append("\n");
        // update UI immediately if present
        if (ui != null) ui.updateLog(actionLog.toString());
    }

    public String getActionLog() {
        return actionLog.toString();
    }


    //============================================
    // Methods used by Actor classes
    //============================================
    public void drawForActor(Actor actor, int count) {
        if (deckIterator == null) return;
        actor.drawCards(deckIterator, count);
        logAction((actor instanceof Player ? "Player" : "Enemy") + " drew " + count + " card(s).");
        if (ui != null) ui.refreshUI();
    }

    // Attempt to play a card from an actor's hand against a target.
    // Returns true if a matching card was found and used (cast attempted).
    public boolean playCard(Actor actor, String cardName, Actor target) {
        SpellCard playedCard = null;
        for (SpellCard c : actor.getHand()) {
            if (c.getName().equals(cardName)) {
                playedCard = c;
                break;
            }
        }
        if (playedCard == null) {
            logAction((actor instanceof Player ? "Player" : "Enemy") + " attempted to play: " + cardName + " (no matching card in hand)");
            return false;
        }

        // remove from hand, log, cast, refresh UI
        actor.getHand().remove(playedCard);
        logAction((actor instanceof Player ? "Player" : "Enemy") + " played: " + playedCard.getName());
        playedCard.getSpell().cast(actor, target);
        if (ui != null) ui.refreshUI();
        return true;
    }

    public void changeState(BattleState state) {
        currentState = state;
        currentState.enter();
    }
}
