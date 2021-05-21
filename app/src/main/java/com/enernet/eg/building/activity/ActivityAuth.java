package com.enernet.eg.building.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.CaJsInterface;

import java.net.URISyntaxException;

public class ActivityAuth extends BaseActivity implements IaResultHandler {


    private WebView m_WebView;
    private static final String m_strUrlCert = "https://www.egservice.co.kr/CertNiceApp/index.jsp";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        m_WebView = findViewById(R.id.wbAuth);
        m_WebView.addJavascriptInterface(new CaJsInterface(this), "EgApp");

        //웹뷰의 설정을 다음과 같이 맞춰주시기 바랍니다.
        m_WebView.getSettings().setJavaScriptEnabled(true);    //필수설정(true)
        m_WebView.getSettings().setDomStorageEnabled(true);        //필수설정(true)
        m_WebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);    //필수설정(true)

        m_WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        m_WebView.getSettings().setLoadsImagesAutomatically(true);
        m_WebView.getSettings().setBuiltInZoomControls(true);
        m_WebView.getSettings().setSupportZoom(true);
        m_WebView.getSettings().setSupportMultipleWindows(true);
        m_WebView.getSettings().setLoadWithOverviewMode(true);
        m_WebView.getSettings().setUseWideViewPort(true);

        /**
         !필수사항!

         웹뷰 내 앱링크를 사용하려면 WebViewClient를 반드시 설정하여 주시기바랍니다. (하단 DemoWebViewClient 참고)
         **/
        // for market
        m_WebView.setWebViewClient(new DemoWebViewClient());
        m_WebView.loadUrl(m_strUrlCert);

    }

    public class DemoWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //웹뷰 내 표준창에서 외부앱(통신사 인증앱)을 호출하려면 intent:// URI를 별도로 처리해줘야 합니다.
            //다음 소스를 적용 해주세요.
            if (url.startsWith("intent://")) {
                Intent intent = null;
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    if (intent != null) {
                        //앱실행
                        startActivity(intent);
                    }
                } catch (URISyntaxException e) {
                    //URI 문법 오류 시 처리 구간

                } catch (ActivityNotFoundException e) {
                    String packageName = intent.getPackage();
                    if (!packageName.equals("")) {
                        // 앱이 설치되어 있지 않을 경우 구글마켓 이동
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    }
                }
                //return  값을 반드시 true로 해야 합니다.
                return true;

            } else if (url.startsWith("https://play.google.com/store/apps/details?id=") || url.startsWith("market://details?id=")) {
                //표준창 내 앱설치하기 버튼 클릭 시 PlayStore 앱으로 연결하기 위한 로직
                Uri uri = Uri.parse(url);
                String packageName = uri.getQueryParameter("id");
                if (packageName != null && !packageName.equals("")) {
                    // 구글마켓 이동
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                }
                //return  값을 반드시 true로 해야 합니다.
                return true;
            }

            //return  값을 반드시 false로 해야 합니다.
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        // Google play store will not accept
        //  intermediate certificate problem
        /*
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            handler.proceed();
            // Ignore SSL certificate errors
        }
        */
    }

    @Override
    public void onResult(CaResult Result) {

        if (Result.object == null) {
            // Toast.makeText(m_Context, StringUtil.getString(this, R.string.tv_label_network_error), Toast.LENGTH_SHORT).show();
            return;
        }


    }
}