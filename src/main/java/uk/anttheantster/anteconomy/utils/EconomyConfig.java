package uk.anttheantster.anteconomy.utils;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class EconomyConfig {
    // Economy
    public static final KeyedCodec<Integer> DEFAULT_BALANCE =
            new KeyedCodec<>("DefaultBalance", Codec.INTEGER);

    // Database
    public static final KeyedCodec<String> DB_HOST =
            new KeyedCodec<>("DBHost", Codec.STRING);
    public static final KeyedCodec<Integer> DB_PORT =
            new KeyedCodec<>("DBPort", Codec.INTEGER);
    public static final KeyedCodec<String> DB_NAME =
            new KeyedCodec<>("DBName", Codec.STRING);
    public static final KeyedCodec<String> DB_USER =
            new KeyedCodec<>("DBUser", Codec.STRING);
    public static final KeyedCodec<String> DB_PASSWORD =
            new KeyedCodec<>("DBPassword", Codec.STRING);

    // Allow disabling DB (useful for testing)
    public static final KeyedCodec<Boolean> DB_ENABLED =
            new KeyedCodec<>("DBEnabled", Codec.BOOLEAN);

    public static final BuilderCodec<EconomyConfig> CODEC = BuilderCodec.<EconomyConfig>builder(EconomyConfig.class, EconomyConfig::new)
            .addField(DEFAULT_BALANCE, (o, v) -> o.defaultBalance = v, o -> o.defaultBalance)

            .addField(DB_ENABLED, (o, v) -> o.dbEnabled = v, o -> o.dbEnabled)
            .addField(DB_HOST, (o, v) -> o.dbHost = v, o -> o.dbHost)
            .addField(DB_PORT, (o, v) -> o.dbPort = v, o -> o.dbPort)
            .addField(DB_NAME, (o, v) -> o.dbName = v, o -> o.dbName)
            .addField(DB_USER, (o, v) -> o.dbUser = v, o -> o.dbUser)
            .addField(DB_PASSWORD, (o, v) -> o.dbPassword = v, o -> o.dbPassword)

            .build();

    // Defaults
    private int defaultBalance = 1000;

    private boolean dbEnabled = true;
    private String dbHost = "127.0.0.1";
    private int dbPort = 3306;
    private String dbName = "anteconomy";
    private String dbUser = "root";
    private String dbPassword = "password";

    public int getDefaultBalance() { return defaultBalance; }

    public boolean isDbEnabled() { return dbEnabled; }
    public String getDbHost() { return dbHost; }
    public int getDbPort() { return dbPort; }
    public String getDbName() { return dbName; }
    public String getDbUser() { return dbUser; }
    public String getDbPassword() { return dbPassword; }
}
