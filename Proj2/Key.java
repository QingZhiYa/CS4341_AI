package com.company;

public class Key {
    private int flag1 = 0;
    private int flag2 = 0;
    private int flag3 = 0;
    private int flag4 = 0;
    private int flag5 = 0;

    public Key(int flag1, int flag2, int flag3, int flag4, int flag5) {
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.flag3 = flag3;
        this.flag4 = flag4;
        this.flag5 = flag5;

    }

    public Key(int[] array){
        flag1 = array[0];
        flag2 = array[1];
        flag3 = array[2];
        flag4 = array[3];
        flag5 = array[4];
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Key)) {
            return false;
        }

        Key otherKey = (Key) object;
        return this.flag1 == otherKey.flag1 &&
                this.flag2 == otherKey.flag2 &&
                this.flag3 == otherKey.flag3 &&
                this.flag4 == otherKey.flag4 &&
                this.flag5 == otherKey.flag5;
    }

    @Override
    public int hashCode() {
        int result = 17; // any prime number
        result = 31 * result + Integer.valueOf(this.flag1).hashCode();
        result = 31 * result + Integer.valueOf(this.flag2).hashCode();
        result = 31 * result + Integer.valueOf(this.flag3).hashCode();
        result = 31 * result + Integer.valueOf(this.flag4).hashCode();
        result = 31 * result + Integer.valueOf(this.flag5).hashCode();
        return result;
    }
}