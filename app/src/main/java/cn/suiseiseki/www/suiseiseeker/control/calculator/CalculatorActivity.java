package cn.suiseiseki.www.suiseiseeker.control.calculator;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import cn.suiseiseki.www.suiseiseeker.R;
import cn.suiseiseki.www.suiseiseeker.control.FontHelper;

/**
 * Created by Shuikeyi on 2016/7/12.
 * address:shuikeyi92@gmail.com
 */
public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    private Set<Character> charset = new HashSet<Character>(),charset2 = new HashSet<>();
    MaterialEditText inputText;
    TextView suffixTextView,resultView;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        View v = findViewById(R.id.calculator_lineaylayout);
        setContentView(R.layout.calculator_layout);
        toolbar = (Toolbar) findViewById(R.id.calculator_toolbar);
        toolbar.setTitle(getString(R.string.calculator));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputText =(MaterialEditText)findViewById(R.id.calculator_inputtext);
        suffixTextView =(TextView)findViewById(R.id.calculator_textshow);
        Button calButton = (Button) findViewById(R.id.calculator_cal);
        calButton.setOnClickListener(this);
        Button clearButton = (Button) findViewById(R.id.calculator_clear);
        clearButton.setOnClickListener(this);
        resultView = (TextView) findViewById(R.id.result_calculator);
        FontHelper.applyFont(this,v,"fonts/myfont.ttf");
        //** ready to calculate **//
        prepare();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.calculator_cal:
                String input = (inputText.getText()).toString();
                if(isCorrect(input)) {
                    suffixTextView.setText("The suffix is: "+suffix(input));
                   try {
                       String result = Double.toString(calculate(suffix(input)));
                       resultView.setText(result);
                   }
                   catch (Exception e)
                   {
                       e.printStackTrace();
                       Toast.makeText(this,"Error in calculation!",Toast.LENGTH_SHORT).show();
                   }
                    finally {
                       mStack.clear();
                       sb.delete(0,sb.length());
                   }
                }
                else {
                    Toast.makeText(this,getString(R.string.wrong_input),Toast.LENGTH_SHORT).show();
                    sb.delete(0,sb.length());
                    mStack.clear();
                }
                break;
            case R.id.calculator_clear:
                suffixTextView.setText("");
                inputText.setText("");
                sb.delete(0,sb.length());
                mStack.clear();
                break;
            case android.R.id.home:
            {
                if(NavUtils.getParentActivityName(this)!=null)
                {
                    NavUtils.navigateUpFromSameTask(this);
                }
                break;
            }
        }
    }


    /**
     * Change the formula into suffix
     * Notice that ()*+
     */
    Stack<Character> mStack = new Stack<>();
    StringBuilder sb =new StringBuilder();
    private String suffix(String s)
    {
        s=s+"+0";
        char[] array = s.toCharArray();
        for(int i = 0;i<array.length;i++) {
            char c = array[i];
            switch (c) {
                case '+':
                case '-':
                    getOper(c, 1);
                    break;
                case '*':
                case '/':
                    getOper(c, 2);
                    break;
                case '(':
                    mStack.push(c);
                    break;
                case ')':
                    getParent(c);
                    break;
                default:
                    sb.append(c);
                    if(i <= array.length - 2 && charset2.contains(array[i+1]))
                        sb.append(" ");
                    break;
            }
        }
        //end of "for"
            while(!mStack.isEmpty()){
                sb.append(' ');
                sb.append(mStack.pop());
            }
            return sb.toString();
        }


    /**
     * if c = ")" need to find responding "("
     */

    private void getParent(char c) {
        while(!mStack.isEmpty()){
            char chx=mStack.pop();
            if(chx=='('){
                break;
            }else{
                sb.append(chx);
            }
        }
    }



    /**
     * @param ch Operate formula
     * @param prec1 The priority of Operation
     *
     */
    private void getOper(char ch, int prec1) {
        while (!mStack.isEmpty()) {
            char operTop = mStack.pop();
            if (operTop == '(') {
                mStack.push(operTop);
                break;
            } else {
                int prec2;
                if (operTop == '+' || operTop == '-') {
                    prec2 = 1;
                } else {
                    prec2 = 2;
                }
                if (prec1 > prec2) {
                    mStack.push(operTop);
                    break;
                } else {
                    sb = sb.append(operTop);
                }
            }
        }// end while
        mStack.push(ch);
    }

    private void prepare()
    {
        charset.add('(');
        charset.add(')');
        charset.add('+');
        charset.add('-');
        charset.add('*');
        charset.add('/');
        charset2.addAll(charset);
        charset.add('.');
        for(char c ='0';c<='9';c++)
        {
            charset.add(c);
        }

    }



    private boolean isCorrect(String s)
    {
        boolean state = true;
        int count1 = 0,count2 = 0;
        if (s.length() == 0) return false;
        for(int i =0;i<s.length();i++)
        {
            char c =s.charAt(i);
            if(!charset.contains(c))
            {
                state = false;
            }
            else if(c == '(')
            {
                count1++;
            }
            else if(c == ')')
            {
                count2++;
            }
        }
        if(count1 != count2)
            state = false;
        return state;
    }

    /**
     * Method to calculate the result
     */
    private Double calculate(String s) {
        Stack<Double> mystack = new Stack<>();
        int pointer1 = 0;
        for(int i = 0;i<s.length();i++)
        {
            char c = s.charAt(i);
            switch (c)
            {
                case ' ':
                    mystack.push(Double.parseDouble(s.substring(pointer1,i)));
                    pointer1 = i+1;
                    break;
                case '*':
                    pointer1++;
                    double a = mystack.pop() * mystack.pop();
                    mystack.push(a);
                    break;
                case '/':
                    double b = mystack.pop();
                    double e = mystack.pop();
                    pointer1++;
                    mystack.push(e/b);
                    break;
                case '+':
                    double d = mystack.pop() + mystack.pop();
                    mystack.push(d);
                    pointer1++;
                    break;
                case '-':
                    double g = mystack.pop();
                    pointer1++;
                    double f = mystack.pop();
                    mystack.push(f-g);
                    break;
                default:
                    // haha no one will see this
            }
        }
        return mystack.pop();

    }






}
