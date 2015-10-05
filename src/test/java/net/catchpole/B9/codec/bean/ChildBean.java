package net.catchpole.B9.codec.bean;

public class ChildBean {
    public int i;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChildBean childBean = (ChildBean) o;

        return i == childBean.i;
    }

    @Override
    public String toString() {
        return "ChildBean{" +
                "i=" + i +
                '}';
    }
}
