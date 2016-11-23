package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avenues.lib.testotpappnew.R;

import java.util.ArrayList;

import dto.PaymentOptionDTO;

/**
 * Created by amit.pande on 28/7/15.
 */
public class PayOptAdapter extends ArrayAdapter<PaymentOptionDTO> {
    private Activity context;
    ArrayList<PaymentOptionDTO> data = null;

    public PayOptAdapter(Activity context, int resource,
                         ArrayList<PaymentOptionDTO> data) {
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
        PaymentOptionDTO paymentOption = data.get(position);
        if (paymentOption != null) { // Parse the data from each object and set it.
            TextView optionName = (TextView) row.findViewById(R.id.item_value);
            optionName.setText(paymentOption.getPayOptName());
        }
        return row;
    }
}
