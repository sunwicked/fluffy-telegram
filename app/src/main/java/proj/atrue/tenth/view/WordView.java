package proj.atrue.tenth.view;

import java.util.HashMap;

public interface WordView {

    void loadingState();
    void tenthReady(String result);
    void everyTenthReady(String result);
    void wordWrapReady(HashMap result);
    void errorState();
}
