package com.xuanxuan.NFChacker.OtherView;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.xuanxuan.NFChacker.R;
import com.xuanxuan.NFChacker.ToolsUnit;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private List<ItemBean> mData;
    private Context mContext;
    private String oldString;

    public ListViewAdapter(Context mContext, List<ItemBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_edittext, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ItemBean itemObj = mData.get(position);

        //This is important. Remove TextWatcher first.
        if (holder.editText1.getTag() instanceof TextWatcher) {
            holder.editText1.removeTextChangedListener((TextWatcher) holder.editText1.getTag());
        }

        holder.editText1.setText(itemObj.getReader());
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(position);
                if(ToolsUnit.RuleOrTable){
                    ToolsUnit.currentRule.remove(itemObj.getReader());
                    ToolsUnit.ruleAdapter.notifyDataSetChanged();
                }else {

                    ToolsUnit.currentMap.remove(itemObj.getReader());
                    ToolsUnit.tableAdapter.notifyDataSetChanged();
                }
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldString=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    itemObj.setReader("");
                } else {
                    itemObj.setReader(s.toString());
                    if(ToolsUnit.RuleOrTable){
                        ToolsUnit.currentRule=ToolsUnit.ChangeReader(ToolsUnit.currentRule,oldString,s.toString());
                    }else {
                    ToolsUnit.currentMap=ToolsUnit.ChangeReader(ToolsUnit.currentMap,oldString,s.toString());
                    }
                }
            }
        };

        holder.editText1.addTextChangedListener(watcher);
        holder.editText1.setTag(watcher);



        //This is important. Remove TextWatcher first.
        if (holder.editText2.getTag() instanceof TextWatcher) {
            holder.editText2.removeTextChangedListener((TextWatcher) holder.editText2.getTag());
        }

        holder.editText2.setText(itemObj.getCard());

        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    itemObj.setCard("");
                } else {
                    itemObj.setCard(s.toString());

                    if(ToolsUnit.RuleOrTable){
                        ToolsUnit.currentRule=ToolsUnit.ChangeCard(ToolsUnit.currentRule,itemObj.getReader(),s.toString());
                    }else {
                        ToolsUnit.currentMap=ToolsUnit.ChangeCard(ToolsUnit.currentMap,itemObj.getReader(),s.toString());
                    }
                }
            }
        };

        holder.editText2.addTextChangedListener(watcher2);
        holder.editText2.setTag(watcher2);
        return convertView;
    }

    private class ViewHolder {
        private EditText editText1;
        private EditText editText2;
        private Button bt;

        public ViewHolder(View convertView) {
            editText1 = (EditText) convertView.findViewById(R.id.edit_text1);
            editText2 = (EditText) convertView.findViewById(R.id.edit_text2);
            bt = (Button)convertView.findViewById(R.id.button7);
        }
    }
}
