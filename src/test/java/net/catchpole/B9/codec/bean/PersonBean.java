package net.catchpole.B9.codec.bean;

public class PersonBean {
    private String name;
    private Integer age;
    private Integer cats;
    private boolean alive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Integer getCats() {
        return cats;
    }

    public void setCats(Integer cats) {
        this.cats = cats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonBean that = (PersonBean) o;

        if (alive != that.alive) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        return !(cats != null ? !cats.equals(that.cats) : that.cats != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (cats != null ? cats.hashCode() : 0);
        result = 31 * result + (alive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", cats=" + cats +
                ", alive=" + alive +
                '}';
    }
}
