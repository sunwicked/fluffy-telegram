package proj.atrue.tenth;

import android.content.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class WordPresenter {

    public static final String URL = "https://blog.truecaller.com/2018/01/22/life-as-an-android-engineer/";
    private WordView wordView;
    private Context context;
    private TenthApiService wordService;

    public WordPresenter(WordView wordView, Context context) {
        this.wordView = wordView;
        this.context = context;

        if(this.wordService == null)
        {
            this.wordService = new TenthApiService();
        }
    }


    public void  getData()
    {
        wordService.getApi(context).getUrLData(URL)
            .subscribeOn(Schedulers.io())
            .map(new Function<Response<ResponseBody>, String>() {
                @Override
                public String apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                    return new String(responseBodyResponse.body().bytes());
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        wordCounter(result);


                        everyTenth(result);


                        tenthWord(result);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void everyTenth(final String bodyString) {
        Observable<String> everyTenthObservable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return everyTenthCharacterRequest(bodyString);
            }
        });
        everyTenthObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        wordView.everyTenthReady(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void wordCounter(final String bodyString) {
        Observable<HashMap> wordCountObservable = Observable.fromCallable(new Callable<HashMap>() {
            @Override
            public HashMap call() throws Exception {
                return wordCounterRequest(bodyString);
            }
        });

        wordCountObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HashMap hashMap) {
                        wordView.wordWrapReady(hashMap);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private HashMap<String, Integer> wordCounterRequest(String bodyString) {
        String[] words = bodyString.split(" ");
        HashMap<String, Integer> wordCountMap = new HashMap<>();
        for (String word : words) {
            word = word.toLowerCase();
            if (wordCountMap.containsKey(word)) {
                wordCountMap.put(word, wordCountMap.get(word) + 1);
            } else {
                wordCountMap.put(word, 1);
            }
        }
        return wordCountMap;
        // tvWordCounter.setText(Integer.toString(wordCountMap.get("truecaller")));

    }

    private String everyTenthCharacterRequest(String bodyString) {
        StringBuilder op = new StringBuilder();
        Set<Character> unique = new HashSet<>();
        for (int i = 0; i < bodyString.length(); i += 10) {
            unique.add(bodyString.charAt(i));
        }
        for (Character character : unique) {
            op.append(character);
        }
        // tvEveryTenth.setText(op.toString());
        return op.toString();
    }



    private String tenthCharacterRequest(String bodyString) {

        return String.valueOf(bodyString.charAt(10));
        // tvTenth.setText(Character.toString(bodyString.charAt(10)));
    }

    private void tenthWord(final String body) {
        Observable<String> tenthObservable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return tenthCharacterRequest(body);
            }
        });


        tenthObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        wordView.everyTenthReady(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
