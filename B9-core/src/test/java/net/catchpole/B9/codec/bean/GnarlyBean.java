package net.catchpole.B9.codec.bean;

public class GnarlyBean {
    public ChildBean maya;
    public ChildBean lucinda;
    public boolean b1;
    public Boolean b2;
    public byte by1;
    public Byte by2;
    public char c1;
    public Character c2;
    public short s1;
    public Short s2;
    public int i1;
    public Integer i2;
    public long l1;
    public Long l2;
    public float f1;
    public Float f2;
    public double d1;
    public Double d2;
    public String string;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GnarlyBean that = (GnarlyBean) o;

        if (b1 != that.b1) return false;
        if (by1 != that.by1) return false;
        if (c1 != that.c1) return false;
        if (s1 != that.s1) return false;
        if (i1 != that.i1) return false;
        if (l1 != that.l1) return false;
        if (Float.compare(that.f1, f1) != 0) return false;
        if (Double.compare(that.d1, d1) != 0) return false;
        if (maya != null ? !maya.equals(that.maya) : that.maya != null) return false;
        if (lucinda != null ? !lucinda.equals(that.lucinda) : that.lucinda != null) return false;
        if (b2 != null ? !b2.equals(that.b2) : that.b2 != null) return false;
        if (by2 != null ? !by2.equals(that.by2) : that.by2 != null) return false;
        if (c2 != null ? !c2.equals(that.c2) : that.c2 != null) return false;
        if (s2 != null ? !s2.equals(that.s2) : that.s2 != null) return false;
        if (i2 != null ? !i2.equals(that.i2) : that.i2 != null) return false;
        if (l2 != null ? !l2.equals(that.l2) : that.l2 != null) return false;
        if (f2 != null ? !f2.equals(that.f2) : that.f2 != null) return false;
        if (d2 != null ? !d2.equals(that.d2) : that.d2 != null) return false;
        return !(string != null ? !string.equals(that.string) : that.string != null);

    }

    @Override
    public String toString() {
        return "GnarlyBean{" +
                "maya=" + maya +
                ", lucinda=" + lucinda +
                ", b1=" + b1 +
                ", b2=" + b2 +
                ", by1=" + by1 +
                ", by2=" + by2 +
                ", c1=" + (int)c1 +
                ", c2=" + (c2 == null ? null : (int)c2) +
                ", s1=" + s1 +
                ", s2=" + s2 +
                ", i1=" + i1 +
                ", i2=" + i2 +
                ", l1=" + l1 +
                ", l2=" + l2 +
                ", f1=" + f1 +
                ", f2=" + f2 +
                ", d1=" + d1 +
                ", d2=" + d2 +
                ", string='" + string + '\'' +
                '}';
    }
}
