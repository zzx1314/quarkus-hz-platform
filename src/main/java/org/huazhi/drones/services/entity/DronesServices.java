package org.huazhi.drones.services.entity;

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
public class DronesServices  extends PanacheEntityBase{
    @Id
	@GeneratedValue
	private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 参数
     */
    private String params;
}
