package org.huazhi.ai.aiapplication.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.system.sysuser.entity.SysUser;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用
 * </p>
 *
 * @author zzx
 * @since 2025-06-16
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class AiApplication  extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String description;

	private String type;

	private String model;

	private String aiRole;

	private String prompt;

	private Integer mixHistory;

	@Column(columnDefinition = "INT DEFAULT 0", insertable = false)
	private Integer isDeleted;

	private String mcpIds;

	private String knowledgeIds;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private Boolean isSetup;

	@ElementCollection
    @CollectionTable(
            name = "ai_application_roles",
            joinColumns = @JoinColumn(name = "application_id")
    )
    @Column(name = "role_id")
    public List<Long> roles;

	private Long createId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createId", insertable = false, updatable = false)
    private SysUser sysUser;

}
