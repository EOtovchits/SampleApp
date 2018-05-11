package cft.sample.app.exceptions;

import lombok.Data;

@Data
public class SampleAppException extends RuntimeException {

    private String errMess;
    private Long groupId;
    private Long itemId;

    public SampleAppException(ErrCodes errCode, Long groupId, Long itemId){
        this.errMess = errCode.getMess();
        this.groupId = groupId;
        this.itemId = itemId;
    }
}
