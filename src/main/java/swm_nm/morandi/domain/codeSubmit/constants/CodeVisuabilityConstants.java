package swm_nm.morandi.domain.codeSubmit.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CodeVisuabilityConstants {
    OPEN("open"),//공개
    CLOSE("close"),//비공개
    ONLY_ACCEPTED("onlyaccepted");//맞았을 때만 공개

    private final String codeVisuability;


//    class="col-md-10">
//                <div class="radio"><label><input type="radio" name="code_open" id="code_open_open"
//    value="open">공개</label></div>
//                <div class="radio"><label><input type="radio" name="code_open" id="code_open_close" value="close"
//    checked>비공개</label></div>
//                <div class="radio"><label><input type="radio" name="code_open" id="code_open_accepted"
//    value="onlyaccepted">맞았을 때만 공개</label></div>
//              </div>
//            </div>
}
