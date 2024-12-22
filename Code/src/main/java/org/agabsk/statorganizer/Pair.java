package org.agabsk.statorganizer;

import java.util.HashMap;
import java.util.Map;

public class Pair {
    private Map<String, Object> Obj_1, Obj_2;

    public Pair(){
        this.Obj_1 = new HashMap<>();
        this.Obj_2 = new HashMap<>();
    }

    public void setObject_1(Object obj){
        this.Obj_1.put("1", obj);
    }

    public void setObject_2(Object obj){
        this.Obj_2.put("2", obj);
    }

    public Object getObject_1(){
        return this.Obj_1.get("1");
    }

    public Object getObject_2(){
        return this.Obj_2.get("2");
    }
}
