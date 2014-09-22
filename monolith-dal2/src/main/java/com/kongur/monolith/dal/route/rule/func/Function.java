package com.kongur.monolith.dal.route.rule.func;

public interface Function<I, O> {

    O apply(I input);
}
