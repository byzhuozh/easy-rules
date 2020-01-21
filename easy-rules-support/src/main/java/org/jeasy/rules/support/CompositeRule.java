/**
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.rules.support;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;
import org.jeasy.rules.core.RuleProxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 复合规则
 *
 * Base class representing a composite rule composed of a set of rules.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class CompositeRule extends BasicRule {

    /**
     * 复合规则列表(代理规则列表)
     * The set of composing rules.
     */
    protected Set<Rule> rules;

    /**
     * 代理规则
     * key：rule,  value: proxyRule
     * key：规则，  value: 代理规则
     */
    private Map<Object, Rule> proxyRules;

    /**
     * Create a new {@link CompositeRule}.
     */
    public CompositeRule() {
        this(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    /**
     * Create a new {@link CompositeRule}.
     *
     * @param name rule name
     */
    public CompositeRule(final String name) {
        this(name, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    /**
     * Create a new {@link CompositeRule}.
     *
     * @param name rule name
     * @param description rule description
     */
    public CompositeRule(final String name, final String description) {
        this(name, description, Rule.DEFAULT_PRIORITY);
    }

    /**
     * Create a new {@link CompositeRule}.
     *
     * @param name rule name
     * @param description rule description
     * @param priority rule priority
     */
    public CompositeRule(final String name, final String description, final int priority) {
        super(name, description, priority);
        rules = new TreeSet<>();
        proxyRules = new HashMap<>();
    }

    @Override
    public abstract boolean evaluate(Facts facts);

    @Override
    public abstract void execute(Facts facts) throws Exception;

    /**
     * 添加规则
     * Add a rule to the composite rule.
     * @param rule the rule to add
     */
    public void addRule(final Object rule) {
        //生成规则代理类
        Rule proxy = RuleProxy.asRule(rule);
        rules.add(proxy);
        proxyRules.put(rule, proxy);
    }

    /**
     * 移除规则
     * Remove a rule from the composite rule.
     * @param rule the rule to remove
     */
    public void removeRule(final Object rule) {
        Rule proxy = proxyRules.get(rule);
        if (proxy != null) {
            rules.remove(proxy);
        }
    }

}
