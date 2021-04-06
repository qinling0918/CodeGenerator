package me.tsinling.code.generator;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import me.tsinling.code.generator.utils.HistoryHelper;
import me.tsinling.code.generator.utils.InputHelper;
import me.tsinling.code.generator.utils.Md5Util;
import me.tsinling.code.generator.utils.PrefUtils;
import me.tsinling.code.generator.utils.ToastUtils;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView auto_tv_factory_name;
    private AutoCompleteTextView auto_tv_factory_code;
    private CheckBox cb_set_time;
    private EditText et_valid_time;
    private Spinner sp_time_unit;
    private LinearLayout ll_time;
    private Button bt_generate_code;
    private Button bt_verify_code;
    private CheckedTextView ctv_verify_result;

    //  private Md5Util.TimeUnit timeUnit = null;
    private int timeUnitPos = 2;

    private Context mContext;
    private HistoryHelper nameHistoryHelper;
    private HistoryHelper codeHistoryHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext = this;
        initViews();

    }

    private void initViews() {
        auto_tv_factory_name = findViewById(R.id.auto_tv_factory_name);
        auto_tv_factory_code = findViewById(R.id.auto_tv_factory_code);
        cb_set_time = findViewById(R.id.cb_set_time);
        et_valid_time = findViewById(R.id.et_valid_time);
        sp_time_unit = findViewById(R.id.sp_time_unit);
        ll_time = findViewById(R.id.ll_time);
        bt_generate_code = findViewById(R.id.bt_generate_code);
        bt_verify_code = findViewById(R.id.bt_verify_code);
        ctv_verify_result = findViewById(R.id.ctv_verify_result);

        initFactoryName();
        initFactoryCode();
        initTimeSet();
        initGenerateCode();
        initVerifyCode();


    }


    private void initVerifyCode() {
        bt_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });

    }

    private void verifyCode() {
        if (isEmpty(auto_tv_factory_code, auto_tv_factory_name)) {
            return;
        }
        boolean verify = Md5Util.verify(auto_tv_factory_name.getEditableText().toString()
                , auto_tv_factory_code.getEditableText().toString());

        ctv_verify_result.setChecked(!verify);
        if (!verify) {
            ToastUtils.showError("校验失败!");
            ctv_verify_result.setText(getStringBuilder().append("\n").append("校验失败!"));
        } else {
            ToastUtils.showNormal("校验成功!");
            ctv_verify_result.setText(getStringBuilder().append("\n").append("校验成功!"));
        }
    }


    private void initGenerateCode() {
        bt_generate_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCode();
            }
        });


    }

    private void generateCode() {
        boolean isHaveValidTime = cb_set_time.isChecked();
        boolean isEmpty = isHaveValidTime
                ? isEmpty(et_valid_time, auto_tv_factory_name)
                : isEmpty(auto_tv_factory_name);

        if (isEmpty) {
            return;
        }
        int validTime = isHaveValidTime ? getValidTime() : 0;


        Md5Util.TimeUnit timeUnit = Md5Util.TimeUnit.matchTimeUnit((char) (timeUnitPos + (int) 'A'));
        String name = auto_tv_factory_name.getEditableText().toString();
        String code = Md5Util.generate(name, validTime, timeUnit);
        auto_tv_factory_code.setText(code);

        codeHistoryHelper.save(code);
        nameHistoryHelper.save(name);

        StringBuilder stringBuilder = getStringBuilder();
        ctv_verify_result.setText(stringBuilder);

    }

    private StringBuilder getStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(auto_tv_factory_name.getHint())
                .append(":\n")
                .append(auto_tv_factory_name.getEditableText())
                .append(auto_tv_factory_code.getHint())
                .append(":\n")
                .append(auto_tv_factory_code.getEditableText());
        return stringBuilder;
    }

    private int getValidTime() {
        try {
            int time = Integer.valueOf(et_valid_time.getText().toString());
            if (time <= 0) {
                ToastUtils.showError("有效时间必须大于 0");
                ctv_verify_result.setText("有效时间必须大于 0");
                time = 0;

                cb_set_time.setChecked(false);
            }
            if (time > 65535) {
                ToastUtils.showError("有效时间最大值为 65535");
                ctv_verify_result.setText("有效时间最大值为 65535,若时间不满足,请尝试更换时间单位");
                et_valid_time.setText("65535");
                time = 65535;
            }
            return time;
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            return 0;
        }

    }


    private boolean isEmpty(TextView... textviews) {
        if (null == textviews || textviews.length == 0) {
            return false;
        }
        for (TextView textview : textviews) {
            if (InputHelper.isEmpty(textview)) {
                ToastUtils.showShort("%s不能为空!", textview.getHint());
                return true;
            }
        }
        return false;
    }


    private void initTimeSet() {
        cb_set_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_time.setVisibility(isChecked ? View.VISIBLE : GONE);
            }
        });
        sp_time_unit.setSelection(timeUnitPos);
        sp_time_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeUnitPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void initFactoryName() {

        auto_tv_factory_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFactoryNameHistory();
                auto_tv_factory_name.showDropDown();
            }
        });
        initFactoryNameHistory();
    }

    private void initFactoryCode() {
        auto_tv_factory_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFactoryCodeHistory();
                auto_tv_factory_code.showDropDown();
            }
        });
        initFactoryCodeHistory();
    }

    private void initFactoryNameHistory() {

        nameHistoryHelper = HistoryHelper.getInstance(PrefUtils.FACTORY_NAME);
        initAutoComplete(auto_tv_factory_name, nameHistoryHelper.getAll());
    }

    private void initFactoryCodeHistory() {
        codeHistoryHelper = HistoryHelper.getInstance(PrefUtils.FACTORY_CODE);
        initAutoComplete(auto_tv_factory_code, codeHistoryHelper.getAll());
    }


    private void initAutoComplete(final AutoCompleteTextView auto, final String[] historys) {

        if (null == historys || historys.length == 0) {
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, historys);

        auto.setAdapter(adapter);
        auto.setDropDownHeight(550);
        auto.setThreshold(1);
        auto.setCompletionHint(String.format(Locale.CHINA, "最近的%d条记录", historys.length));
    /*    auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });*/

        auto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auto.setText(historys[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
