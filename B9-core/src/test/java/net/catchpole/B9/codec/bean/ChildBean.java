package net.catchpole.B9.codec.bean;

public class ChildBean {
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
    public String toString() {
        return "ChildBean{" +
                "f=" + f +
                '}';
    }
}
