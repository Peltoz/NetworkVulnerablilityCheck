package com.test.networkvulnerablilitycheck;

public class CheckActivity {

    public void routerChk() {

        RouterCheck e = new RouterCheck();
        e.chkAdminPage();
        e.chkSecProtocol();
    }

    public void iotChk() {

    }
}
