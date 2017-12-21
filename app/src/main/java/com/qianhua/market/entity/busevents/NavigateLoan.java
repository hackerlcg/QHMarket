package com.qianhua.market.entity.busevents;


/**
 * EventBus event for navigating to TabLoan with param query money
 */
public class NavigateLoan {

    public int queryMoney = -1;

    public NavigateLoan(int queryMoney) {
        this.queryMoney = queryMoney;
    }

}
