package org.dromara.soul.common.dto;
//
//import org.dromara.soul.common.dto.RuleData;
//import org.dromara.soul.common.dto.SelectorData;

public interface IVisitor {
    void visit(SelectorData IElement);
    void visit(RuleData IElement);
}
