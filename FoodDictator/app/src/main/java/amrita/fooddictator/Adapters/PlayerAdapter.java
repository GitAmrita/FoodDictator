package amrita.fooddictator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import amrita.fooddictator.Objects.Player;
import amrita.fooddictator.R;

/**
 * Created by amritachowdhury on 8/9/17.
 */

public class PlayerAdapter extends ArrayAdapter<Player> {
    private final Context context;
    private final List<Player> allPlayers;

    public PlayerAdapter(Context context, List<Player> allPlayers) {
        super(context, R.layout.list_players, allPlayers);
        this.context = context;
        this.allPlayers = allPlayers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_players, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.player_name);
        TextView idView = (TextView) rowView.findViewById(R.id.player_id);
        TextView presentView = (TextView) rowView.findViewById(R.id.player_present);
        nameView.setText(allPlayers.get(position).getName());
        idView.setText(String.valueOf(allPlayers.get(position).getId()));
        presentView.setText(allPlayers.get(position).isSelected() ? "SELECTED" : "SELECT");
        return rowView;
    }
}
