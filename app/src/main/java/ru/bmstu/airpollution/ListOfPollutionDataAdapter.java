package ru.bmstu.airpollution;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Formatter;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.bmstu.airpollution.model.PollutionData;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListOfPollutionDataAdapter extends RecyclerView.Adapter<ListOfPollutionDataAdapter.PollutionCardHolder> {
    private static int countItems;
    private List<PollutionData> pollutionDataList;

    ListOfPollutionDataAdapter(List<PollutionData> pollutionDataList) {
        setPollutionDataList(pollutionDataList);
    }

    @NonNull
    @Override
    public PollutionCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pollution_card, parent, false);

        return new PollutionCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollutionCardHolder cardHolder, int position) {
        if (pollutionDataList != null && pollutionDataList.size() > position) {
            PollutionData pollutionData = pollutionDataList.get(position);


            cardHolder.setData(pollutionData);
        }
    }

    @Override
    public int getItemCount() {
        return getPollutionDataList().size();
    }

    class PollutionCardHolder extends ViewHolder {
        ImageView imageView;
        TextView labelView;
        TextView precisionView;
        TextView pressureView;
        TextView valueView;

        PollutionCardHolder(@NonNull View v) {
            super(v);

            imageView = v.findViewById(R.id.toxin_image);
            labelView = v.findViewById(R.id.toxin_label);
            precisionView = v.findViewById(R.id.pollution_precision);
            pressureView = v.findViewById(R.id.pollution_pressure);
            valueView = v.findViewById(R.id.pollution_value);
        }

        void setData(PollutionData pollutionData) {
            switch (pollutionData.getToxin().getType()) {
                case CO:
                    imageView.setImageResource(R.drawable.carbon_monoxide);
                    break;
                case SO2:
                    imageView.setImageResource(R.drawable.sulfur_dioxide);
                    break;
            }
            labelView.setText(pollutionData.getToxin().getLabel());
            precisionView.setText(new Formatter().format("%16.3e", pollutionData.getPrecision()).toString());
            pressureView.setText(new Formatter().format("%16.3e", pollutionData.getPressure()).toString());
            pressureView.setTextColor(Color.BLACK);
            valueView.setText(new Formatter().format("%16.3e", pollutionData.getValue()).toString());
            valueView.setTextColor(Color.BLACK);

//            Log.i("Value: ", ""+pollutionData.getValue());

            if (pollutionData.getPressure() > 215 || pollutionData.getPressure() < 0.00464) {
                pressureView.setTextColor(Color.RED);
            }

            switch (pollutionData.getToxin().getType()) {
                case CO:
                    if (pollutionData.getValue() > 20) {
                        valueView.setTextColor(Color.RED);
                    }
                    break;
                case SO2:
                    if (pollutionData.getValue() > 0.5) {
                        valueView.setTextColor(Color.RED);
                    }
            }

        }
    }
}

