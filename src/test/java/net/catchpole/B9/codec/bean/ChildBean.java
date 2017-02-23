package net.catchpole.B9.codec.bean;

import java.io.Serializable;

public class ChildBean implements Serializable {
    public float f;

    public ChildBean(float f) {
        this.f = f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChildBean childBean = (ChildBean) o;

        return f == childBean.f;
    }

    @Override
    public int hashCode() {
        return (f != +0.0f ? Float.floatToIntBits(f) : 0);
    }

    @Override
    public String toString() {
        return "ChildBean{" +
                "f=" + f +
                '}';
    }
}
