package org.fetch.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

@Data
@AllArgsConstructor
public class AccountingResponse {
    JSONObject json;
}
