package org.hansung.roadbuddy.generic;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class GenericPostRequestDTO extends GenericRequestDTO {
    public abstract String getApiKeyDisplayName();
}
