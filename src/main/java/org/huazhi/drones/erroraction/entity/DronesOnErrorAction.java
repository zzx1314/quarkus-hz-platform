package org.huazhi.drones.erroraction.entity;

import org.huazhi.drones.erroraction.entity.dto.DronesOnErrorActionParam;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class DronesOnErrorAction  extends PanacheEntityBase {
    @Id
	@GeneratedValue
	private Long id;

    private String actionId;

    private String type;

    private DronesOnErrorActionParam params;

    private String after;
}
