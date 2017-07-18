package com.palanceli.ime.androidimesample;

import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;

public class AndroidIMESampleService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView mKeyboardView;     // 对应keyboard.xml中定义的KeyboardView
    private Keyboard mKeyboard;             // 对应qwerty.xml中定义的Keyboard
    private CandidateView mCandidateView;   // 候选窗
    private StringBuilder m_composeString = new StringBuilder(); // 保存写作串
    private IMChangedReceiver mIMChangedReceiver = new IMChangedReceiver(); // 声明广播接收器

    @Override
    public View onCreateInputView() {
        // res/layout/keyboard.xml
        mKeyboardView = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        mKeyboard = new Keyboard(this, R.xml.qwerty);  // res/xml/qwerty.xml
        mKeyboardView.setKeyboard(mKeyboard);
        // 将自己设为mKeyboardView的listener,以便接收和处理键盘消息
        mKeyboardView.setOnKeyboardActionListener(this);

        // 动态注册IMChanged广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_INPUT_METHOD_CHANGED);
        registerReceiver(mIMChangedReceiver, intentFilter);

        return mKeyboardView;
    }

    @Override public View onCreateCandidatesView(){
        Log.d(this.getClass().toString(), "onCreateCandidatesView: ");
        mCandidateView = new CandidateView(this);
        return mCandidateView;
    }

    @Override public void onStartInput(EditorInfo editorInfo, boolean restarting){
        super.onStartInput(editorInfo, restarting);
        Log.d(this.getClass().toString(), "onStartInput: ");

        m_composeString.setLength(0);
        setCandidatesViewShown(false);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if(code == ' '){ // 如果收到的是空格
                    if(m_composeString.length() > 0) {  // 如果有写作串，则将首个候选提交上屏
                        ic.commitText(m_composeString, m_composeString.length());
                        m_composeString.setLength(0);
                    }else{                              // 如果没有写作串，则直接将空格上屏
                        ic.commitText(" ", 1);
                    }
                    setCandidatesViewShown(false);
                }else {          // 否则，将字符计入写作串
                    m_composeString.append(code);
                    ic.setComposingText(m_composeString, 1);
                    setCandidatesViewShown(true);
                    if(mCandidateView != null){
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(m_composeString.toString());
                        mCandidateView.setSuggestions(list);
                    }
                }
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

}