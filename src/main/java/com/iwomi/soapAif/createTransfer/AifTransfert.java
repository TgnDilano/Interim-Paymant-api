package com.iwomi.soapAif.createTransfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AifTransfert {
    DebitAcc debitAcc;
    List<CreditAcc> creditAcc;
    String pnbr;
    String optype;
    String dateval;
}
