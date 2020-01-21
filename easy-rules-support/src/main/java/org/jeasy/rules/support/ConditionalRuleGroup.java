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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;

/**
 * A conditional rule group is a composite rule where the rule with the highest priority acts as a condition:
 * if the rule with the highest priority evaluates to true, then we try to evaluate the rest of the rules
 * and execute the ones that evaluate to true.
 *
 * @author Dag Framstad (dagframstad@gmail.com)
 */
public class ConditionalRuleGroup extends CompositeRule {

    private Set<Rule> successfulEvaluations;
    private Rule conditionalRule;

    /**
     * Create a conditional rule group.
     */
    public ConditionalRuleGroup() {
    }

    /**
     * Create a conditional rule group.
     *
     * @param name of the conditional rule
     */
    public ConditionalRuleGroup(String name) {
        super(name);
    }

    /**
     * Create a conditional rule group.
     *
     * @param name        of the conditional rule
     * @param description of the conditional rule
     */
    public ConditionalRuleGroup(String name, String description) {
        super(name, description);
    }

    /**
     * Create a conditional rule group.
     *
     * @param name        of the conditional rule
     * @param description of the conditional rule
     * @param priority    of the composite rule
     */
    public ConditionalRuleGroup(String name, String description, int priority) {
        super(name, description, priority);
    }

    /**
     * 条件：最高优先级的规则要先通过校验
     *
     * A path rule will trigger all it's rules if the path rule's condition is true.
     * @param facts The facts.
     * @return true if the path rules condition is true.
     */
    @Override
    public boolean evaluate(Facts facts) {
        successfulEvaluations = new HashSet<>();
        //获取优先级最高的规则
        conditionalRule = getRuleWithHighestPriority();
        //只要优先级最高的规则通过校验，则再筛选符合条件的规则(不含最高优先级的规则)
        if (conditionalRule.evaluate(facts)) {
            for (Rule rule : rules) {
                //判断其余规则是否符合条件
                if (rule != conditionalRule && rule.evaluate(facts)) {
                    successfulEvaluations.add(rule);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * When a conditional rule group is applied, all rules that evaluated to true are performed
     * in their natural order, but with the conditional rule (the one with the highest priority) first.
     *
     * @param facts The facts.
     *
     * @throws Exception thrown if an exception occurs during actions performing
     */
    @Override
    public void execute(Facts facts) throws Exception {
        //执行优先级最高的规则
        conditionalRule.execute(facts);
        //对其他规则先做倒排，再执行
        for (Rule rule : sort(successfulEvaluations)) {
            rule.execute(facts);
        }
    }

    /**
     * 获取优先级最高的规则
     * @return
     */
    private Rule getRuleWithHighestPriority() {
        //排序(倒排)
        List<Rule> copy = sort(rules);
        // make sure that we only have one rule with the highest priority
        //得到优先级最高的规则
        Rule highest = copy.get(0);
        if (copy.size() > 1 && copy.get(1).getPriority() == highest.getPriority()) {
           throw new IllegalArgumentException("Only one rule can have highest priority");
        }
        return highest;
    }

    private List<Rule> sort(Set<Rule> rules) {
        return new ArrayList<>(new TreeSet<>(rules));
    }

}
