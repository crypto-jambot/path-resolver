package dev.rabbit.pojo;

public enum NodeList {
    WETH(new Node("Wrapped Ether", "WETH", "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", 18), 4),
    USDC(new Node("USD Coin", "USDC", "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6), 4),
    DAI(new Node("Dai Stablecoin", "DAI", "0x6B175474E89094C44Da98b954EedeAC495271d0F", 18), 4);

    // Stored for future use

//    USDT(new Node("Tether USD", "USDT", "0xdAC17F958D2ee523a2206206994597C13D831ec7", 6), 4),
//    WBTC(new Node("Wrapped BTC", "WBTC", "0x2260FAC5E5542a773Aa44fBCfeDf7C193bc2C599", 8), 4),
//    UNI(new Node("Uniswap", "UNI", "0x1f9840a85d5aF5bf1D1762F925BDADdC4201F984", 18), 3),
//    SOTU(new Node("Seed Of The Universe", "SOTU", "0xa91c82B6933105C23298E631A0C1Be751D239162", 18), 3),
//    ICHI(new Node("ICHI", "ICHI", "0x111111517e4929D3dcbdfa7CCe55d30d4B6BC4d6", 18), 3),
//    LINK(new Node("ChainLink Token", "LINK", "0x514910771AF9Ca656af840dff83E8264EcF986CA", 18), 3),
//    SUSHI(new Node("SushiToken", "SUSHI", "0x6B3595068778DD592e39A122f4f5a5cF09C90fE2", 18), 3);

    private final Node node;
    private final int searchDepth;

    NodeList(Node node, int searchDepth) {
        this.node = node;
        this.searchDepth = searchDepth;
    }

    public Node getNode() {
        return node;
    }

    public int getSearchDepth() {
        return searchDepth;
    }
}
