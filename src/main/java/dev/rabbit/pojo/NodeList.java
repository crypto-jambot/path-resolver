package dev.rabbit.pojo;

public enum NodeList {
    WETH("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", 4),
    USDC("0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 4),
    USDT("0xdAC17F958D2ee523a2206206994597C13D831ec7", 4),
    DAI("0x6B175474E89094C44Da98b954EedeAC495271d0F", 4),
    WBTC("0x2260FAC5E5542a773Aa44fBCfeDf7C193bc2C599", 4),
    UNI("0x1f9840a85d5aF5bf1D1762F925BDADdC4201F984", 4),
    SOTU("0xa91c82B6933105C23298E631A0C1Be751D239162", 4),
    ICHI("0x111111517e4929D3dcbdfa7CCe55d30d4B6BC4d6", 4),
    LINK("0x514910771AF9Ca656af840dff83E8264EcF986CA", 4),
    SUSHI("0x6B3595068778DD592e39A122f4f5a5cF09C90fE2", 4);

    private final String node;
    private final int searchDepth;

    NodeList(String node, int searchDepth) {
        this.node = node;
        this.searchDepth = searchDepth;
    }

    public String getNode() {
        return node;
    }

    public int getSearchDepth() {
        return searchDepth;
    }
}
