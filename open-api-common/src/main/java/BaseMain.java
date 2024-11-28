import com.forty.common.BaseResponse;
import com.forty.common.CodeStatus;

public class BaseMain {

    public static void main(String[] args) {
        BaseResponse<String> res = new BaseResponse<>(CodeStatus.FAIL);
        System.out.println(res);
    }
}
