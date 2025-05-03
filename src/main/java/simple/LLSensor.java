package simple;

public class LLSensor {
    private boolean status = false;
    private boolean isDoorClosed = true;
    private boolean isLatched = true;
    public LLSensor() {
    }

    public boolean failure() {
        return status;
    }

    public boolean latch(){
        if(isDoorClosed)
        {
            isLatched = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean unlatch(){
        if(isDoorClosed)
        {
            isLatched = false;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isLatched(){
        return isLatched;
    }


    // Demo functions below
    public void setFailure(boolean status) {
        this.status = status;
    }

    public boolean openDoor(){
        if(isDoorClosed && !isLatched){
            isDoorClosed = false;
            return true;
        }else{
            return false;
        }
    }

    public boolean closeDoor(){
        if(!isDoorClosed){
            isDoorClosed = true;
            return true;
        }else{
            return false;
        }
    }

}
