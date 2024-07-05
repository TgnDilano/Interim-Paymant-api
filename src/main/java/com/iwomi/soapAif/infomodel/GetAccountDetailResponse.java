package com.iwomi.soapAif.infomodel;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAccountDetailResponse {
    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Branch branch;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Currency currency;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public AccountClass accountClass;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Customer customer;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Service service;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public AccountType accountType;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public User userWhoInitiated;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Branch mergeBranch;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Currency mergeCurrency;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public AccountClass mergeClass;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Product product;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Branch branchThatDeliveredChequeBook;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Branch branchWhereTheAccountWasCreated;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Branch branchFromAccountInformationForm;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Branch lastBranchThatHeldTheAccount;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Package packageObj;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public Currency lastAccountCurrency;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String accountSubjectToInterestCalculation;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String codeForInterestLadderPrinting;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String accountStatementCode;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private boolean taxableAccount;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private boolean accountNotToBePurged;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private boolean pendingClosure;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private int directCreditCeiling;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private int thresholdForReorderingCheques;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private boolean closedAccount;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String openingDate;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String lastModificationDate;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private int modificationSheetNumber;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private int accountSubjectToDeductionAtSource;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String accountingBalance;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String valueDateBalance;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String historyBalance;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String historyDate;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String interestCalculationBalance;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String indicativeBalance;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String unavailableFundsWithoutDirectCredit;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String unavailableDirectCreditFunds;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String dailyUnavailableFundsWithoutDirectCredit;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String dailyUnavailableDirectCreditFunds;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String debitTurnovers;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String creditTurnovers;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String lastMovementDate;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String lastCreditDate;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String lastDebitDate;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String frequencyOfDebitInterestCalculation;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String transferToDebtRecoveryProcedure;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String mergeAccountNumber;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String mergeAccountSuffix;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String frequencyOfCreditInterestCalculation;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String lastMatchingPairAllocated;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String lastOverdraftLimitDate;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String beginningOfOccasionalInstalment;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String realTimeTransferCode;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String originalDateWhenAccountShowedDebitBalance;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String checkDigitDeclared;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String accountPledging;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String chequeDeliveryMethod;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String defaultChequeBookType;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String chequeAddressType;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String chequeAddressCode;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String iBANAccountKey;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String temporaryOpening;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String amountOfReservedFunds;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private boolean indicativeBalanceUpdatedByFundReservation;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String freeInputAmount2;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String accountStatementDeliveryMethod;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String freeInputField2;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String freeInputField3;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private boolean jointAccount;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String responsibleCustomer;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String availableBalance;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    public OverdraftAuthorization overdraftAuthorization;
}