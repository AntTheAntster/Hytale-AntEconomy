package uk.anttheantster.anteconomy.utils;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class EconomyConfig {
    public static final KeyedCodec<Integer> DEFAULT_BALANCE =
            new KeyedCodec<>("DefaultBalance", Codec.INTEGER);

    public static final BuilderCodec<EconomyConfig> CODEC = BuilderCodec.<EconomyConfig>builder(EconomyConfig.class, EconomyConfig::new)
            .addField(DEFAULT_BALANCE, (o, v) -> o.defaultBalance = v, o -> o.defaultBalance)

            .build();

    private int defaultBalance = 1000;

    public int getDefaultBalance() { return defaultBalance; }
}
