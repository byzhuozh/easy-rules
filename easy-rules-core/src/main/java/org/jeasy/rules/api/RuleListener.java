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
package org.jeasy.rules.api;

/**
 * A listener for rules execution events.
 *
 * 规则执行时的事件监听器
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface RuleListener {

    /**
     * 规则条件判断之前的触发器
     *
     * Triggered before the evaluation of a rule.
     *
     * @param rule being evaluated
     * @param facts known before evaluating the rule
     * @return true if the rule should be evaluated, false otherwise
     */
    boolean beforeEvaluate(Rule rule, Facts facts);

    /**
     * 规则条件判断之后的触发器
     *
     * Triggered after the evaluation of a rule.
     *
     * @param rule that has been evaluated
     * @param facts known after evaluating the rule
     * @param evaluationResult true if the rule evaluated to true, false otherwise
     */
    void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult);

    /**
     * 规则执行之前的触发器
     *
     * Triggered before the execution of a rule.
     *
     * @param rule the current rule
     * @param facts known facts before executing the rule
     */
    void beforeExecute(Rule rule, Facts facts);

    /**
     * 规则执行成功之后的触发器
     *
     * Triggered after a rule has been executed successfully.
     *
     * @param rule the current rule
     * @param facts known facts after executing the rule
     */
    void onSuccess(Rule rule, Facts facts);

    /**
     * 规则执行失败之后的触发器
     *
     * Triggered after a rule has failed.
     *
     * @param rule      the current rule
     * @param facts known facts after executing the rule
     * @param exception the exception thrown when attempting to execute the rule
     */
    void onFailure(Rule rule, Facts facts, Exception exception);

}
