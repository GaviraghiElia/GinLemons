package it.unimib.ginlemons.model;

public class FirebaseResponse
{
    private boolean success;
    private String message;

    public FirebaseResponse() {}

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
