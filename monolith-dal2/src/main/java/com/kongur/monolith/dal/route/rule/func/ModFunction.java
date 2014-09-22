package com.kongur.monolith.dal.route.rule.func;

import org.apache.commons.lang.Validate;

/**
 * È¡Ä£ÔËËã
 * 
 * @author zhengwei
 */
public class ModFunction implements Function<Long, Long> {

    private Long mod;

    public ModFunction(Long mod) {
        Validate.notNull(mod);
        this.mod = mod;
    }

    public Long apply(Long input) {
        Validate.notNull(input);

        Long result = input % this.mod;
        return result;
    }
    
    public Long apply(Long input, Long input2) {
        Validate.notNull(input);
        Validate.notNull(input2);

        Long result = input % input2;
        return result;
    }

    public void setMod(Long mod) {
        Validate.notNull(mod);
        this.mod = mod;
    }

    public Long getMod() {
        return mod;
    }

}
