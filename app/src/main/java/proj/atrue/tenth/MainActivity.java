package proj.atrue.tenth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements WordView {

    public static final String URL = "https://blog.truecaller.com/2018/01/22/life-as-an-android-engineer/";
    @BindView(R.id.btn_start_scan)
    Button btnStartScan;
    @BindView(R.id.tv_tenth)
    TextView tvTenth;
    @BindView(R.id.tv_every_tenth)
    TextView tvEveryTenth;
    @BindView(R.id.tv_word_counter)
    TextView tvWordCounter;
    private WordPresenter wordPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        wordPresenter = new WordPresenter(this,this);
    }


    @OnClick({R.id.btn_start_scan})
    public void getData() {
        wordPresenter.getData();
    }





    @Override
    public void tenthReady(String result) {
        tvTenth.setText(result);    }

    @Override
    public void everyTenthReady(String result) {
        tvEveryTenth.setText(result);
    }

    @Override
    public void wordWrapReady(HashMap result) {
        String magicalInput = "truecaller";
        tvWordCounter.setText(Integer.toString((Integer) result.get(magicalInput)));
    }
}
