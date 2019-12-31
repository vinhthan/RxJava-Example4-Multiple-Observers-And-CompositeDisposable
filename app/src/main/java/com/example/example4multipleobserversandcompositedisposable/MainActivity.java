package com.example.example4multipleobserversandcompositedisposable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();

        Observable<String> nameObservable = getNameObservable();

        Observer<String> tnameObserver = getTNameObserver();
        Observer<String> hNameObserver = getHNameObserver();

        //Observer dang ky Observable

        //loc ten bat dau bang chu "T"
        compositeDisposable.add(
                (Disposable) nameObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.startsWith("T");
                    }
                })
                .subscribeWith(tnameObserver)
        );

        //loc ten bat dau bang "H"
        //sau do doi chu thuong sang chu hoa (map() chuyen doi 1 item thanh 1 item khac)
        compositeDisposable.add(
                (Disposable) nameObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.startsWith("H");
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .subscribeWith(hNameObserver)
        );
    }

    private Observer<String> getTNameObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: "+s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    private Observer<String> getHNameObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: "+s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    //tao Observable phat du lieu
    private Observable<String> getNameObservable(){
        return Observable.fromArray("Hai", "Vinh", "Duc", "Ta", "Hai", "Son", "Namnb", "Phuong",
                "Nga", "Ly", "Dieu", "Tan", "Loan", "Hanh", "nambn", "Quyet", "Tuanntq", "Tuanda", "Tuanka");
    }

    //huy dang ky Observable
    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }
}
