package com.onefengma.taobuxiu.views.buys;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.CityCategory;
import com.onefengma.taobuxiu.model.entities.IronBuyPush;
import com.onefengma.taobuxiu.utils.DateUtils;
import com.onefengma.taobuxiu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/8/16
 */
public class PushListAdapter extends BaseAdapter {

    public List<IronBuyPush> myBuys = new ArrayList<>();

    @Override
    public int getCount() {
        return myBuys.size();
    }

    public void setMyBuys(List<IronBuyPush> myBuys) {
        if (myBuys == null) {
            return;
        }
        this.myBuys.clear();
        this.myBuys.addAll(myBuys);
        notifyDataSetChanged();
    }

    @Override
    public IronBuyPush getItem(int position) {
        return myBuys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.push_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final IronBuyPush ironBuyBrief = getItem(position);

        viewHolder.title.setText(ironBuyBrief.ironType + "/" + ironBuyBrief.material + "/" + ironBuyBrief.surface + "/" + ironBuyBrief.proPlace + "( " + CityCategory.instance().getCityDesc(ironBuyBrief.locationCityId) + ")");
        viewHolder.subTitle.setText(ironBuyBrief.length + "*" + ironBuyBrief.width + "*" + ironBuyBrief.height + " " + ironBuyBrief.toleranceFrom + "-" + ironBuyBrief.toleranceTo + " " + ironBuyBrief.numbers + "" + ironBuyBrief.unit);
        viewHolder.message.setText(StringUtils.getString(R.string.buy_item_message, ironBuyBrief.message));
        viewHolder.deadLine.setText(StringUtils.getString(R.string.buy_item_time_limit, DateUtils.getDateStr(System.currentTimeMillis() + ironBuyBrief.timeLimit)));

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.sub_title)
        TextView subTitle;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.dead_line)
        TextView deadLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
