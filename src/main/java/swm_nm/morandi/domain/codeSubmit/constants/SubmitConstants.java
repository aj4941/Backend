package swm_nm.morandi.domain.codeSubmit.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SubmitConstants {
    JAVA("93"),
    C_PLUSPLUS("84"),
    PYTHON("28"),
    PYPY3("73"),
    C99("0"),
    RUBY("68"),
    KOTLIN("69"),
    SWIFT("74"),
    TEXT("58"),
    C_SHARP("86"),
    NODE_JS("17"),
    GO("12"),
    D("29"),
    RUST("94"),
    C_PLUS17_CLANG("85");

    private final String languageId;


//                  <option value="93" data-mime="text/x-java" selected>Java 11</option>
//                  <option value="84" data-mime="text/x-c++src">C++17</option>
//                  <option value="28" data-mime="text/x-python">Python 3</option>
//                  <option value="73" data-mime="text/x-python">PyPy3</option>
//                  <option value="0" data-mime="text/x-csrc">C99</option>
//                  <option value="68" data-mime="text/x-ruby">Ruby</option>
//                  <option value="69" data-mime="text/x-kotlin">Kotlin (JVM)</option>
//                  <option value="74" data-mime="text/x-swift">Swift</option>
//                  <option value="58" data-mime="text/plain">Text</option>
//                  <option value="86" data-mime="text/x-csharp">C#</option>
//                  <option value="17" data-mime="text/javascript">node.js</option>
//                  <option value="12" data-mime="text/x-go">Go</option>
//                  <option value="29" data-mime="text/x-d">D</option>
//                  <option value="94" data-mime="text/x-rustsrc">Rust 2018</option>
//                  <option value="85" data-mime="text/x-c++src">C++17 (Clang)</option>
}
