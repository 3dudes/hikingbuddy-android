package it.bz.its.angelhack.threedudes.hikingbuddy.enums;

public enum HttpCodes {
    UNKNOWN(-1),
    OK(200),
    CREATED(201),
    NOTFOUND(404),
    UNPROCESSABLE(422);

    private int v;

    private HttpCodes(int val) {
        this.v = val;
    }

    public int getNumericValue() {
        return this.v;
    }

    public static HttpCodes getFromNumericValue(int v) {
        for (HttpCodes httpCode : HttpCodes.values()) {
            if (v == httpCode.getNumericValue()) {
                return httpCode;
            }
        }

        return UNKNOWN;
    }
}
