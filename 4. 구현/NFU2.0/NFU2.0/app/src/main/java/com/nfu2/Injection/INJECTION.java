package com.nfu2.Injection;
import java.util.Comparator;

public class INJECTION implements Comparator<INJECTION> {
    private String Drug;
    private String Dosage;
    private String Route;
    private long Duedate;
    private String Dodate;
    private String Doman;
    private String Writeman;
    private boolean Check;
    private String Message;
    private String Injectionkey;


    public INJECTION(){}

    public INJECTION(String Dodate, String Doman, boolean Check, String Message){
        this.Dodate = Dodate;
        this.Doman = Doman;
        this.Check = Check;
        this.Message = Message;
    }

    public String getDrug(){
        return Drug;
    }
    public void setDrug(String Drug){
        this.Drug=Drug;
    }
    public String getDosage(){
        return Dosage;
    }
    public void setDosage(String Dosage){
        this.Dosage=Dosage;
    }
    public String getRoute(){
        return Route;
    }
    public void setRoute(String Route){
        this.Route=Route;
    }
    public long getDuedate(){
        return Duedate;
    }
    public void setDuedate(long Duedate){
        this.Duedate=Duedate;
    }
    public String getDodate(){
        return Dodate;
    }
    public void setDodate(String Dodate){
        this.Dodate=Dodate;
    }
    public String getDoman(){
        return Doman;
    }
    public void setDoman(String Doman){
        this.Doman=Doman;
    }
    public boolean getCheck(){
        return Check;
    }
    public void setCheck(boolean Check){
        this.Check=Check;
    }
    public String getWriteman(){
        return Writeman;
    }
    public void setWriteman(String Writeman){
        this.Writeman=Writeman;
    }
    public String getInjectionkey(){return Injectionkey;}
    public void setInjectionkey(String Injectionkey){this.Injectionkey=Injectionkey;}
    public String getMessage(){return Message;}
    public void setMessage(String Message){this.Message=Message;}

    @Override
    public int compare(INJECTION o1, INJECTION o2) {
        if(o1.getDuedate()>o2.getDuedate()){
            return 1;
        }
        else if(o1.getDuedate()<o2.getDuedate()){
            return -1;
        }
        return 0;
    }
}
