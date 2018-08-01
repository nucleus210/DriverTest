package com.example.android.drivertest;

@SuppressWarnings("WeakerAccess")
public class QuizQuestions {

    //private variables
    private int _ID;
    private byte[] _image;
    private String _question;
    private String _answer;
    private String _answerSec;
    private String _choiceA;
    private String _choiceB;
    private String _choiceC;
    private String _choiceD;

    // Empty constructor
    public QuizQuestions() {
    }

    /**
     * Add constructor for this class and give arguments for constructor:
     */

    public QuizQuestions(byte[] image,
                         String question,
                         String answer,
                         String answerSec,
                         String choiceA,
                         String choiceB,
                         String choiceC,
                         String choiceD) {

        this.set_image(image);
        this.set_question(question);
        this.set_answer(answer);
        this.set_answerSec(answerSec);
        this.set_choiceA(choiceA);
        this.set_choiceB(choiceB);
        this.set_choiceC(choiceC);
        this.set_choiceD(choiceD);

    }

    // get and set methods

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String get_question() {
        return _question;
    }

    public void set_question(String _question) {
        this._question = _question;
    }

    public String get_answer() {
        return _answer;
    }

    public void set_answer(String _answer) {
        this._answer = _answer;
    }

    public String get_answerSec() {
        return _answerSec;
    }

    public void set_answerSec(String _answerSec) {
        this._answerSec = _answerSec;
    }

    public byte[] get_image() {
        return _image;
    }

    public void set_image(byte[] b) {
        this._image = b;
    }

    public String get_choiceA() {
        return _choiceA;
    }

    public void set_choiceA(String _choiceA) {
        this._choiceA = _choiceA;
    }

    public String get_choiceB() {
        return _choiceB;
    }

    public void set_choiceB(String _choiceB) {
        this._choiceB = _choiceB;
    }

    public String get_choiceC() {
        return _choiceC;
    }

    public void set_choiceC(String _choiceC) {
        this._choiceC = _choiceC;
    }

    public String get_choiceD() {
        return _choiceD;
    }

    public void set_choiceD(String _choiceD) {
        this._choiceD = _choiceD;
    }
}