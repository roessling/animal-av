/* This file was generated by SableCC (http://www.sablecc.org/). */

package extras.lifecycle.script.generated.node;

import extras.lifecycle.script.generated.node.ASingleExpression;
import extras.lifecycle.script.generated.node.Node;
import extras.lifecycle.script.generated.node.PExpression;
import extras.lifecycle.script.generated.node.PValueExpression;
import extras.lifecycle.script.generated.node.Switch;
import extras.lifecycle.script.generated.analysis.*;

@SuppressWarnings("nls")
public final class ASingleExpression extends PExpression
{
    private PValueExpression _value_;

    public ASingleExpression()
    {
        // Constructor
    }

    public ASingleExpression(
        @SuppressWarnings("hiding") PValueExpression _value_)
    {
        // Constructor
        setValue(_value_);

    }

    @Override
    public Object clone()
    {
        return new ASingleExpression(
            cloneNode(this._value_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleExpression(this);
    }

    public PValueExpression getValue()
    {
        return this._value_;
    }

    public void setValue(PValueExpression node)
    {
        if(this._value_ != null)
        {
            this._value_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._value_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._value_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._value_ == child)
        {
            this._value_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._value_ == oldChild)
        {
            setValue((PValueExpression) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}