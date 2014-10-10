package ro.mihaidumitrescu.application;

public enum ApplicationModes {
    PROD,
    TEST;

    public final static String configurationParameterName = "applicationMode";
}
