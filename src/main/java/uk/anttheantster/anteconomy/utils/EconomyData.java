package uk.anttheantster.anteconomy.utils;

import java.util.HashMap;
import java.util.Map;

public final class EconomyData {
    public int version = 1;
    public Map<String, PlayerEntry> players = new HashMap<>();
    public Map<String, String> names = new HashMap<>();

    public static final class PlayerEntry {
        public String name;
        public long balance;
        public PlayerEntry() {}
        public PlayerEntry(String name, long balance) {
            this.name = name;
            this.balance = balance;
        }
    }
}
