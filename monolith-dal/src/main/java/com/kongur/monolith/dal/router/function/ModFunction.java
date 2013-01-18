package com.kongur.monolith.dal.router.function;

import org.apache.commons.lang.Validate;

/**
 * ȡģ
 * 
 * @author zhengwei
 *
 */
public class ModFunction implements IFunction2<Long, Long> {
    private Long modDenominator;
    
    public ModFunction(Long modDenominator)
    {
        Validate.notNull(modDenominator);
        this.modDenominator = modDenominator;
    }
    
    public Long apply(Long input) {
        Validate.notNull(input);
        
        Long result = input % this.modDenominator;
        return result;
    }

    public void setModDenominator(Long modDenominator) {
        Validate.notNull(modDenominator);
        this.modDenominator = modDenominator;
    }

    public Long getModDenominator() {
        return modDenominator;
    }

    public Long apply(Long input, int mod) {
        
        Validate.notNull(input);
        
        return input % mod;
    }

}
