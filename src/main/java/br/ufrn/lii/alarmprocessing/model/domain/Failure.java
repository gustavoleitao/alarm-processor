package br.ufrn.lii.alarmprocessing.model.domain;

/**
 * Created by Gustavo on 13/09/2016.
 */
public class Failure {

    public String name;

    public Failure(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Failure{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
