package com.example.docconnect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.docconnect.Common.Common;
import com.example.docconnect.Retrofit.IMyRestaurantAPI;
import com.example.docconnect.Retrofit.RetrofitClient;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashScreen extends AppCompatActivity {
    IMyRestaurantAPI myRestaurantAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        init(); //Part 4
//
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//
//                        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                            @Override
//                            public void onSuccess(Account account) {
////                                Toast.makeText(SplashScreen.this, "Logged on", Toast.LENGTH_SHORT).show();
////                                AccountKit.logOut();
//                                dialog.show();
//                                compositeDisposable.add(myRestaurantAPI.getUser(Common.API_KEY, account.getId())
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(userModel -> {
//
//                                    if (userModel.isSuccess()) // If user is available in db
//                                    {
//                                        Common.currentUser = userModel.getResult().get(0);
//                                        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                    else // If user is not available in db
//                                    {
//                                        Intent intent = new Intent(SplashScreen.this, UpdateInfoActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                    dialog.dismiss();
//                                }, throwable -> {
//                                    dialog.dismiss();
//                                            Toast.makeText(SplashScreen.this, "GET USER API", Toast.LENGTH_SHORT).show();
//                                        }));
//
//                            }
//
//                            @Override
//                            public void onError(AccountKitError accountKitError) {
//                                Toast.makeText(SplashScreen.this, "Please sign in.", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
//                                finish();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        Toast.makeText(SplashScreen.this, "You must accept this permission to use our app", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                }).check();

        //printKeyHash();
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) // already logged on
        {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Common.IS_LOGIN,true);
            startActivity(intent);
            finish();
        }
        else
        {
//            setContentView(R.layout.activity_main);
//            ButterKnife.bind(SplashScreen.this）；
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Common.IS_LOGIN,false);
            startActivity(intent);
            finish();
        }
    }

    // Part 4
    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyRestaurantAPI.class);
    }


//    private void printKeyHash() {
//        try{
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES
//            );
//            for (Signature signature : packageInfo.signatures){
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        }catch (PackageManager.NameNotFoundException e){
//            e.printStackTrace();
//        }catch (NoSuchAlgorithmException e){
//            e.printStackTrace();
//        }
//    }
}
