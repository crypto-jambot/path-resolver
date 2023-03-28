package dev.rabbit.pojo;

public enum NodeList {
    WETH(new Node("Wrapped Ether", "WETH", "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", 18), 4),
    USDC(new Node("USD Coin", "USDC", "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6), 4),
    DAI(new Node("Dai Stablecoin", "DAI", "0x6B175474E89094C44Da98b954EedeAC495271d0F", 18), 4),
    USDT(new Node("Tether USD", "USDT", "0xdAC17F958D2ee523a2206206994597C13D831ec7", 6), 4),
    BNB(new Node("BNB", "BNB", "0xB8c77482e45F1F44dE1745F52C74426C631bDD52", 18), 4),
    HEX(new Node("HEX", "HEX", "0x2b591e99afE9f32eAA6214f7B7629768c40Eeb39", 8), 4),
    MATIC(new Node("Matic Token", "MATIC", "0x7D1AfA7B718fb893dB30A3aBc0Cfc608AaCfeBB0", 18), 4),
    BUSD(new Node("Binance USD", "BUSD", "0x4Fabb145d64652a948d72533023f6E7A623C7C53", 18), 4),
    SHIB(new Node("SHIBA INU", "SHIB", "0x95aD61b0a150d79219dCF64E1E6Cc01f0B64C4cE", 18), 4),
    WISE(new Node("Wise Token", "WISE", "0x66a0f676479Cee1d7373f3DC2e2952778BfF5bd6", 18), 4),
    FRAX(new Node("Frax", "FRAX", "0x853d955aCEf822Db058eb8505911ED77F175b99e", 18), 4),
    WBTC(new Node("Wrapped BTC", "WBTC", "0x2260FAC5E5542a773Aa44fBCfeDf7C193bc2C599", 8), 4),
    UNI(new Node("Uniswap", "UNI", "0x1f9840a85d5aF5bf1D1762F925BDADdC4201F984", 18), 4),
    ICHI(new Node("ICHI", "ICHI", "0x111111517e4929D3dcbdfa7CCe55d30d4B6BC4d6", 18), 4),
    LINK(new Node("ChainLink Token", "LINK", "0x514910771AF9Ca656af840dff83E8264EcF986CA", 18), 4),
    SUSHI(new Node("SushiToken", "SUSHI", "0x6B3595068778DD592e39A122f4f5a5cF09C90fE2", 18), 4);

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
