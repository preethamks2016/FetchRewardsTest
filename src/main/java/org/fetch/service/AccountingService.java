package org.fetch.service;

import org.fetch.dtos.requests.AccountingRequest;
import org.fetch.dtos.responses.AccountingResponse;

public interface AccountingService {
    AccountingResponse calculatePoints(AccountingRequest request);
}
