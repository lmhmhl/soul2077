package org.dromara.soul.common.dto;

public interface IElement {
    void accept(IVisitor visitor);
}
