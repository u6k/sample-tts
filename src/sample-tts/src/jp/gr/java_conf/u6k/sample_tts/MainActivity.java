
package jp.gr.java_conf.u6k.sample_tts;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private static final String TAG = "sample-tts";

    private TextToSpeech _tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button voiceButton = (Button) findViewById(R.id.VoiceButton);
        Button stopButton = (Button) findViewById(R.id.StopButton);
        Button ttsInstallCheckButton = (Button) findViewById(R.id.TTSInstallCheckButton);
        Button installTTSDataButton = (Button) findViewById(R.id.InstallTTSDataButton);

        voiceButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        ttsInstallCheckButton.setOnClickListener(this);
        installTTSDataButton.setOnClickListener(this);

        _tts = new TextToSpeech(this, this);
        _tts.setLanguage(Locale.JAPAN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        _tts.shutdown();
    }

    @Override
    public void onInit(int status) {
        Log.d(TAG, "status: " + status);
        _tts.setOnUtteranceCompletedListener(this);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.d(TAG, "onUtteranceCompleted: " + utteranceId);
        Toast.makeText(this, "onUtteranceCompleted: " + utteranceId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.VoiceButton:
                onClickVoiceButton();
                break;

            case R.id.StopButton:
                onClickStopButton();
                break;

            case R.id.TTSInstallCheckButton:
                onClickTTSInstallCheckButton();
                break;

            case R.id.InstallTTSDataButton:
                onClickInstallTTSDataButton();
                break;

            default:
                Log.e(TAG, "onClick: unknown id: " + v.getId());
        }
    }

    private void onClickVoiceButton() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UUID.randomUUID().toString());

        _tts.speak("こんにちは。", TextToSpeech.QUEUE_FLUSH, param);
    }

    private void onClickStopButton() {
        _tts.stop();
    }

    private void onClickTTSInstallCheckButton() {
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, R.id.tts_data_check);
    }

    private void onClickInstallTTSDataButton() {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.tts_data_check) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Toast.makeText(this, "resultCode: CHECK_VOICE_DATA_PASS", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "fail: resultCode: " + resultCode, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
