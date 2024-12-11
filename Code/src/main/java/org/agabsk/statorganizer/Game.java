package org.agabsk.statorganizer;

import com.google.gson.JsonArray;

public class Game {
    private JsonArray[] Quarters = new JsonArray[8];

    public void setQuarters(JsonArray[] Quarters){
        this.Quarters = Quarters;
    }

    public JsonArray[] getQuarters(){
        return this.Quarters;
    }

    public void setQtr(JsonArray events, int qtrKey){
        if (qtrKey < 5){
            this.Quarters[qtrKey-1] = events;
        } else{
            this.Quarters[qtrKey-7] = events;
        }
    }
}
