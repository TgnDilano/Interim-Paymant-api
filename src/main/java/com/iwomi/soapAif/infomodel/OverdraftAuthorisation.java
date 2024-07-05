package com.iwomi.soapAif.infomodel;

import javax.xml.bind.annotation.XmlElement;

class OverdraftAuthorization {
    @XmlElement(name = "overdraftAuthNIdentifier", namespace = "http://soprabanking.com/amplitude")
    public OverdraftAuthNIdentifier overdraftAuthNIdentifier;

    @XmlElement(name = "overallCharges", namespace = "http://soprabanking.com/amplitude")
    public boolean overallCharges;

    @XmlElement(name = "reference", namespace = "http://soprabanking.com/amplitude")
    public String reference;

    @XmlElement(name = "occasionalAuthorisation", namespace = "http://soprabanking.com/amplitude")
    public boolean occasionalAuthorisation;

    @XmlElement(name = "startDateOfOverdraftLimit", namespace = "http://soprabanking.com/amplitude")
    public String startDateOfOverdraftLimit;

    @XmlElement(name = "endDateOfOverdraftLimit", namespace = "http://soprabanking.com/amplitude")
    public String endDateOfOverdraftLimit;

    @XmlElement(name = "expiryDate", namespace = "http://soprabanking.com/amplitude")
    public String expiryDate;

    @XmlElement(name = "overdraftLimitValue", namespace = "http://soprabanking.com/amplitude")
    public String overdraftLimitValue;

    @XmlElement(name = "amountOfChargesInForeignCurrency", namespace = "http://soprabanking.com/amplitude")
    public String amountOfChargesInForeignCurrency;

    @XmlElement(name = "amountOfChargesInLocalCurrency", namespace = "http://soprabanking.com/amplitude")
    public String amountOfChargesInLocalCurrency;

    @XmlElement(name = "taxRate", namespace = "http://soprabanking.com/amplitude")
    public String taxRate;

    @XmlElement(name = "taxAmountInForeignCurrency", namespace = "http://soprabanking.com/amplitude")
    public String taxAmountInForeignCurrency;

    @XmlElement(name = "taxAmountInLocalCurrency", namespace = "http://soprabanking.com/amplitude")
    public String taxAmountInLocalCurrency;

    @XmlElement(name = "operationNature", namespace = "http://soprabanking.com/amplitude")
    public OperationNature operationNature;

    @XmlElement(name = "operation", namespace = "http://soprabanking.com/amplitude")
    public Operation operation;

    @XmlElement(name = "userWhoInitiated", namespace = "http://soprabanking.com/amplitude")
    public User userWhoInitiated;

    @XmlElement(name = "creationDate", namespace = "http://soprabanking.com/amplitude")
    public String creationDate;

    @XmlElement(name = "overdraftLimitStatus", namespace = "http://soprabanking.com/amplitude")
    public String overdraftLimitStatus;

    @XmlElement(name = "overrideKey", namespace = "http://soprabanking.com/amplitude")
    public String overrideKey;

    @XmlElement(name = "overdraftLimitSituation", namespace = "http://soprabanking.com/amplitude")
    public String overdraftLimitSituation;

    @XmlElement(name = "eventNumber", namespace = "http://soprabanking.com/amplitude")
    public String eventNumber;

    @XmlElement(name = "financingCommitmentAccount", namespace = "http://soprabanking.com/amplitude")
    public FinancingCommitmentAccount financingCommitmentAccount;

    @XmlElement(name = "managementCommissionRate", namespace = "http://soprabanking.com/amplitude")
    public String managementCommissionRate;

    @XmlElement(name = "managementCommissionAmountInForeignCurrency", namespace = "http://soprabanking.com/amplitude")
    public String managementCommissionAmountInForeignCurrency;

    @XmlElement(name = "managementCommissionAmountInLocalCurrency", namespace = "http://soprabanking.com/amplitude")
    public String managementCommissionAmountInLocalCurrency;

    @XmlElement(name = "renewal", namespace = "http://soprabanking.com/amplitude")
    public boolean renewal;

    @XmlElement(name = "overdraftAuthNType", namespace = "http://soprabanking.com/amplitude")
    public OverdraftAuthNType overdraftAuthNType;

    @XmlElement(name = "period", namespace = "http://soprabanking.com/amplitude")
    public String period;

    @XmlElement(name = "offBalanceSheetCommitmentManagement", namespace = "http://soprabanking.com/amplitude")
    public String offBalanceSheetCommitmentManagement;

