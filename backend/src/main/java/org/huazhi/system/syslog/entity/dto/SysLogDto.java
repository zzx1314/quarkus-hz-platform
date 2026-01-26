package org.huazhi.system.syslog.entity.dto;

import java.util.Objects;

public class SysLogDto {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysLogDto sysLogDto = (SysLogDto) o;
        return Objects.equals(id, sysLogDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SysLogDto{" +
                "id=" + id +
                '}';
    }
}