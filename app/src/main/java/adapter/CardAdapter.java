package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avenues.lib.testotpappnew.R;

import java.util.ArrayList;

import dto.CardTypeDTO;

/**
 * Created by amit.pande on 28/7/15.
 */
public class CardAdapter extends ArrayAdapter<CardTypeDTO> {
    private Activity context;
    ArrayList<CardTypeDTO> data = null;

    public CardAdapter(Activity context, int resource,
                       ArrayList<CardTypeDTO> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        return getDropDownView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        CardTypeDTO cardType = data.get(position);
        if (cardType != null) { // Parse the data from each object and set it.
            TextView cardName = (TextView) row.findViewById(R.id.item_value);
            cardName.setText(cardType.getCardName());
        }
        return row;
    }
}