    @XmlElement(name = "initialCommissionAmountInForeignCurrency", namespace = "http://soprabanking.com/amplitude")
    public String initialCommissionAmountInForeignCurrency;

    @XmlElement(name = "initialCommissionAmountInLocalCurrency", namespace = "http://soprabanking.com/amplitude")
    public String initialCommissionAmountInLocalCurrency;

    @XmlElement(name = "amountOfFollowingYearsManagementCommissionInForeignCurrency", namespace = "http://soprabanking.com/amplitude")
    public String amountOfFollowingYearsManagementCommissionInForeignCurrency;

    @XmlElement(name = "amountOfFollowingYearsManagementCommissionInLocalCurrency", namespace = "http://soprabanking.com/amplitude")
    public String amountOfFollowingYearsManagementCommissionInLocalCurrency;

    @XmlElement(name = "rateOfFollowingYearsManagementCommission", namespace = "http://soprabanking.com/amplitude")
    public String rateOfFollowingYearsManagementCommission;

    @XmlElement(name = "terminationCommissionAmountInForeignCurrency", namespace = "http://soprabanking.com/amplitude")
    public String terminationCommissionAmountInForeignCurrency;

    @XmlElement(name = "terminationCommissionAmountInLocalCurrency", namespace = "http://soprabanking.com/amplitude")
    public String terminationCommissionAmountInLocalCurrency;

    @XmlElement(name = "resiliationCommissionRate", namespace = "http://soprabanking.com/amplitude")
    public String resiliationCommissionRate;

    @XmlElement(name = "negotiationCommissionAmountInForeignCurrency", namespace = "http://soprabanking.com/amplitude")
    public String negotiationCommissionAmountInForeignCurrency;

    @XmlElement(name = "negotiationCommissionAmountInLocalCurrency", namespace = "http://soprabanking.com/amplitude")
    public String negotiationCommissionAmountInLocalCurrency;

    @XmlElement(name = "negotiationCommissionRate", namespace = "http://soprabanking.com/amplitude")
    public String negotiationCommissionRate;

    @XmlElement(name = "expenseAccount", namespace = "http://soprabanking.com/amplitude")
    public ExpenseAccount expenseAccount;
}

class OverdraftAuthNIdentifier {
    @XmlElement(name = "branch", namespace = "http://soprabanking.com/amplitude")
    public String branch;

    @XmlElement(name = "currency", namespace = "http://soprabanking.com/amplitude")
    public String currency;

    @XmlElement(name = "accountNumber", namespace = "http://soprabanking.com/amplitude")
    public String accountNumber;

    @XmlElement(name = "accountSuffix", namespace = "http://soprabanking.com/amplitude")
    public String accountSuffix;

    @XmlElement(name = "authorizationNumber", namespace = "http://soprabanking.com/amplitude")
    public String authorizationNumber;

    @XmlElement(name = "orderNumber", namespace = "http://soprabanking.com/amplitude")
    public String orderNumber;
}

class OperationNature {
    @XmlElement(name = "operationNatureCode" , namespace = "http://soprabanking.com/amplitude")
    public String operationNatureCode;

    @XmlElement(name = "designation" , namespace = "http://soprabanking.com/amplitude")
    public String designation;
}

class Operation {
    @XmlElement(name = "code" , namespace = "http://soprabanking.com/amplitude")
    public String code;

    @XmlElement(name = "designation" , namespace = "http://soprabanking.com/amplitude")
    public String designation;
}

class FinancingCommitmentAccount {
    @XmlElement(name = "branch", namespace = "http://soprabanking.com/amplitude")
    public Branch branch;

    @XmlElement(name = "currency", namespace = "http://soprabanking.com/amplitude")
    public Currency currency;

    @XmlElement(name = "account", namespace = "http://soprabanking.com/amplitude")
    public String account;

    @XmlElement(name = "suffix", namespace = "http://soprabanking.com/amplitude")
    public String suffix;
}

class OverdraftAuthNType {
    @XmlElement(name = "code", namespace = "http://soprabanking.com/amplitude")
    public String code;

    @XmlElement(name = "designation", namespace = "http://soprabanking.com/amplitude")
    public String designation;
}

class ExpenseAccount {
    @XmlElement(name = "branch", namespace = "http://soprabanking.com/amplitude")
    public Branch branch;

    @XmlElement(name = "currency", namespace = "http://soprabanking.com/amplitude")
    public Currency currency;

    @XmlElement(name = "account", namespace = "http://soprabanking.com/amplitude")
    public String account;

    @XmlElement(name = "suffix", namespace = "http://soprabanking.com/amplitude")
    public String suffix;
}
