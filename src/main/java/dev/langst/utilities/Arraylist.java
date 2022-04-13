package dev.langst.utilities;

import java.util.Arrays;

public class Arraylist<T> implements List<T>{

    private Object[] elements;
    private int index;

    public Arraylist(){
        this.elements = new Object[10];
        this.index = 0;
    }

    @Override
    public void add(Object element) {

        if(index >= elements.length){
            this.elements = Arrays.copyOf(this.elements, this.elements.length * 2);
        }

        this.elements[index] = element;
        this.index++;
    }

    @Override
    public T get(int index) {
        return (T)this.elements[index];
    }

    @Override
    public int size() {
        return this.index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < this.index; i++){
            sb.append(this.elements[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
