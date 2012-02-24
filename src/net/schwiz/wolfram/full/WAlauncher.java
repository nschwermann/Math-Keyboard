package net.schwiz.wolfram.full;


import java.util.List;

import net.schwiz.wolfram.full.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


public class WAlauncher extends Activity implements OnClickListener {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    MyEditView myEditView ;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        findViewById(R.id.equalButtonID).setOnClickListener(this);
        findViewById(R.id.micButtonID).setOnClickListener(this);
        myEditView = (MyEditView)findViewById(R.id.inputID);
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
    			//Fill the list view with the strings the recognizer thought it could have heard
    			List<String> matches =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    			            if(matches != null && matches.size() > 0) {
    			            	String voiceTotext = matches.get(0);
    			            	//do something with this text
    			            	myEditView.setText(voiceTotext);
    			     }
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.equalButtonID:
			launchWebsite();
			break;
		case R.id.micButtonID:
			sayIt();
			break;
		}
	}
	
		
	public void launchWebsite() {
		String input = myEditView.getText().toString();
		input = Uri.encode(input);
		Uri newUri = Uri.parse("http://www.wolframalpha.com/input/?i="+input+"&t=ff3tb01");
		try{
			startActivity(new Intent(Intent.ACTION_VIEW, newUri ));
		}
		catch(ActivityNotFoundException e){
			Log.d("Invalid URI - Woflram Launcher", e.toString());
		}
		finish();
	}

	public static class MyEditView extends EditText {
		//ref to the nesting the view
		WAlauncher launcher;
		
		public MyEditView(Context context, AttributeSet attrs) {
			super(context,attrs);
			this.launcher = (WAlauncher)context;
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {	
			switch(keyCode){
			case KeyEvent.KEYCODE_ENTER:
				launcher.launchWebsite();
				break;
			}
			return super.onKeyDown(keyCode, event);
		}
		
		@Override
		public Editable getText() {
			// TODO Auto-generated method stub
			return super.getText();
		}		
	}

    protected void sayIt() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        		RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say what (e.g. pizza)");
        Toast t = Toast.makeText(this, "Preparing to launch voice recognition one moment...", Toast.LENGTH_LONG);
        t.show();
        try{
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);}
        catch(ActivityNotFoundException e){
        	Log.e("Wolfram - Voice Recognition Error",e.toString());
        	t = Toast.makeText(this, "Voice recognition application not detected on this device", Toast.LENGTH_SHORT);
        	t.show();
        }
    }

}