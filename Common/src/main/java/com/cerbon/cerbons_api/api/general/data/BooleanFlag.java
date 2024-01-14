package com.cerbon.cerbons_api.api.general.data;

public class BooleanFlag {
    private boolean flag = false;

    public void flag() {
        flag = true;
    }

    public boolean getAndReset() {
        boolean toReturn = flag;
        if (flag)
            flag = false;
        return toReturn;
    }
}
