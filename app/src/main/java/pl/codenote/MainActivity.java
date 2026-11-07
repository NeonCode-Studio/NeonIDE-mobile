package pl.codenote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.widget.EditText;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private WebView web;

    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(0xFF174EA6);
        getWindow().setNavigationBarColor(0xFF000000);
        web = new WebView(this);
        web.setBackgroundColor(0xFF000000);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            AlertDialog.Builder dlg(String msg) {
                return new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert).setMessage(msg);
            }
            @Override public boolean onJsAlert(WebView v, String u, String m, final JsResult r) {
                dlg(m).setPositiveButton("OK", (d, w) -> r.confirm()).setOnCancelListener(d -> r.cancel()).show();
                return true;
            }
            @Override public boolean onJsConfirm(WebView v, String u, String m, final JsResult r) {
                dlg(m).setPositiveButton("OK", (d, w) -> r.confirm()).setNegativeButton("Anuluj", (d, w) -> r.cancel())
                    .setOnCancelListener(d -> r.cancel()).show();
                return true;
            }
            @Override public boolean onJsPrompt(WebView v, String u, String m, String def, final JsPromptResult r) {
                final EditText et = new EditText(MainActivity.this);
                et.setText(def);
                et.setSingleLine(true);
                dlg(m).setView(et).setPositiveButton("OK", (d, w) -> r.confirm(et.getText().toString()))
                    .setNegativeButton("Anuluj", (d, w) -> r.cancel()).setOnCancelListener(d -> r.cancel()).show();
                return true;
            }
        });
        web.addJavascriptInterface(new Bridge(), "Android");
        web.loadUrl("file:///android_asset/index.html");
    }

    class Bridge {
        // Kompilacja C++ przez Wandbox (wymaga internetu)
        @JavascriptInterface
        public void runCpp(final String code, final String compiler, final String stdin) {
            new Thread(() -> {
                String res;
                try {
                    JSONObject body = new JSONObject();
                    body.put("code", code);
                    body.put("stdin", stdin == null ? "" : stdin);
                    body.put("compiler", (compiler == null || compiler.isEmpty()) ? "gcc-head" : compiler);
                    HttpURLConnection c = (HttpURLConnection) new URL("https://wandbox.org/api/compile.json").openConnection();
                    c.setRequestMethod("POST");
                    c.setRequestProperty("Content-Type", "application/json");
                    c.setDoOutput(true);
                    c.setConnectTimeout(15000);
                    c.setReadTimeout(60000);
                    c.getOutputStream().write(body.toString().getBytes("UTF-8"));
                    InputStream in = c.getResponseCode() < 400 ? c.getInputStream() : c.getErrorStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) sb.append(line).append('\n');
                    res = sb.toString();
                } catch (Exception e) {
                    res = "{\"compiler_error\":\"Błąd sieci: " + String.valueOf(e.getMessage()).replace("\"", "'") + "\"}";
                }
                final String out = res;
                runOnUiThread(() -> web.evaluateJavascript("onCppResult(" + JSONObject.quote(out) + ")", null));
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        web.evaluateJavascript("handleBack()", v -> { if ("false".equals(v)) super.onBackPressed(); });
    }
}
