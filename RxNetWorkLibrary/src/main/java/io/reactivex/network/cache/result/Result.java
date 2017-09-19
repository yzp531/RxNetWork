package io.reactivex.network.cache.result;

/**
 * by y on 20/09/2017.
 */

public class Result {

    private boolean isResult;
    private String message;

    public Result(boolean isResult, String message) {
        this.isResult = isResult;
        this.message = message;
    }

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
