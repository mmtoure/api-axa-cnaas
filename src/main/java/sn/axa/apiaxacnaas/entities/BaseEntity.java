package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(
        name = "partnerFilter",
        parameters = @ParamDef(name = "partnerId", type = Long.class)
)
public abstract class BaseEntity {
}
