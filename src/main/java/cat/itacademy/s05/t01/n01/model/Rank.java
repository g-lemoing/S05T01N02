package cat.itacademy.s05.t01.n01.model;

public enum Rank {_1("1", 1), _2("2", 2), _3("3", 3), _4("4", 4),
                    _5("5", 5), _6("6", 6), _7("7", 7),
                    _8("8", 8), _9("9", 9), _10("10", 10),
                    _J("J", 10), _Q("Q", 10), _K("K", 10);

    private String label;
    private int value;
    
    Rank(String label, int value) {
        this.label = label;
        this.value = value;
    }

}
