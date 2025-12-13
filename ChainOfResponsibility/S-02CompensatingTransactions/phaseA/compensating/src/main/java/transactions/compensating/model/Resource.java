package transactions.compensating.model;

import transactions.compensating.enums.ResourceRequest;

public interface Resource {
    public ResourceRequest getResourceType();
}
