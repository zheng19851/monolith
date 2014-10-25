package com.runssnail.monolith.dal.router.function;

public interface IFunction2<I,O> {
    O apply(I input);
    
    O apply(I input, int mod);
}
