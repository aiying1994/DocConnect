package com.example.docconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.docconnect.Common.Common;
import com.example.docconnect.Retrofit.IMyRestaurantAPI;
import com.example.docconnect.Retrofit.RetrofitClient;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    IMyRestaurantAPI myRestaurantAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;

//    private static final int APP_REQUEST_CODE = 1234;
    @BindView(R.id.btn_sign_in)
    Button btn_sign_in;

    @OnClick(R.id.btn_sign_in)
    void loginUser(){

        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                builder.build());
        startActivityForResult(intent, Common.APP_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.APP_REQUEST_CODE){
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(loginResult.getError() != null)
            {
                Toast.makeText(this,""+loginResult.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            }
            else if (loginResult.wasCancelled())
            {
                Toast.makeText(this,"Login cancelled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(Common.IS_LOGIN,true);
                startActivity(intent);
                finish();

//                // Login Success
//                dialog.show();
//
//                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                    @Override
//                    public void onSuccess(Account account) {
//                        compositeDisposable.add(myRestaurantAPI.getUser(Common.API_KEY,
//                                account.getId())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(userModel -> {
//                            if (userModel.isSuccess()) // If user is available in db
//                            {
//                                Common.currentUser = userModel.getResult().get(0);
//                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                            else // If user is not available in db
//                            {
//                                Intent intent = new Intent(MainActivity.this, UpdateInfoActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                            dialog.dismiss();
//                        },throwable -> {
//                            dialog.dismiss();
//                            Toast.makeText(MainActivity.this, "GET USER"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                        }));
//                    }
//
//                    @Override
//                    public void onError(AccountKitError accountKitError) {
//                        Toast.makeText(MainActivity.this, "ACCOUNT KIT ERROR"+accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                // Part 18: Get Token
//                FirebaseInstanceId.getInstance()
//                        .getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                if(task.isSuccessful())
//                                {
//                                    Common.updateToken(getBaseContext() ,task.getResult().getToken());
//
//                                    Log.d("MyToken", task.getResult().getToken());
//
//                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                    intent.putExtra(Common.IS_LOGIN,true);
//                                    startActivity(intent);
//                                    finish();
//                                }
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MainActivity.this, "??? "+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                intent.putExtra(Common.IS_LOGIN,true);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();
        
        initView();
        
    }

    private void initView() {


    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyRestaurantAPI.class);
    }
}
