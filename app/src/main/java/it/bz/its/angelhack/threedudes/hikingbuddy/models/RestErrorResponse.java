package it.bz.its.angelhack.threedudes.hikingbuddy.models;


import java.util.List;

public class RestErrorResponse {
    List<String> errors;

    public String getJoinedErrors() {
        String retVal = "";

        if (errors != null) {
            for (int i = 0; i < errors.size(); i++) {
                retVal += errors.get(i);

                if (i != (errors.size() - 1)) {
                    retVal += ", ";
                }
            }
        }

        return retVal;
    }
}
